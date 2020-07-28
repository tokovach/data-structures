package com.mytraining.javatraining.rdf.connection;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class TupleQuerySender extends QueryThread<java.util.List<BindingSet>> {
	private final Repository repository;
	private final String query;

	public TupleQuerySender(Repository repository, String query) {
		this.repository = repository;
		this.query = query;
	}

	@Override
	protected java.util.List<BindingSet> process() {
		try (RepositoryConnection connection = repository.getConnection()) {
			TupleQuery tupleQuery = connection.prepareTupleQuery(query);
			try (TupleQueryResult result = tupleQuery.evaluate()) {
				return QueryResults.asList(result);
			}
		}
	}
}
