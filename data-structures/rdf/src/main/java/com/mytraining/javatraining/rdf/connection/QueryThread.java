package com.mytraining.javatraining.rdf.connection;

import java.util.concurrent.Callable;

public abstract class QueryThread<V> implements Callable<V> {
	
	@Override
	public V call() throws Exception {
		return process();
	}


	/**
	 * Abstract method used to process requests.
	 */
	abstract V process();
}
