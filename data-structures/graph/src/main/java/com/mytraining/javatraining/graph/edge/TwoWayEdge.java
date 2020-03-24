package com.mytraining.javatraining.graph.edge;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TwoWayEdge<V, E> implements IndirectEdge<V, E> {
	private final Set<V> vertices;
	private E value;

	TwoWayEdge(V firstVertex, V secondVertex, E value) {
		validateVertices(firstVertex, secondVertex);
		vertices = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(firstVertex, secondVertex)));
		this.value = value;
	}

	@Override
	public Set<V> getVertices() {
		return vertices;
	}

	@Override
	public E getValue() {
		return value;
	}

	@Override
	public void setValue(E value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IndirectEdge)) {
			return false;
		}
		IndirectEdge edge = (IndirectEdge) o;
		return this.vertices.equals(edge.getVertices());
	}

	@Override
	public int hashCode() {
		return Objects.hash(vertices);
	}

	private void validateVertices(V firstVertex, V secondVertex) {
		if (Objects.requireNonNull(firstVertex) == Objects.requireNonNull(secondVertex)) {
			throw new IllegalArgumentException();
		}
	}
}
