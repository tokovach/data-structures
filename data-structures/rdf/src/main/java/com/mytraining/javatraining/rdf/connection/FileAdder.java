package com.mytraining.javatraining.rdf.connection;

import java.io.File;
import java.io.IOException;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAdder extends UpdateThread {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileAdder.class);
	private final Repository repository;
	private final File file;
	private final String baseURI;
	private final RDFFormat rdfFormat;
	private final Resource context;

	public FileAdder(Repository repository, File file, String baseURI, RDFFormat rdfFormat,
					Resource context) {
		this.repository = repository;
		this.file = file;
		this.baseURI = baseURI;
		this.rdfFormat = rdfFormat;
		this.context = context;
	}

	@Override
	void process() {
		try (RepositoryConnection connection = repository.getConnection()) {
			connection.add(file, baseURI, rdfFormat, context);
		} catch (IOException e) {
			Thread.currentThread().interrupt();
			LOGGER.error(e.getMessage());
		}
	}
}
