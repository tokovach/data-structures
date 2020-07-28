package com.mytraining.javatraining.rdf;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class HTTPRepositoryClient {
	private Repository repository;
	private RepositoryConnection connection;

	public HTTPRepositoryClient(String server, String repositoryID){
		this.repository = new HTTPRepository(server,repositoryID);
		repository.init();
	}


}
