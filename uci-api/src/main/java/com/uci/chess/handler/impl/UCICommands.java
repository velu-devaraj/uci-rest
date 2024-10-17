package com.uci.chess.handler.impl;

import java.util.HashMap;

public class UCICommands {
	public final static String isready = "isready";
	public final static String uci = "uci";
	public final static String lineFeed = "\n";

	
	public final static String readyok = "readyok";
	
	public final static String error = "error";
	static HashMap<String,Object> outputSet = new HashMap<>();
	{
		init();
	}
	public static void init() {
//		outputSet.put("position","");
		outputSet.put("go","bestmove");
		outputSet.put("fen", Integer.valueOf(1));
	}
	
	public static Object mapResponse(String commandName) {	
		return outputSet.get( commandName.split(" ")[0] );
	}

}
