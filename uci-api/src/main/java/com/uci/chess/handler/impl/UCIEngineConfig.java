package com.uci.chess.handler.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class UCIEngineConfig {

	private final Logger log = LoggerFactory.getLogger(UCIEngineConfig.class);

	@Value("classpath:engine-parameters.json")
	private Resource paramFile;

	@Bean
	public EngineParams uciEngineParams() throws Exception {

		InputStream inputStream = paramFile.getInputStream();

		ObjectMapper om = new ObjectMapper();
		log.debug("UCIService started");
		String jsonString = new String(inputStream.readAllBytes());
		// String jsonString = Files.readString(file);
		JsonNode n = om.readTree(jsonString);

		return new EngineParams(n);
	}


}