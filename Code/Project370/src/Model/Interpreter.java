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

import Controller.Controller;

public class Interpreter {

    private Robot robot;
    private Stack<String> stack;
    private JsonObject parsedJson;
    private HashMap<String, String> basicWords;
    private HashMap<String, String> userWords;
    private HashMap<String, String> variables;
    private Controller controller;

    /**
     * Constructor for interpreter.
     * 
     * @param robot The robot the interpreter is tied to.
     */
    public Interpreter(Robot robot) {
        this.robot = robot;
        this.stack = new Stack<String>();
        basicWords = new HashMap<String, String>();
        userWords = new HashMap<String, String>();
        variables = new HashMap<String, String>();
        controller = Controller.getInstance();


        // load basic words


        // load words from code
        try {
            parsedJson =
                    (new JsonParser().parse(new FileReader(robot.getPath()))).getAsJsonObject();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        codeArr = parsedJson.getAsJsonObject("code").getAsJsonArray();

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
            if (inDefinition) {// if flagged as currently in forth comment
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
     * @param word
     */
    public void runWord(String word) {

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
    }


    // ARITHMETIC FUNCTIONS

    /**
     * Subtracts the first int on stack from the second int on the stack, and pushes result back
     * onto the stack.
     */
    void subtract() {

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp2) - Integer.parseInt(temp1)));
    }

    /**
     * Adds the first int on stack to the second int on the stack, and pushes result back onto the
     * stack.
     */
    void add() {

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp2) + Integer.parseInt(temp1)));
    }

    /**
     * Multiplies the first int on stack to the second int on the stack, and pushes result back onto
     * the stack.
     */
    void multiply() {

        String temp1 = stack.pop();
        String temp2 = stack.pop();
        stack.push(Integer.toString(Integer.parseInt(temp1) * Integer.parseInt(temp2)));
    }

    /**
     * Divides the first int on stack to the second int on the stack, and pushes the quotient and
     * the remainder back onto the stack (in that order).
     */
    void divide() {

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

        stack.pop();
    }

    /**
     * Duplicates value at top of stack.
     */
    void dup() {

        String temp = stack.pop();
        stack.push(temp);
        stack.push(temp);
    }

    /**
     * Swaps the top two elements on that stack.
     */
    void swap() {

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

        String temp3 = stack.pop();
        String temp2 = stack.pop();
        String temp1 = stack.pop();
        stack.push(temp3);
        stack.push(temp1);
        stack.push(temp2);
    }

    // LOGIC FUNCTIONS

    /**
     * 
     * @param code
     */
    void ifCond(ListIterator<String> code) {

        if (stack.pop().equals("true")) {

            return;
        } else if (stack.pop().equals("false")) {

            goTo(code, "else");
        } else {
            // Invalid value
        }
    }

    /**
     * Only called to skip else code.
     * 
     * @param codeIterator iterator of current code to pass into goTo function.
     */
    void elseCond(ListIterator<String> codeIterator) {

        goTo(codeIterator, "then");
    }

    /**
     * Checks to see if the top two values on the stack are equal to each other. Pushes "true" onto
     * the stack if they are, "false" otherwise.
     */
    void equalTo() {

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

        String val = stack.pop();

        if (val.equals("true")) {

            stack.push("false");
        } else if (val.equals("false")) {

            stack.push("true");
        } else {

            // error invalid value TODO
        }
    }


    // ROBOT FUNCTIONS

    /**
     * Pushes the max health of the robot onto the stack.
     */
    void health() {

        // push max health
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
        controller.G_Move();
    }

    /**
     * Turns the robot once to the right.
     */
    void turn() {
        int newDirection = Integer.parseInt(stack.pop());

        while (newDirection > 0) {
            controller.G_turnRight();
            newDirection--;
        }
    }

    /**
     * Shoots on the target square.
     */
    void shoot() {
        // TODO make shoot target.
        controller.G_Attack();
    }

    void scan() {
        // TODO
    }

    void identify() {
        // TODO
    }



    // MAILBOX FUNCTIONS

    /**
     * Sends message to other team member. Returns a boolean indicating success or failure. Failure
     * can occur if the robot is dead (health = 0), or its mailbox is full. Because mailboxes aren't
     * implemented for this program, the message is never sent and "false" is pushed.
     */
    void send() {

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
     * Goes to target from current position
     * 
     * @param list ListIterator to seek through.
     * @param target Target String.
     */
    void goTo(ListIterator<String> code, String target) {

        int nestLevel = 0;
        String currWord;
        while (!((currWord = code.next()).equals(target)) && nestLevel == 0) {

            if (currWord.equals("if") || currWord.equals("do") || currWord.equals("begin")) {
                nestLevel++;
            } else if (currWord.equals("then") || currWord.equals("loop")
                    || currWord.equals("until")) {
                nestLevel--;
            }
        } ;
    }

    /**
     * Pops, and prints string at top of stack.
     */
    void popPrint() {

        System.out.println(stack.pop());
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

    }
}
