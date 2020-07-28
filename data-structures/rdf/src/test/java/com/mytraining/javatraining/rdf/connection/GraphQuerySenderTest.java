package com.mytraining.javatraining.rdf.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.junit.jupiter.api.Test;

class GraphQuerySenderTest {
	String uri = "http://localhost:7200/repositories/w1";
	HTTPRepository repository = new HTTPRepository(uri);
	String basicQuery = "describe ?p WHERE { ?p ?x \"craigellis@yahoo.com\" } ";
	String prefix = "http://example.com/resource/gdp";

	/**
	 * Always start up server before at localhost:7200
	 */
	@Test
	void testQueryReceivedFromGraphDB() {
		repository.init();
		GraphQuerySender sender = new GraphQuerySender(repository, basicQuery);
		Model result = sender.process();
		for (Statement statement : result){
			System.out.println(statement.toString());
		}
	}
}