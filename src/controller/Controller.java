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
		completeState = storage.readStorage();
		//storage = new Storage();
		//completeState = storage.readStorage();
		
		
		//---------for testing if read into the state class successfully
		
		System.out.println("items in completed list: " +completeState.completedEvents.size());
		System.out.println("items in incompleted list: " + completeState.incompletedEvents.size());
		System.out.println("items in floating list: " + completeState.floatingEvents.size());
		System.out.println("items in displayed list: " + completeState.displayedEvents.size());
		//TODO: load event lists from storage
		
		 
		
	}
	
	/**
	 * constructor which accepts information from existing storage
	 * Will receive info from storage then call parser to store into completeState
	 * @param placeHolderForDataFromStorageFile
	 */
	public Controller(String placeHolderForDataFromStorageFile){
		completeState = new State();
		parser = new Parser();
		storage = new Storage();
		completeState = storage.readStorage();
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
		Command userCommand;
		userCommand = parser.parseCommand(commandText); //parser should return Command
		System.out.println(completeState.incompletedEvents.size());
		userCommand.execute(completeState);
		Storage.clearStorage();
		completeState.stateToStorage();
		return completeState;
	}
}
