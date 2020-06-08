package com.mytraining.javatraining.graph.rdftest;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;

public class RDFUtil {

	public static void listLiteralsFromFilteredModel(Model model, Resource resource, IRI predicate, Value value) {
		Model filteredModel = model.filter(resource, predicate, value);
		for (Statement statement : filteredModel) {
			System.out.println(statement);
		}
	}
}
