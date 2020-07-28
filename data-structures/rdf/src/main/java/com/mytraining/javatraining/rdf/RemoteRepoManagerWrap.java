package com.mytraining.javatraining.rdf;

import java.io.File;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.config.RepositoryImplConfig;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.config.SailRepositoryConfig;
import org.eclipse.rdf4j.sail.config.SailImplConfig;
import org.eclipse.rdf4j.sail.inferencer.fc.config.SchemaCachingRDFSInferencerConfig;

public class RemoteRepoManagerWrap {
	private RepositoryManager manager;

	public RemoteRepoManagerWrap(String repoDir) {
		manager = new RemoteRepositoryManager(repoDir);
		manager.init();
	}

	public void addRepo(String repoId, SailImplConfig config) {
		manager.addRepositoryConfig(new RepositoryConfig(repoId, createRepoImplConfig(config)));
	}

	public void addRDFSInferRepo(String repoId, SailImplConfig config) {
		manager.addRepositoryConfig(
						new RepositoryConfig(repoId, createRepoImplConfig(new SchemaCachingRDFSInferencerConfig(config))));
	}

	public Repository getRepository(String repoId) {
		return manager.getRepository(repoId);
	}

	private RepositoryImplConfig createRepoImplConfig(SailImplConfig config) {
		return new SailRepositoryConfig(config);
	}
}
