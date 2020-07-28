package com.mytraining.javatraining.rdf;

import java.io.File;
import java.util.Optional;

import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.sail.inferencer.fc.CustomGraphQueryInferencer;
import org.eclipse.rdf4j.sail.inferencer.fc.SchemaCachingRDFSInferencer;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class RepoWrapFactory {

	public static SailRepoWrap createMainMemorySailRepoWrap(Optional<File> file) {
		return file.map(value -> new SailRepoWrap(new MemoryStore(value)))
						.orElseGet(() -> new SailRepoWrap(new MemoryStore()));
	}

	public static SailRepoWrap createRDFSInferredMainMemorySailRepoWrap(Optional<File> file) {
		return file.map(value -> new SailRepoWrap(new SchemaCachingRDFSInferencer(new MemoryStore(value))))
						.orElseGet(() -> new SailRepoWrap(new SchemaCachingRDFSInferencer(new MemoryStore())));
	}

	public static SailRepoWrap createCustomInferMainMemorySailRepoWrap(Optional<File> file, String rule,
					String match) {
		return file.map(value -> new SailRepoWrap(
						new CustomGraphQueryInferencer(new MemoryStore(value), QueryLanguage.SPARQL, rule, match)))
						.orElseGet(() -> new SailRepoWrap(
										new CustomGraphQueryInferencer(new MemoryStore(), QueryLanguage.SPARQL, rule, match)));
	}

	/**
	 * @param file  is the physical storage of the native store.
	 * @param index it is the order of triples i.e "spoc","posc"
	 * @return
	 */
	public static SailRepoWrap createNativeSailRepoWrap(File file, Optional<String> index) {
		return index.map(value -> new SailRepoWrap(new NativeStore(file, value)))
						.orElseGet(() -> new SailRepoWrap(new NativeStore(file)));
	}

	public static SailRepoWrap createRDFSInferredNativeSailRepoWrap(File file, Optional<String> index) {
		return index.map(value -> new SailRepoWrap(new SchemaCachingRDFSInferencer(new NativeStore(file, value))))
						.orElseGet(() -> new SailRepoWrap(new SchemaCachingRDFSInferencer(new NativeStore(file))));
	}

	public static SailRepoWrap createCustomInferNativeSailRepoWrap(File file, Optional<String> index, String rule,
					String match) {
		return index.map(value -> new SailRepoWrap(new CustomGraphQueryInferencer(new NativeStore(file, value),
						QueryLanguage.SPARQL, rule, match)))
						.orElseGet(() -> new SailRepoWrap(new CustomGraphQueryInferencer(new NativeStore(file),
										QueryLanguage.SPARQL, rule, match)));
	}
}
