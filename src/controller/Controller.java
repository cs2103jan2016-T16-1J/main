package controller;

import java.io.IOException;
import java.text.ParseException;

import main.State;
import parser.Parser;
import storage.Storage;
import command.Command;
import json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		completeState = storage.readStorage(Storage.storageFile);
		
	}
	
	public State getCompleteState() {
		return completeState;
	}

	/**
	 * This method is invoked from the UI when the user gives a command
	 * It calls the parser and receives a Command object in return.
	 * The Command is then executed
	 * @param commandText
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public State executeCommand(String commandText) throws IOException, JSONException{
		completeState.setStatusMessage(null);
		Command userCommand;
		userCommand = parser.parseCommand(commandText); //parser should return Command
		if(null == userCommand){
			completeState.setStatusMessage(State.MESSAGE_PARSE_ERROR);
		}
		System.out.println(completeState.incompletedEvents.size());
		userCommand.execute(completeState);
		assert isValidCommand(userCommand);
		//assert false;
		storage.stateToStorage(completeState, storage.getDirectory());
		return completeState;
	}
	
	private boolean isValidCommand(Command userCommand){
		if((userCommand != null) && (completeState.getStatusMessage() != State.MESSAGE_PARSE_ERROR)){
			return true;
		}
		
		return false;
	}
	
	
}
