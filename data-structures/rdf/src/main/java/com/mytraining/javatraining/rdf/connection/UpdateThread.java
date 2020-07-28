package com.mytraining.javatraining.rdf.connection;

public abstract class UpdateThread implements Runnable {

	@Override
	public void run() {
		process();
	}

	/**
	 * Abstract method used to process requests.
	 */
	abstract void process();
}
