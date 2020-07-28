package com.mytraining.javatraining.rdf;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.jupiter.api.Test;

class ModelWrapTest {
	String ex = "http://example.com/";
	String res = "http://resource.com/";
	String obj = "http://object.com/";
	String name = "ex:kurec";
	String name1 = "ex:turbo";
	String predicate = "ima";
	String predicate1 = "nqma";
	Integer integer = 69;
	Integer integer1 = 420;
	ModelWrap wrap;

	@Test
	void createIRI() {
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		System.out.println(iri);
		assertEquals(iri.toString(), name);
	}

	@Test
	void createLiteral() {
		wrap = new ModelWrap("ex", ex);
		Literal literal = wrap.createLiteral(name);
		Literal literal1 = wrap.createLiteral(integer);
		System.out.println(literal);
		System.out.println(literal1);
		assertEquals(literal.stringValue(), name);
		assertEquals(literal1.intValue(), integer);
	}

	@Test
	void createStatement() {
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		Literal literal = wrap.createLiteral(name1);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		System.out.println(statement);
		assertEquals(statement.getObject(), literal);
		assertEquals(statement.getSubject(), iri);
		assertNull(statement.getContext());
	}

	@Test
	void addConsumeStatement() {
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		Literal literal = wrap.createLiteral(name1);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		System.out.println(statement);
		wrap.addStatement(statement);
		wrap.consumeStatements((x) -> assertEquals(x, statement));
	}

	@Test
	void consumeFilteredStatements() {
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.consumeFilteredStatements((x) -> {
			System.out.println(x);
			assertEquals(x, statement1);
		}, null, FOAF.DOCUMENT, null);
		wrap.consumeFilteredStatements((x) -> {
			System.out.println(x);
			assertEquals(x, statement);
		}, iri, null, null);
	}

	@Test
	void getLiterals() {
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		List<String> arr = Arrays.asList(val, val1, val2);
		;
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri, FOAF.PERSON, literal1);
		Statement statement2 = wrap.createStatement(iri, FOAF.PERSON, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		for (Literal literal3 : wrap.getLiterals(iri, FOAF.PERSON)) {
			System.out.println(literal3.getLabel());
			assertTrue(arr.contains(literal3.getLabel()));
		}
	}

	@Test
	void getSubjectsFromModel() {
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		List<String> arr = Arrays.asList(name2, name, name1);
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.PERSON, literal1);
		Statement statement2 = wrap.createStatement(iri2, FOAF.PERSON, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		for (Resource resource : wrap.getSubjectsFromModel(wrap.getBaseModel())) {
			System.out.println(resource);
			assertTrue(arr.contains(resource.stringValue()));
		}
	}

	@Test
	void updatePropertyValue() {
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		Statement statement3 = wrap.createStatement(iri1, FOAF.PERSON, literal);
		Statement statement2 = wrap.createStatement(iri2, FOAF.PERSON, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		wrap.addStatement(statement3);
		System.out.println(statement1);
		wrap.updatePropertyValue(iri1, FOAF.DOCUMENT, wrap.createLiteral(12));
		assertEquals(1, Models.getPropertyLiterals(wrap.getBaseModel(), iri1, FOAF.DOCUMENT).size());
		assertEquals(12, Models.getPropertyLiteral(wrap.getBaseModel(), iri1, FOAF.DOCUMENT).get().intValue());
	}

	@Test
	void getPredicatesFromModel() {
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		List<IRI> arr = Arrays.asList(FOAF.PERSON, FOAF.DOCUMENT, FOAF.DNA_CHECKSUM);
		wrap = new ModelWrap("ex", ex);
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		Statement statement2 = wrap.createStatement(iri2, FOAF.DNA_CHECKSUM, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		for (IRI resource : wrap.getPredicatesFromModel(wrap.getBaseModel())) {
			System.out.println(resource);
			assertTrue(arr.contains(resource));
		}
	}

	@Test
	void createRDFCollection() {
		wrap = new ModelWrap("ex", ex);
		List<Literal> nums = Arrays.asList(wrap.createLiteral(1), wrap.createLiteral(2), wrap.createLiteral(3),
						wrap.createLiteral(4));
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		Statement statement2 = wrap.createStatement(iri2, FOAF.DNA_CHECKSUM, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		wrap.appendModel(wrap.createRDFCollection(nums, iri, FOAF.LABEL_PROPERTY, new LinkedHashModel()));
		Model newModel = new LinkedHashModel();
		for (Statement statement3 : wrap.getRDFCollection(wrap.getBaseModel(), iri, FOAF.LABEL_PROPERTY)) {
			System.out.println(statement3);
			newModel.add(statement3);
		}
		assertTrue(newModel.contains(null, RDF.FIRST, wrap.createLiteral(1)));
		assertTrue(newModel.contains(null, RDF.FIRST, wrap.createLiteral(3)));
		assertTrue(newModel.contains(null, RDF.TYPE, RDF.LIST));
		assertTrue(newModel.contains(null, RDF.REST, RDF.NIL));
	}

	@Test
	void revertRDFCollection() {
		wrap = new ModelWrap("ex", ex);
		List<Literal> nums = Arrays.asList(wrap.createLiteral(1), wrap.createLiteral(2), wrap.createLiteral(3),
						wrap.createLiteral(4));
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		Statement statement2 = wrap.createStatement(iri2, FOAF.DNA_CHECKSUM, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		wrap.appendModel(wrap.createRDFCollection(nums, iri, FOAF.LABEL_PROPERTY, new LinkedHashModel()));
		for (Value literal3 : wrap.revertRDFCollection(wrap.getBaseModel(), iri, FOAF.LABEL_PROPERTY)){
			System.out.println(literal3);
		}
		assertEquals(wrap.revertRDFCollection(wrap.getBaseModel(), iri, FOAF.LABEL_PROPERTY), nums);
	}

	@Test
	void removeCollection() {
		wrap = new ModelWrap("ex", ex);
		List<Literal> nums = Arrays.asList(wrap.createLiteral(1), wrap.createLiteral(2), wrap.createLiteral(3),
						wrap.createLiteral(4));
		String name2 = "ex:dinq";
		String val = "ex:tomiii";
		String val1 = "ex:didaaaa";
		String val2 = "ex:coolio";
		IRI iri = wrap.createIRI(name);
		IRI iri1 = wrap.createIRI(name1);
		IRI iri2 = wrap.createIRI(name2);
		Literal literal = wrap.createLiteral(val);
		Literal literal1 = wrap.createLiteral(val1);
		Literal literal2 = wrap.createLiteral(val2);
		Statement statement = wrap.createStatement(iri, FOAF.PERSON, literal);
		Statement statement1 = wrap.createStatement(iri1, FOAF.DOCUMENT, literal1);
		Statement statement2 = wrap.createStatement(iri2, FOAF.DNA_CHECKSUM, literal2);
		wrap.addStatement(statement);
		wrap.addStatement(statement1);
		wrap.addStatement(statement2);
		wrap.appendModel(wrap.createRDFCollection(nums, iri, FOAF.LABEL_PROPERTY, new LinkedHashModel()));
		Model model =wrap.getRDFCollection(wrap.getBaseModel(), iri, FOAF.LABEL_PROPERTY);
		wrap.removeCollectionFromModel(wrap.getBaseModel(),model,iri,FOAF.LABEL_PROPERTY);
		assertThrows(NullPointerException.class,()->wrap.getRDFCollection(wrap.getBaseModel(), iri, FOAF.LABEL_PROPERTY));
	}
}