package com.mytraining.javatraining.graph.rdftest;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;

public class TestJ {
	ValueFactory valueFactory = SimpleValueFactory.getInstance();

	String ex = "http://example.org/";

	IRI picasso = valueFactory.createIRI(ex, "Picasso");
	IRI artist = valueFactory.createIRI(ex, "Artist");

	Model model = new TreeModel();

	public void addToModel() {
		/* First Statement - Picasso is an artist. */
		model.add(picasso, RDF.VALUE, artist);

		/* Second Statement - Picasso's first name is Pablo. */
		model.add(picasso, FOAF.FIRST_NAME, valueFactory.createLiteral("Pablo"));
	}

}
