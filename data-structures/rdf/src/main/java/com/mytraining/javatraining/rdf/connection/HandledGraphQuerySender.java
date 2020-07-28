package com.mytraining.javatraining.rdf.connection;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFHandler;

public class HandledGraphQuerySender extends QueryThread<Boolean> {
	private final Repository repository;
	private final String query;
	private final RDFHandler handler;

	public HandledGraphQuerySender(Repository repository, String query, RDFHandler handler) {
		this.repository = repository;
		this.query = query;
		this.handler = handler;
	}

	@Override
	protected Boolean process() {
		try (RepositoryConnection connection = repository.getConnection()) {
			GraphQuery graphQuery = connection.prepareGraphQuery(query);
			graphQuery.evaluate(handler);
			return true;
		}
	}
}
