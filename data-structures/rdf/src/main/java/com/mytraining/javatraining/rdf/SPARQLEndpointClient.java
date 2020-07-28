package com.mytraining.javatraining.rdf;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

public class SPARQLEndpointClient {
	private Repository repository;

	public SPARQLEndpointClient(String sparqlEndpoint) {
		this.repository = new SPARQLRepository(sparqlEndpoint);
		repository.init();
	}
}
