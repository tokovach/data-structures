package com.mytraining.javatraining.rdf.connection;

import java.util.Collection;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class StatementAdder extends UpdateThread {
	private final Repository repository;
	private final Collection<Statement> statements;
	private final Resource context;

	public StatementAdder(Repository repository, Collection<Statement> statements,
					Resource context) {
		this.repository = repository;
		this.statements = statements;
		this.context = context;
	}

	@Override
	void process() {
		try (RepositoryConnection connection = repository.getConnection()) {
			connection.add(statements, context);
		}
	}
}
