package com.uci.chess.handler.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;

import com.uci.chess.model.Command;
import com.uci.chess.model.CommandResponse;

public class UCIService {

	private final Logger log = LoggerFactory.getLogger(UCIService.class);

	ProcessBuilder processBuilder;

	public UCIService(EngineParams uciEngineParams) throws IOException {
		log.debug("UCIService started");

		String enginePath = uciEngineParams.getString("enginePath");
		log.debug("enginePath {}", enginePath );
		String engineExecutable = uciEngineParams.getString("engineExecutable");
		log.debug("engineExecutable {}", engineExecutable );
		String workingDirectory = uciEngineParams.getString("workingDirectory");
		log.debug("workingDirectory {}", workingDirectory );
		int engineProcessMaxCount = uciEngineParams.getInt("engineProcessMaxCount");
		int retrycount = uciEngineParams.getInt("retryCount");
		int milliSecWait = uciEngineParams.getInt("milliSecWait");

		processBuilder = new ProcessBuilder(enginePath + File.separator + engineExecutable);
		processBuilder.directory(new File(workingDirectory));
		log.debug("Exe path : {} ",enginePath + File.separator + engineExecutable);
		File f = new File(enginePath + File.separator + engineExecutable);
		log.debug("File: exists {} , path {} , executable {}, size {} " , f.exists(), f.getAbsolutePath(),f.canExecute() , f.getTotalSpace());
		
		processBuilder = new ProcessBuilder(enginePath + File.separator + engineExecutable);
		processBuilder.directory(new File(workingDirectory));

		processPoolManager = ProcessPoolManager.getManager(engineProcessMaxCount, retrycount, milliSecWait,
				processBuilder);

	}

	// one instance is created of ProcessPoolManager and returned to all callers
	ProcessPoolManager processPoolManager;

	public CommandResponse execute(Command command) throws IOException {

		String sid = RequestContextHolder.currentRequestAttributes().getSessionId();
		
		ProcessManager pm = null;
		log.debug("execute {} sid {}", Thread.currentThread().toString(), sid);
		try {

			// one instance of ProcessPoolManager exists
			// synchronized call on getNextFreeProcess() acquires a lock and 
			// released in this call
			// no reference to the process or process resources are maintained beyond this
			// call
			pm = processPoolManager.getNextFreeProcess();

			CommandResponse cr = new CommandResponse();
			if (null == pm) {
				log.debug("Error: all engine processes are busy, none available {}", sid);
				addError(cr, "Error: all engine processes are busy, none available");
				return cr;
			}
			log.debug("pid,sid : {} {} ",pm.getProcess().pid(),sid);
			BufferedWriter bw = pm.getProcess().outputWriter();
			BufferedReader br = pm.getProcess().inputReader();
			BufferedReader er = pm.getProcess().errorReader();

			// flush out previous responses
		
			log.debug("flush error stream 1:");
			String errorString = flushout(er);
			log.debug("error stream: {} ", errorString );
			
			
			flushout(br);

			for (String cline : command.getCommandString()) {
				bw.write(cline);
				if (!cline.endsWith("\n")) {
					bw.newLine();
				}
				bw.flush();

				// marker to detect end of response
				Object endMarker = UCICommands.mapResponse(cline);

				if (null != endMarker) {
					readResponse(cr, endMarker, br);
				}

			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {

				log.debug("Error: command interrupted", e);
				addError(cr, "Error: command interrupted");				
				return cr;
			}
			bw.write(UCICommands.isready);
			bw.newLine();
			bw.flush();

			String outputMarker = UCICommands.readyok;

			readResponse(cr, outputMarker, br);

			log.debug("flush error stream 2:");
			errorString = flushout(er);
			log.debug("error stream: {} ", errorString );
			log.debug("output stream:");
			String resString = flushout(br);
			cr.addEngineResultItem(resString);

			if (!StringUtils.isEmpty(errorString)) {
				addError(cr, errorString);
			}
			log.debug(errorString);

			return cr;
		} finally {
			if (null != pm) {
				// release the lock for process
				pm.getLock().unlock();
			}
		}

	}

	private String flushout(BufferedReader reader ) throws IOException {
		char[] buf = new char[8000];
		int off = 0;

		while (reader.ready()) {
			log.debug("Stream flush");
			int c = reader.read(buf, off, 0);
			if (c == -1 || c == 0) {
				break;
			} else {
				off += c;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {

				log.debug("Error: command interrupted", e);
			}
		}
		String outStr = new String(buf, 0, off);
		return outStr;
	}

	private void addError(CommandResponse cr, String line) {
		cr.setError(true);
		cr.addErrorStringItem(line);
	}

	private void readResponse(CommandResponse cr, Object endMarker, BufferedReader br) throws IOException {
		String line;
		int endCount = 1;

		String endString = null;

		boolean endByCount = endMarker instanceof Integer;

		if (endByCount) {
			endCount = Integer.parseInt((String) endMarker);
		} else {
			endString = (String) endMarker;
		}

		int i = 0;
		String sid = RequestContextHolder.currentRequestAttributes().getSessionId();

		if (!br.ready()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {

				log.debug("sid {} ", sid);
				log.debug("Error: command interrupted", e);
			}
		}
		while (null != (line = br.readLine())) {

			log.debug("sid {}, {}" ,sid, line);
			i++;

			cr.addEngineResultItem(line);
			if (!endByCount) {
				if (line.startsWith(endString)) {

					break;
				}
			} else {
				if (i == endCount) {
					break;
				}
			}
			if (line.startsWith(UCICommands.error)) {
				addError(cr, line);				
				break;
			}

		}
	}

}
