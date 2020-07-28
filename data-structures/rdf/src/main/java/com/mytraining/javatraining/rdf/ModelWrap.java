package com.mytraining.javatraining.rdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.util.RDFCollections;

public class ModelWrap {
	private ModelBuilder builder;
	private Model model;
	private ValueFactory factory;
	private String namespace;
	private String prefix;

	public ModelWrap(String prefix, String namespace) {
		this.prefix = prefix;
		this.namespace = namespace;
		this.builder = new ModelBuilder();
		this.model = builder.setNamespace(prefix, namespace).build();
		this.factory = SimpleValueFactory.getInstance();
	}

	public IRI createIRI(String name) {
		Objects.requireNonNull(name);
		return factory.createIRI(name);
	}

	public Literal createLiteral(String value) {
		Objects.requireNonNull(value);
		return factory.createLiteral(value);
	}

	public Literal createLiteral(Integer value) {
		Objects.requireNonNull(value);
		return factory.createLiteral(value);
	}

	public Statement createStatement(Resource resource, IRI predicate, Value value) {
		Objects.requireNonNull(resource);
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(value);
		return factory.createStatement(resource, predicate, value);
	}

	public void addStatement(Statement statement) {
		model.add(statement);
	}

	public void appendModel(Model model) {
		this.model.addAll(model);
	}

	public void consumeStatements(Consumer<Statement> consumer) {
		for (Statement statement : model) {
			consumer.accept(statement);
		}
	}

	public void consumeFilteredStatements(Consumer<Statement> consumer, Resource resource, IRI predicate, Value value) {
		for (Statement statement : getModelFromFilter(resource, predicate, value)) {
			consumer.accept(statement);
		}
	}

	public Set<Literal> getLiterals(Resource resource, IRI predicate) {
		return Models.getPropertyLiterals(model, resource, predicate);
	}

	public Set<Resource> getSubjectsFromModel(Model model) {
		return model.subjects();
	}

	public void updatePropertyValue(Resource resource, IRI predicate, Value newValue) {
		Models.setProperty(model, resource, predicate, newValue);
	}

	public Set<IRI> getPredicatesFromModel(Model model) {
		return model.predicates();
	}

	public Set<Value> getValuesFromModel(Model model) {
		return model.objects();
	}

	public Model getModelFromFilter(Resource resource, IRI predicate, Value value) {
		return model.filter(resource, predicate, value);
	}

	public Model getBaseModel() {
		return model;
	}

	public Model createRDFCollection(List<Literal> literals, Resource subject, IRI collectionProperty, Model model) {
		Resource blankNode = factory.createBNode();
		Model collectionModel = RDFCollections.asRDF(literals, blankNode, model);
		collectionModel.add(subject, collectionProperty, blankNode);
		return collectionModel;
	}

	public Model getRDFCollection(Model rdfCollection, Resource collectionHolder, IRI holdingProperty) {
		return RDFCollections.getCollection(rdfCollection,
						getResourceValueFromModel(rdfCollection, collectionHolder, holdingProperty), new LinkedHashModel());
	}

	public List<Value> revertRDFCollection(Model rdfCollection, Resource collectionHolder, IRI holdingProperty) {
		return RDFCollections.asValues(rdfCollection,
						getResourceValueFromModel(rdfCollection, collectionHolder, holdingProperty), new ArrayList<>());
	}

	public void removeCollectionFromModel(Model mainModel, Model removableCollection, Resource collectionHolder,
					IRI holdingProperty) {
		mainModel.removeAll(removableCollection);
		mainModel.remove(collectionHolder, holdingProperty,
						getResourceValueFromModel(mainModel, collectionHolder, holdingProperty));
	}

	private Resource getResourceValueFromModel(Model model, Resource subject, IRI predicate) {
		return (Resource) Objects.requireNonNull(Models.object(model.filter(subject, predicate, null)).orElse(null));
	}
}
