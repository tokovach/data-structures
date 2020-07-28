package com.mytraining.javatraining.rdf.connection;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class GraphQuerySender extends QueryThread<Model> {
	private final Repository repository;
	private final String query;

	public GraphQuerySender(Repository repository, String query) {
		this.repository = repository;
		this.query = query;
	}

	@Override
	protected Model process() {
		try (RepositoryConnection connection = repository.getConnection()) {
			GraphQuery graphQuery = connection.prepareGraphQuery(query);
			try (GraphQueryResult result = graphQuery.evaluate()) {
				return QueryResults.asModel(result);
			}
		}
	}
}
