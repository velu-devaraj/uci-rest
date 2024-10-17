package com.uci.chess.handler.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uci.chess.handler.*;
import com.uci.chess.model.About;
import com.uci.chess.model.AboutResponse;

@Service
public class AboutApiDelegateImpl implements AboutApiDelegate {

	public ResponseEntity<AboutResponse> aboutApp() {

		About about = new About();
		about.setDescription("Chess Application");
		about.addEnginesItem("LC0");
		AboutResponse ar = new AboutResponse();
		ar.setAbout(about);
		ResponseEntity<AboutResponse> re = new ResponseEntity<AboutResponse>(ar, HttpStatus.OK);

		return re;

	}

}
