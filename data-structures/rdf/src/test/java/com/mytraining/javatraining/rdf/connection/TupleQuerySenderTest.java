package com.mytraining.javatraining.rdf.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.junit.jupiter.api.Test;

class TupleQuerySenderTest {
	String uri = "http://localhost:7200/repositories/test";
	HTTPRepository repository = new HTTPRepository(uri);
	String basicQuery = "SELECT distinct ?y WHERE { ?x ?p ?y } ";
	String prefix = "http://example.com/resource/gdp";

	/**
	 * Always start up server before at localhost:7200
	 */
	@Test
	void testQueryReceivedFromGraphDB() {
		repository.init();
		TupleQuerySender sender = new TupleQuerySender(repository, basicQuery);
		List<BindingSet> result = sender.process();
		for (BindingSet set : result){
			System.out.println(set.getValue("y"));
		}
	}

}