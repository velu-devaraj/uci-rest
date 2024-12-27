package com.uci.chess.handler.impl;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessPoolManager {
	private final Logger log = LoggerFactory.getLogger(ProcessPoolManager.class);

	private final int poolSize;

	private List<ProcessManager> processPool;
	ProcessBuilder processBuilder;
	
	
	public static synchronized ProcessPoolManager getManager(int poolSize, int retryCount, long milliSecWait, ProcessBuilder processBuilder) {
		if(null != poolManager) {
			return poolManager;
		}
		
		poolManager = new ProcessPoolManager( poolSize,  retryCount,  milliSecWait,  processBuilder) ;
		return poolManager;
	}

	private static ProcessPoolManager poolManager;
	
	private ProcessPoolManager(int poolSize, int retryCount, long milliSecWait, ProcessBuilder processBuilder) {
		this.poolSize = poolSize;
		this.retryCount = retryCount;
		this.milliSecWait = milliSecWait;
		this.processBuilder = processBuilder;
		processPool = new ArrayList<>();
	}

	int retryCount;
	long milliSecWait;

	public synchronized ProcessManager getNextFreeProcess() throws IOException {
		for (ProcessManager pm : processPool) {
			if (pm.getLock().tryLock()) {
				return pm;
			}
		}

		if (poolSize > processPool.size()) {
			// create new process, add and return;
			try {
			Process p = processBuilder.start();
			log.debug("process info : user {}, cmd,  {}, cmdline {} ",p.info().user(), p.info().command(), p.info().commandLine());
			ProcessManager pm = new ProcessManager(p);
			if (pm.getLock().tryLock()) {
				processPool.add(pm);
				return pm;
			}
			}catch(Exception e) {
				log.debug("process start:",e);
			}
		}

		for (int i = 0; i < retryCount; i++) {
			for (ProcessManager pm : processPool) {
				try {
					if (pm.getLock().tryLock(milliSecWait, TimeUnit.MILLISECONDS)) {
						return pm;
					}
				} catch (InterruptedException e) {

					log.error("Error locking process:", e);
					return null;
				}
			}
		}
		log.error("Error all engine process are busy, none available");

		return null;
	}

	public void shutdown() {
		for (ProcessManager process : processPool) {
			process.getProcess().destroy();
		}
	}

}