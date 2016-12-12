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
    	
    	
    	System.out.println(robot.filePath);
    	
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
        
        System.out.println(parsedJson.toString());
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

        while (!(stack.isEmpty())) {

            String curWord = stack.pop();

            if (inComment > 0) {// if flagged as currently in forth comment
                if (curWord.equals(")")) {// checks to see if at end of comment
                    inComment--;
                }
                if (curWord.equals("(")) {// checks to see start of nested comment
                    inComment++;
                }
                continue;
            }
            if (inDefinition) {// if flagged as currently in definition
                if (curWord.equals(";")) {// checks to see if at end of definition
                    inDefinition = false;
                    userWords.put(functionName, functionBody);
                } else {
                    functionBody.concat(" ");
                    functionBody.concat("curWord");
                }
                continue;
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
                    functionName = curWord;
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
    	
    	System.out.println(robot.filePath);
    	
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
    	
    	System.out.println(userWords.toString());
    	
        if (!(userWords.containsKey(word))) {

            throw new IllegalArgumentException("runWord: Argument is not a defined word!");
        }

        String body = userWords.get(word);
        ListIterator<String> wordsQ = Arrays.asList(body.split("\\s+")).listIterator();

        while (wordsQ.hasNext()) {

            String currWord = wordsQ.next();

            if (userWords.containsKey(currWord)) {

                runWord(word);
                continue;
            }

            if (basicWords.containsKey(word)) {

                switch (word) {
                    case "if":
                        ifCond(wordsQ);
                        break;
                    case "else":
                        elseCond(wordsQ);
                        break;
                    default:
                        Method method;
                        try {
                            method = getClass().getMethod(basicWords.get(word));
                            method.invoke(word);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                }
            }
            if (variables.containsKey(word)) {

                stack.push(word);
            }
        }
    }


    // ARITHMETIC FUNCTIONS

    /**
     * Subtracts the first int on stack from the second int on the stack, and pushes result back
     * onto the stack.
     */
    void subtract() {

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
    void add() {

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
    void multiply() {

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
    void divide() {

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
    void drop() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        stack.pop();
    }

    /**
     * Duplicates value at top of stack.
     */
    void dup() {

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
    void swap() {

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
    void rot() {

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
    void ifCond(ListIterator<String> codeIterator) {

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
    void elseCond(ListIterator<String> codeIterator) {

        goTo(codeIterator, "then");
    }

    /**
     * Checks to see if the top two values on the stack are equal to each other. Pushes "true" onto
     * the stack if they are, "false" otherwise.
     */
    void equalTo() {

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
    void notEqualTo() {

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
    void lessThan() {

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
    void lessThanEqual() {

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
    void greaterThan() {

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
    void greaterThanEqual() {

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
    void and() {

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
    void or() {

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
    void invert() {

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
    void health() {

        stack.push(Integer.toString(robot.getType()));
    }

    /**
     * Pushes the value of the remaining health onto the stack.
     */
    void healthLeft() {

        stack.push(Integer.toString(robot.getHealth()));
    }

    /**
     * Pushes the max moves of the robot onto the stack.
     */
    void moves() {

        stack.push(Integer.toString(robot.getMovementMax()));
    }

    /**
     * Pushes the amount of moves left onto the stack.
     */
    void movesLeft() {

        stack.push(Integer.toString(robot.getMovementCur()));
    }

    /**
     * Pushes the attack power of the robot onto the stack.
     */
    void attack() {

        stack.push(Integer.toString(robot.getDamage()));
    }

    /**
     * Pushes the range of the robot onto the stack.
     */
    void range() {

        stack.push(Integer.toString(robot.getRange()));
    }

    /**
     * Pushes the String of the team colour onto the stack.
     */
    void team() {

        stack.push(robot.getColourString());
    }

    /**
     * Pushes the string of the type of the robot onto the stack.
     */
    void type() {

        stack.push(robot.getStringType());
    }

    /**
     * Moves the robot forward.
     */
    void move() {
        Controller.getInstance().G_Move();
    }

    /**
     * Turns the robot once to the right.
     */
    void turn() {

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
    void shoot() {

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
    void scan() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        stack.push(Integer.toString(Controller.getInstance().gameBoard.getTargetList().size() - 1));
    }

    void identify() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }
        // TODO

    }

    void check() {

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
    void send() {

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
    void message() {

        stack.push("false");
    }

    void receive() {


    }


    // UTILITY FUNCTIONS

    /**
     * Goes forward to target from current position
     * 
     * @param codeIterator ListIterator to seek through.
     * @param target Target String.
     */
    void goTo(ListIterator<String> codeIterator, String target) {

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
    void goBackTo(ListIterator<String> codeIterator, String target) {

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
    void popPrint() {

        if (stack.isEmpty()) {

            throw new IllegalStateException("Stack is Empty!");
        }

        System.out.println(stack.pop());
    }

    /**
     * Gets the value of the variable on top of stack, and pushes to the stack.
     */
    void getVariable() {

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
    void setVariable() {

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
    void randomPush() {

        int i = (new Random()).nextInt(Integer.parseInt(stack.pop()));
        stack.push(Integer.toString(i));
    }

    /**
     * Function does nothing, simply a placeholder. An example of when this would be called is if
     * the word "Then" is reached, because it only signifies the end of an if statement, and does
     * nothing.
     */
    void nothing() {

        return;
    }



    // MAIN

    public static void main(String[] args) {
    	Controller.getInstance().gameMenu.setVisible(false);
    	Robot testRobot = new Robot(3,3);
    	testRobot.filePath = "src/Model/scripts/SittingDuck.json";
    	Interpreter interpret = new Interpreter(testRobot);
    }
}
