package com.mytraining.javatraining.graph.rdftest;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

public class RunnerThree {
	public static void main(String[] args){
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.org/");

		// In named graph 1, we add info about Picasso
		builder.namedGraph("ex:namedGraph1")
						.subject("ex:Picasso")
						.add(RDF.TYPE, EX.ARTIST)
						.add(FOAF.FIRST_NAME, "Pablo");

		// In named graph 2, we add info about Van Gogh.
		builder.namedGraph("ex:namedGraph2")
						.subject("ex:VanGogh")
						.add(RDF.TYPE, EX.ARTIST)
						.add(FOAF.FIRST_NAME, "Vincent");


		// We're done building, create our Model
		Model model = builder.build();

		// Each named graph is stored as a separate context in our Model
		for (Resource context: model.contexts()) {
			System.out.println("Named graph " + context + " contains: ");
			Rio.write(model.filter(null, null, null, context), System.out, RDFFormat.TURTLE);
		}
	}
}
