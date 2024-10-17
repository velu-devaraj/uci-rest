package com.uci.chess.handler.impl;

import com.fasterxml.jackson.databind.JsonNode;

public class EngineParams {

	JsonNode json;
	
	public EngineParams(JsonNode json){
		this.json = json;
	}
	
	
	public String getString(String name) {
		return json.get(name).asText();
	}
	
	public int getInt(String name) {
		return json.get(name).intValue();
	}
}
