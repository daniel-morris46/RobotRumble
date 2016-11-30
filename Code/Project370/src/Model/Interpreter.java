package Model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.google.gson.*;

public class Interpreter {

	private Robot robot;
	private Stack<String> stack;
	private JsonObject parsedJson; 
	private HashMap<String, String> wordMap;
	private HashMap<String, String> variableMap;
	
	/**
	 * Constructor for interpreter.
	 * @param robot The robot the interpreter is tied to.
	 */
	public Interpreter(Robot robot) {
		this.robot = robot;
		this.stack = new Stack<String>();
		//parsedJson = new JsonObject();
		wordMap = new HashMap<String, String>();
		variableMap = new HashMap<String, String>();
		try {
		parsedJson = (new JsonParser().parse(new FileReader(robot.getPath()))).getAsJsonObject();
    	} catch (JsonParseException e) {
    		e.printStackTrace();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Getter for value in JSON for robot string information.
	 * @param key Key for the value to be returned.
	 * @return String value associated with Key.
	 */
	public String getStringValue(String key){
		
		return parsedJson.getAsJsonObject(key).getAsString();
	}
	
	/**
	 * Getter for value in JSON for robot integer information.
	 * @param key Key for the value to be returned.
	 * @return Integer value associated with Key.
	 */
	public int getIntValue(String key){
		
		return parsedJson.getAsJsonObject(key).getAsInt();
	}
	
	/**
	 * Parses code to set-up dictionaries. 
	 */
	public void parseCode(){
		
		JsonArray codeArr;
		
		wordMap.clear();
		variableMap.clear();
		stack.clear();

		codeArr = parsedJson.getAsJsonObject("code").getAsJsonArray();
		
		boolean inComment = false;
		boolean inDefinition = false;
		String functionBody = "";
		String functionName = "";
		
	    for (int i = 0; i < codeArr.size(); i++ ){
	    	String curLine = codeArr.get(i).getAsString(); //get first line
	    	
	    	String[] lineWords = curLine.split("\\s+");
	    	stack.addAll(Arrays.asList(lineWords));
	    }
	    	
    	while(!(stack.isEmpty())){
    		
    		String curWord = stack.pop();
    		
    		if (inComment){//if flagged as currently in forth comment
    			if (curWord.equals(")")){//checks to see if at end of comment
    				inComment = false;
	    		}
    			continue;
    		}
    		if (inDefinition){//if flagged as currently in forth comment
				if (curWord.equals(";")){//checks to see if at end of definition
					inDefinition = false;
					wordMap.put(functionName, functionBody);
	    		}
				else{
					functionBody.concat(" ");
					functionBody.concat("curWord");
				}
    			continue;
    		}
    		
    		
    		switch (curWord){
    		case "(": //if start of forth comment is detected
	    		inComment = true;
    		case "variable":
    			variableMap.put(stack.pop(), "0");
    			stack.pop();
    		case ":":
    			inDefinition = true;
    			functionName = curWord;
    			functionBody = "";
    		}
	    }
	}
	
	/**
	 * Runs the word passed in. I
	 * @param word
	 */
	public void runWord(String word){
		
		if (!(wordMap.containsKey(word))){
			
			throw new IllegalArgumentException("runWord: Argument is not a defined word!");
		}
		
		String body = wordMap.get(word);
		LinkedList<String> wordsQ = (LinkedList<String>) Arrays.asList(body.split("\\s+"));
		
		while(!(wordsQ.isEmpty())){
			
			String currWord = wordsQ.removeFirst();
			
			if(wordMap.containsKey(currWord)){
				
				// runs function code
			    
			}
			if(variableMap.containsKey(currWord)){
				
				
			}
			if(isArithmetic(currWord)){
				
				
			}
		}
	}
	

    private boolean isArithmetic(String currWord) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
        Color test = Color.red;
        System.out.println(test.red);

    }
}
