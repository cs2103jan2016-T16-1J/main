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
 * The Controller class handles information between the storage, parser and UI, and invokes the logic to handle different commands
 * @@author Reem Razak
 */
public class Controller{
	
	//@@author Reem
	private State completeState;
	private Parser parser;
	private Storage storage;

	/**
	 * Default Controller constructor
	 */
	public Controller() {
		parser = new Parser();
		storage = new Storage();
		storage.createFile(Storage.storageFile);
		completeState = storage.readStorage(Storage.storageFile);
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
		
		completeState = userCommand.execute(completeState);
		assert isValidCommand(userCommand);
		//assert false;
		storage.stateToStorage(completeState, directory);
		pushToEventHistory();
		
		return completeState;
	}
	
	/**
	 * Pushes the current state to the event history. This method is used to maintain history for the Undo Command
	 */
	private void pushToEventHistory(){
		State newState = new State(completeState);
		
		completeState.eventHistory.push(newState);
		
	}
	
	/**
	 * Checks that the command that has been provided is valid
	 * @param userCommand
	 * @return
	 */
	private boolean isValidCommand(Command userCommand){
		if((userCommand != null) && (completeState.getStatusMessage() != State.MESSAGE_PARSE_ERROR)){
			return true;
		}		
		return false;
	}		
	
	/**
	 * Returns all the events in the State ordered by their similarity to the String userInput
	 * This method should be called from the UI when the user is typing, so that they are able to receive recommendations on event names
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
	
	/**
	 * Sorts the already levenshtein-calculated array by levenshtein distance.
	 * The more similar event names should be at the beginning of the array
	 * @param map
	 * @return
	 */
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
	
	/**
	 * Calculates the levenshtein distance between two strings
	 * Levenshtein distance determines the similarity between two strings
	 * @param stringOne
	 * @param stringTwo
	 * @return the levenshteinDistance
	 */
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
