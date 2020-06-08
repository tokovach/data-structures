package com.mytraining.javatraining.graph.rdftest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResultHandler;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class RepoTest {
	public static void main(String[] args) throws IOException {
		String fileDir = "/home/dev/IdeaProjects/java-training/data-structures/graph/src/main/java/com/mytraining/javatraining/graph/rdftest/ex012.ttl";
		BufferedReader reader = new BufferedReader(new FileReader(fileDir));
		Model model = Rio.parse(reader,"", RDFFormat.TURTLE);

		Repository db = new SailRepository(new MemoryStore());
		db.init();
		RepositoryConnection connection = db.getConnection();
		connection.add(model);
		RepositoryResult<Statement> result = connection.getStatements(null,null,null);
		for (Statement statement : result){
			System.out.println(statement);
		}
		String queryString = "PREFIX ab: <http://learningsparql.com/ns/addressbook#> \n";
		queryString += "PREFIX d: <http://learningsparql.com/ns/data#> \n";
		queryString += "SELECT ?s ?n \n";
		queryString += "WHERE { \n";
		queryString += "    ?s ab:firstName ?n .";
		queryString += "}";
		TupleQuery query = connection.prepareTupleQuery(queryString);
		TupleQueryResult queryResult = query.evaluate();

		for (BindingSet binding : queryResult){
			System.out.println(binding.getValue("s"));
			System.out.println(binding.getValue("n"));
		}
		TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
		query.evaluate(handler);

		String queryString1 = "PREFIX ab: <http://learningsparql.com/ns/addressbook#> \n";
		queryString1 += "PREFIX d: <http://learningsparql.com/ns/data#> \n";
		queryString1 += "CONSTRUCT \n";
		queryString1 += "WHERE { \n";
		queryString1 += "    ?s ab:lastName ?n .";
		queryString1 += "}";

		GraphQuery query1 = connection.prepareGraphQuery(queryString1);
		GraphQueryResult result1 = query1.evaluate();

		for (Statement statement : result1){
			System.out.println(statement);
		}
		RDFHandler handler1 = Rio.createWriter(RDFFormat.TURTLE,System.out);
		query1.evaluate(handler1);
		connection.close();
		db.shutDown();
	}
}
