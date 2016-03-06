package controller;

import java.text.ParseException;
import state.State;
import parser.Parser;
import command.Command;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller{
	
	public static State completeState;
	Parser parser;
	
	public Controller(){
		//called if there is no information in storage- ie no tasks preloaded into program
		completeState = new State();
		parser = new Parser();
	}
	
	public Controller(String placeHolderForDataFromStorageFile){
		//Will receive info from storage then call parser to store into completeState
		completeState = new State();
		parser = new Parser();
	}
	
	public void executeCommand(String commandText){
		Command userCommand;
		/*userCommand = */parser.parseCommand(commandText); //parser should return Command
		//userCommand.execute();
	}
}
