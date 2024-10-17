package com.uci.chess.handler.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessManager {

	public ProcessManager(Process process) {
		super();
		this.process = process;
		lock = new ReentrantLock();
	}
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	public Lock getLock() {
		return lock;
	}
	public void setLock(Lock lock) {
		this.lock = lock;
	}
	Process process;
	Lock lock;
	
	
}
