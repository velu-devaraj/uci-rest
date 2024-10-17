package com.uci.chess.handler.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.uci.chess.handler.CommandsApiDelegate;
import com.uci.chess.model.Command;
import com.uci.chess.model.CommandResponse;

@Service
//@RequestScope
public class CommandsApiDelegateImpl implements CommandsApiDelegate {
	private final UCIService uciService;	
	
	private final Logger log = LoggerFactory.getLogger(CommandsApiDelegateImpl.class);

	
	//EngineParams uciEngineParams;
	@Autowired
	CommandsApiDelegateImpl(EngineParams uciEngineParams) throws IOException{
		UCICommands.init();
		uciService = new UCIService(uciEngineParams);
		log.debug("uciService created" );
	}



	
    public  ResponseEntity<CommandResponse> execute(Command command) {
    	String sid = RequestContextHolder.currentRequestAttributes().getSessionId();
    	log.debug("command session : %s".formatted(sid) );
    	
		CommandResponse cr = new CommandResponse();
		try {
			cr = uciService.execute(command);
		} catch (Exception e) {
			log.debug("Unknown error : ",e);
			cr.addErrorStringItem("Unknown error : " + e.getLocalizedMessage() );
			cr.setError(true);

			e.printStackTrace();
		}

		ResponseEntity<CommandResponse> re = ResponseEntity.status(HttpStatus.OK).body(cr);

		return re;

        }

}
