package com.uci.chess.handler.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uci.chess.handler.EngineApiDelegate;
import com.uci.chess.model.Engine;
import com.uci.chess.model.EngineInfoResponse;

@Service
public class EngineApiDelegateImpl implements EngineApiDelegate {
	public ResponseEntity<EngineInfoResponse> engineName() {
	
		Engine eng = new Engine();
		eng.setDescription("Leela Chess Engine");
		eng.engineName("LC0");
		eng.link("https://github.com/LeelaChessZero/lc0");
		EngineInfoResponse enr = new EngineInfoResponse();
		enr.setEngine(eng);
		
		ResponseEntity<EngineInfoResponse> re =  ResponseEntity.status(HttpStatus.OK).body(enr);
		return re;
	}
}
