package com.mytraining.javatraining.graph.rdftest;

import static com.mytraining.javatraining.graph.rdftest.RDFUtil.listLiteralsFromFilteredModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.FOAF;

public class Runner {
	public static void main(String[] args) {
		SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();
		TestJ j = new TestJ("ex", "http://example.org/");
		Resource resource = j.createIRI("Boko");
		Resource resource1 = j.createIRI("Cvetko");
		Resource resource2 = j.createIRI("Cucka");
		j.addToModel(resource, DC.PUBLISHER, j.createIRI("Bullshit"));
		j.addToModel(resource2, DC.PUBLISHER, j.createIRI("Ugly shit"));
		j.addToModel(resource1, DC.CONTRIBUTOR, j.createIRI("Dumb shit"));
		j.addToModel(resource, FOAF.BIRTHDAY,
						valueFactory.createLiteral(new GregorianCalendar(1885, Calendar.APRIL, 1).getTime()));
		j.addToModel(resource, FOAF.AGE, valueFactory.createLiteral(69420));
		j.addToModel(resource2, FOAF.AGE, valueFactory.createLiteral(11111));
		for (Resource publisher : j.getModel().filter(null, DC.PUBLISHER, null).subjects()) {
			listLiteralsFromFilteredModel(j.getModel()
							, publisher, FOAF.AGE, null);
		}
	}
}
