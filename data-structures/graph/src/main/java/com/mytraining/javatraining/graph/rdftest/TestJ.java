package com.mytraining.javatraining.graph.rdftest;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJ {
	private static final Logger logger = LoggerFactory.getLogger(TestJ.class);
	private ValueFactory valueFactory = SimpleValueFactory.getInstance();
	private ModelBuilder builder;
	private Model model;
	private String namespace;
	private String prefix;

	public TestJ(String namespace, String prefix) {
		this.namespace = namespace;
		this.prefix = prefix;
		this.builder = new ModelBuilder();
		initModel();
	}

	public IRI createIRI(String localName) {
		return valueFactory.createIRI(prefix, localName);
	}

	public void addToModel(Resource subject, IRI predicate, Value object) {
		model.add(subject, predicate, object,createIRI("http://orga.org/"));
	}

	public Model getModel(){
		return model;
	}

	public void printStatements() {
		for (Statement statement : model) {
			logger.info(statement.toString());
		}
	}

	public void printLiteralValues(){
	}

	private void initModel() {
		model = builder.setNamespace(prefix, namespace).build();
	}
}
