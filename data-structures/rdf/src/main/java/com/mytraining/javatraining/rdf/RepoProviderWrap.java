package com.mytraining.javatraining.rdf;

import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryProvider;

public class RepoProviderWrap {

	public static RemoteRepositoryManager getRemoteRepoMgr(String url) {
		return (RemoteRepositoryManager) RepositoryProvider.getRepositoryManager(url);
	}
}
