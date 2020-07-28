package com.mytraining.javatraining.rdf;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.Sail;

public class SailRepoWrap {
	Repository repository;

	public SailRepoWrap(Sail sail){
		repository = new SailRepository(sail);
	}

}
