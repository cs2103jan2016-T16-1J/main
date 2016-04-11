package controller;

import java.io.IOException;
import main.GenericEvent;
import main.State;
import parser.Parser;
import storage.Storage;
import command.Command;
import json.JSONException;
import java.util.*;

/**
 * Controller class- handles information between UI Parser and Storage
 * @author Reem
 *
 */
public class Controller{
	
	private State completeState;
	private Parser parser;
	private Storage storage;

	/**
	 * default constructor
	 * called if there is no information in storage- ie no tasks preloaded into program
	 */
	public Controller() {
		parser = new Parser();
		storage = new Storage();
		storage.createFile(Storage.storageFile);
		completeState = storage.readStorage(Storage.storageFile);
	}
	
	public Controller (String directory){
		parser = new Parser();
		storage = new Storage();
		completeState = storage.readStorage(directory);
	}
	
	public State getCompleteState() {
		return completeState;
	}
	
	//for testing use only
	public void setCompleteState(State state){
		this.completeState = state;
	}
	
	/**
	 * This method is invoked from the UI when the user gives a command
	 * It calls the parser and receives a Command object in return.
	 * The Command is then executed
	 * @param commandText
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public State executeCommand(String commandText, String directory) throws IOException, JSONException{
		completeState.clearStatusMessage();
		Command userCommand;
		userCommand = parser.parseCommand(commandText); //parser should return Command
		if(null == userCommand){
			completeState.setStatusMessage(State.MESSAGE_PARSE_ERROR);
			return completeState;
		}
		
		userCommand.execute(completeState);
		assert isValidCommand(userCommand);
		//assert false;
		storage.stateToStorage(completeState, directory);
		
		completeState.eventHistory.push(completeState);
		
		return completeState;
	}
	
	private boolean isValidCommand(Command userCommand){
		if((userCommand != null) && (completeState.getStatusMessage() != State.MESSAGE_PARSE_ERROR)){
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * May need to move this method to State.java
	 * Returns all the events in the State ordered by their similarity to the String userInput
	 * @param userInput
	 * @return matches
	 */
	public ArrayList<GenericEvent> bestMatchingOrder(String userInput){
		ArrayList<GenericEvent> allEvents = completeState.getAllEvents();
		
		HashMap<GenericEvent, Integer> distanceMap = new HashMap<GenericEvent, Integer>();
		
		for(GenericEvent e : allEvents){
			distanceMap.put(e, levenshteinDistance(userInput, e.getName()));
			
		}
		
		ArrayList<GenericEvent> matches = sortByValues(distanceMap);
		
		
		return matches;
	}
	
	private ArrayList<GenericEvent> sortByValues(HashMap<GenericEvent, Integer> map){
		
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	    	   
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });


	       ArrayList<GenericEvent> matches = new ArrayList<GenericEvent>();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              matches.add((GenericEvent)entry.getKey());
	       } 
	       return matches;
	}
	
    private int levenshteinDistance(String stringOne, String stringTwo) {
        stringOne = stringOne.toLowerCase();
        stringTwo = stringTwo.toLowerCase();
        
        int [] costs = new int [stringTwo.length() + 1];
        
        for (int j = 0; j < costs.length; j++){
            costs[j] = j;
        }
        
        for (int i = 1; i <= stringOne.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= stringTwo.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), stringOne.charAt(i - 1) == stringTwo.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[stringTwo.length()];
    }
	
	
	
}
