package Model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;
import java.util.Stack;
import java.lang.reflect.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import Controller.Controller;

public class Interpreter {

    private Robot robot;
    private Stack<String> stack;
    private JsonObject parsedJson;
    private HashMap<String, String> basicWords;
    private HashMap<String, String> userWords;
    private HashMap<String, String> variables;

    /**
     * Constructor for interpreter.
     * 
     * @param robot The robot the interpreter is tied to.
     */
    public Interpreter(Robot robot) {
    	
        this.robot = robot;
        stack = new Stack<String>();
        variables = new HashMap<String, String>();
        userWords = new HashMap<String, String>();

        if(robot.filePath == "" || robot.filePath == null)
    		return;
        
        try {
            // load words from code
            parsedJson =
                    (new JsonParser().parse(new FileReader(robot.getPath() ) ) ).getAsJsonObject();

            // load basic words
            String temp = (new JsonParser().parse(new FileReader("src/Model/basicWords.json"))).toString();
            basicWords = new Gson().fromJson(temp,
                    new TypeToken<HashMap<String, String>>() {}.getType());
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        parseCode();
    }

    /**
     * Getter for value in JSON for robot string information.
     * 
     * @param key Key for the value to be returned.
     * @return String value associated with Key.
     */
    public String getStringValue(String key) {

        return parsedJson.getAsJsonObject(key).getAsString();
    }

    /**
     * Getter for value in JSON for robot integer information.
     * 
     * @param key Key for the value to be returned.
     * @return Integer value associated with Key.
     */
    public int getIntValue(String key) {

        return parsedJson.getAsJsonObject(key).getAsInt();
    }

    /**
     * Parses code to set-up dictionaries.
     */
    public void parseCode() {

        JsonArray codeArr;

        userWords.clear();
        variables.clear();
        stack.clear();
        
        
        JsonObject script = parsedJson.getAsJsonObject("script");
        codeArr = script.get("code").getAsJsonArray();
        
        int inComment = 0;
        boolean inDefinition = false;
        String functionBody = "";
        String functionName = "";

        for (int i = 0; i < codeArr.size(); i++) {
            String curLine = codeArr.get(i).getAsString(); // get first line

            String[] lineWords = curLine.split("\\s+");
            stack.addAll(Arrays.asList(lineWords));
        }
        Stack<String> reverseStack = new Stack<String>();
        
        while (!(stack.isEmpty())) {
        	reverseStack.push(stack.pop());
        }
        
        stack = reverseStack;

        while (!(stack.isEmpty())) {

            String curWord = stack.pop();
            
            if (curWord.equals("(")) {// checks to see if at end of comment
                inComment++;
                continue;
            }
            
            if (inComment > 0) {// if flagged as currently in forth comment
                if (curWord.equals(")")) {// checks to see if at end of comment
                    inComment--;
                }
                if (curWord.equals("(")) {// checks to see start of nested comment
                    inComment++;
                }
            } else {
            	if (inDefinition) {// if flagged as currently in definition
                    if (curWord.equals(";")) {// checks to see if at end of definition
                        inDefinition = false;
                        userWords.put(functionName, functionBody);
                    } else {
                        functionBody = functionBody + " " + curWord;
                    }
                }
            }
            
        	switch (curWord) {
            case "(": // if start of forth comment is detected
                inComment++;
                break;
            case "variable":
                variables.put(stack.pop(), "0");
                stack.pop();
                break;
            case ":":
                inDefinition = true;
                functionName = stack.pop();
                functionBody = "";
                break;
        	}
        }
    }

    /**
     * Runs the word passed in. I
     * 
     * @param word Word to be run.
     */
    public void runWord(String word) {
    	
    	//If the script was not initially parsed
    	if(basicWords == null){
    		try {
                // load words from code
                parsedJson =
                        (new JsonParser().parse(new FileReader(robot.getPath() ) ) ).getAsJsonObject();

                // load basic words
                String temp = (new JsonParser().parse(new FileReader("src/Model/basicWords.json"))).toString();
                basicWords = new Gson().fromJson(temp,
                        new TypeToken<HashMap<String, String>>() {}.getType());
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            parseCode();
    	}
    	
        if (!(userWords.containsKey(word))) {

            throw new IllegalArgumentException("runWord: Argument is not a defined word!");
        }
        System.out.println(variables.toString());

        String body = userWords.get(word);
        ListIterator<String> wordsQ = Arrays.asList(body.split("\\s+")).listIterator();

        System.out.println(body);
        
        while (wordsQ.hasNext()) {
        	
            String currWord = wordsQ.next();
            
            System.out.println(currWord);
            if (userWords.containsKey(currWord)) {
            	System.out.println("User word - " + currWord);
                runWord(currWord);
                continue;
            }

            if (basicWords.containsKey(currWord)) {
            	System.out.println("Basic word - " + currWord);
                switch (currWord) {
                    case "if":
                        ifCond(wordsQ);
                        break;
                    case "else":
                        elseCond(wordsQ);
                        break;
                    default:
                        Method method;
                        try {
                        	System.out.println(basicWords.get(currWord));
                        	
                            method = this.getClass().getMethod(basicWords.get(currWord).toString());
                            method.invoke(this);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            System.out.println(e.getCause());
                        }
                }
            }
            
            if (variables.containsKey(currWord)) {
            	System.out.println("Pushed to stack - " + currWord);
                stack.push(currWord);
            }
        }
    }


    // ARITHMETIC FUNCTIONS

    /**
     * Subtracts the first int on stack from the second int on the stack, and pushes result back
     * onto the stack.
     */
    public void subtract() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp2) - Integer.parseInt(temp1)));
    }

    /**
     * Adds the first int on stack to the second int on the stack, and pushes result back onto the
     * stack.
     */
    public void add() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp2) + Integer.parseInt(temp1)));
    }

    /**
     * Multiplies the first int on stack to the second int on the stack, and pushes result back onto
     * the stack.
     */
    public void multiply() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp1) * Integer.parseInt(temp2)));
    }

    /**
     * Divides the first int on stack to the second int on the stack, and pushes the quotient and
     * the remainder back onto the stack (in that order).
     */
    public void divide() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp2) % Integer.parseInt(temp1)));
        stack.push(Integer.toString(Integer.parseInt(temp2) / Integer.parseInt(temp1)));
    }

    // STACK FUNCTIONS

    /**
     * Pops top of stack.
     */
    public void drop() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        stack.pop();
    }

    /**
     * Duplicates value at top of stack.
     */
    public void dup() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp = stack.pop();
        stack.push(temp);
        stack.push(temp);
    }

    /**
     * Swaps the top two elements on that stack.
     */
    public void swap() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp2 = stack.pop();
        String temp1 = stack.pop();
        stack.push(temp2);
        stack.push(temp1);
    }

    /**
     * Rotates the top 3 elements, so that the 2nd and 3rd value sit on top of the original first
     * value.
     */
    public void rot() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String temp3 = stack.pop();
        String temp2 = stack.pop();
        String temp1 = stack.pop();
        stack.push(temp3);
        stack.push(temp1);
        stack.push(temp2);
    }

    // LOGIC FUNCTIONS

    /**
     * Checks to see if top value is "true" or "false". If true, runs next code. If false, skips to
     * else statement.
     * 
     * @param codeIterator Iterator of current code to pass into goTo function.
     */
    public void ifCond(ListIterator<String> codeIterator) {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val = stack.pop();
        if (val.equals("true")) {

            return;
        } else if (val.equals("false")) {

            goTo(codeIterator, "else");
        } else {
            // Invalid value
        }
    }

    /**
     * Only called to skip else code after running "true" statement code in an if.
     * 
     * @param codeIterator Iterator of current code to pass into goTo function.
     */
    public void elseCond(ListIterator<String> codeIterator) {
    	return;
        //goTo(codeIterator, "then");
    }

    /**
     * Checks to see if the top two values on the stack are equal to each other. Pushes "true" onto
     * the stack if they are, "false" otherwise.
     */
    public void equalTo() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val2 = stack.pop();
        String val1 = stack.pop();

        if (val2.equals(val1)) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Checks to see if the top two values on the stack are not equal to each other. Pushes "true"
     * onto the stack if they are, "false" otherwise.
     */
    public void notEqualTo() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val2 = stack.pop();
        String val1 = stack.pop();

        if (!(val2.equals(val1))) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Checks if the first int on stack is less than the second int on the stack. Pushes "true" onto
     * the stack if it is, "false" otherwise.
     */
    public void lessThan() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int val2 = Integer.parseInt(stack.pop());
        int val1 = Integer.parseInt(stack.pop());

        if (val2 < val1) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Checks if the first int on stack is less than, or equal to, the second int on the stack.
     * Pushes "true" onto the stack if it is, "false" otherwise.
     */
    public void lessThanEqual() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int val2 = Integer.parseInt(stack.pop());
        int val1 = Integer.parseInt(stack.pop());

        if (val2 <= val1) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Checks if the first int on stack is greater than the second int on the stack. Pushes "true"
     * onto the stack if it is, "false" otherwise.
     */
    public void greaterThan() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int val2 = Integer.parseInt(stack.pop());
        int val1 = Integer.parseInt(stack.pop());

        if (val2 > val1) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Checks if the first int on stack is greater than, or equal to, the second int on the stack.
     * Pushes "true" onto the stack if it is, "false" otherwise.
     */
    public void greaterThanEqual() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int val2 = Integer.parseInt(stack.pop());
        int val1 = Integer.parseInt(stack.pop());

        if (val2 >= val1) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Pushes false to the stack if either the top two values of the stack are false, else pushes
     * true.
     */
    public void and() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val2 = stack.pop();
        String val1 = stack.pop();

        if (val2.equals("false") || val1.equals("false")) {

            stack.push("false");
        } else {

            stack.push("true");
        }
    }

    /**
     * Pushes true to the stack if either the top two values of the stack are true, else pushes
     * false.
     */
    public void or() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val2 = stack.pop();
        String val1 = stack.pop();

        if (val2.equals("true") || val1.equals("true")) {

            stack.push("true");
        } else {

            stack.push("false");
        }
    }

    /**
     * Inverts the top boolean on the stack.
     */
    public void invert() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String val = stack.pop();

        if (val.equals("true")) {

            stack.push("false");
        } else if (val.equals("false")) {

            stack.push("true");
        } else {

            throw new IllegalStateException("Value is invalid!");
        }
    }


    // LOOP FUNCTIONS

    //TODO
    
    

    // ROBOT FUNCTIONS

    /**
     * Pushes the max health of the robot onto the stack.
     */
    public void health() {

        stack.push(Integer.toString(robot.getType()));
    }

    /**
     * Pushes the value of the remaining health onto the stack.
     */
    public void healthLeft() {

        stack.push(Integer.toString(robot.getHealth()));
    }

    /**
     * Pushes the max moves of the robot onto the stack.
     */
    public void moves() {

        stack.push(Integer.toString(robot.getMovementMax()));
    }

    /**
     * Pushes the amount of moves left onto the stack.
     */
    public void movesLeft() {

        stack.push(Integer.toString(robot.getMovementCur()));
    }

    /**
     * Pushes the attack power of the robot onto the stack.
     */
    public void attack() {

        stack.push(Integer.toString(robot.getDamage()));
    }

    /**
     * Pushes the range of the robot onto the stack.
     */
    public void range() {

        stack.push(Integer.toString(robot.getRange()));
    }

    /**
     * Pushes the String of the team colour onto the stack.
     */
    public void team() {

        stack.push(robot.getColourString());
    }

    /**
     * Pushes the string of the type of the robot onto the stack.
     */
    public void type() {

        stack.push(robot.getStringType());
    }

    /**
     * Moves the robot forward.
     */
    public void move() {
        Controller.getInstance().G_Move();
    }

    /**
     * Turns the robot once to the right.
     */
    public void turn() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }
        int newDirection = Integer.parseInt(stack.pop());

        while (newDirection > 0) {
        	Controller.getInstance().G_turnRight();
            newDirection--;
        }
    }

    /**
     * Shoots on the target square.
     */
    public void shoot() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int direction = Integer.parseInt(stack.pop());
        int range = Integer.parseInt(stack.pop());
        Hex targetHex = Controller.getInstance().gameBoard.getHexWithDistanceAndRange(robot.getPosition(), range,
                direction);

        Controller.getInstance().gameBoard.currentHex = targetHex;
        Controller.getInstance().G_Attack();
    }

    /**
     * Pushes number of other robots seen to the stack.
     */
    public void scan() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        stack.push(Integer.toString(Controller.getInstance().gameBoard.getTargetList().size() - 1));
    }

    public void identify() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }
        // TODO

    }

    public void check() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        int direction = Integer.parseInt(stack.pop());
        Hex targetHex =
        		Controller.getInstance().gameBoard.getHexWithDistanceAndRange(robot.getPosition(), 1, direction);
        if (targetHex == null) {

            stack.push("OUT OF BOUNDS");
        } else {

            if (targetHex.getOcc().isEmpty()) {

                stack.push("EMPTY");
            } else {

                stack.push("OCCUPIED");
            }
        }
    }



    // MAILBOX FUNCTIONS

    /**
     * Sends message to other team member. Returns a boolean indicating success or failure. Failure
     * can occur if the robot is dead (health = 0), or its mailbox is full. Because mailboxes aren't
     * implemented for this program, the message is never sent and "false" is pushed.
     */
    public void send() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        stack.pop();
        stack.pop();
        stack.push("false");
    }

    /**
     * Checks whether robot has a waiting message. Returns true is present, false otherwise. Because
     * mailboxes aren't implemented for this program, "false" is pushed.
     */
    public void message() {

        stack.push("false");
    }

    public void receive() {


    }


    // UTILITY FUNCTIONS

    /**
     * Goes forward to target from current position
     * 
     * @param codeIterator ListIterator to seek through.
     * @param target Target String.
     */
    private void goTo(ListIterator<String> codeIterator, String target) {

        int nestLevel = 0;
        String currWord;
        while (codeIterator.hasNext() && !((currWord = codeIterator.next()).equals(target))
                && nestLevel == 0) {

            if (currWord.equals("if") || currWord.equals("do") || currWord.equals("begin")) {
                nestLevel++;
            } else if (currWord.equals("then") || currWord.equals("loop")
                    || currWord.equals("until")) {
                nestLevel--;
            }
        } ;
    }

    /**
     * Goes backward to target from current position
     * 
     * @param codeIterator ListIterator to seek through.
     * @param target Target String.
     */
    public void goBackTo(ListIterator<String> codeIterator, String target) {

        int nestLevel = 0;
        String currWord;
        while (codeIterator.hasPrevious() && !((currWord = codeIterator.previous()).equals(target))
                && nestLevel == 0) {

            if (currWord.equals("if") || currWord.equals("do") || currWord.equals("begin")) {
                nestLevel--;
            } else if (currWord.equals("then") || currWord.equals("loop")
                    || currWord.equals("until")) {
                nestLevel++;
            }
        } ;
    }

    /**
     * Pops, and prints string at top of stack.
     */
    public void popPrint() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        System.out.println(stack.pop());
    }

    /**
     * Gets the value of the variable on top of stack, and pushes to the stack.
     */
    public void getVariable() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String name = stack.pop();

        if (!(variables.containsKey(name))) {
            throw new IllegalStateException("Variable not defined!");
        }

        stack.push(variables.get(name));
    }

    /**
     * Sets value on top of stack to the variable defined below it.
     */
    public void setVariable() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        String value = stack.pop();
        String name = stack.pop();

        if (!(variables.containsKey(name))) {

            throw new IllegalStateException("Variable not defined!");
        }

        variables.put(name, value);
    }

    /**
     * Generates random number from 0 to i, where i is a popped int from stack.
     */
    public void randomPush() {

        int i = (new Random()).nextInt(Integer.parseInt(stack.pop()));
        stack.push(Integer.toString(i));
    }

    /**
     * Function does nothing, simply a placeholder. An example of when this would be called is if
     * the word "Then" is reached, because it only signifies the end of an if statement, and does
     * nothing.
     */
    public void nothing() {

        return;
    }



    // MAIN

    public static void main(String[] args) {
    	Robot testRobot = new Robot(3,3);
    	testRobot.filePath = "src/Model/scripts/SittingDuck.json";
    	Interpreter interpret = new Interpreter(testRobot);
    }
}
