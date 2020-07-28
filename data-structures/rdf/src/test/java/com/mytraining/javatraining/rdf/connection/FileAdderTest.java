package com.mytraining.javatraining.rdf.connection;

import java.io.File;
import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.Test;

class FileAdderTest {
	String uri = "http://localhost:7200/repositories/test";
	HTTPRepository repository = new HTTPRepository(uri);
	String basicQuery = "SELECT distinct ?p WHERE { ?x ?p ?y } ";
	String prefix = "http://example.com/resource/gdp";

	/**
	 * Always start up server before at localhost:7200
	 */
	@Test
	void testFileAddToGraphDB() {
		repository.init();
		File file = new File("/home/dev/IdeaProjects/java-training/data-structures/graph/src/main/java/com/mytraining/javatraining/graph/rdftest/ex002.ttl");
		FileAdder adder = new FileAdder(repository,file,null, RDFFormat.TURTLE,null);
		adder.process();
	}

}