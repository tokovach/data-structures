package com.mytraining.javatraining.graph.vertex;

import java.util.Objects;

public class ValueVertex<L, V> implements Vertex<L, V> {
	private L label;
	private V value;

	ValueVertex(L label, V value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public L getLabel() {
		return label;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Vertex)) {
			return false;
		}
		Vertex vertex = (Vertex) o;
		return Objects.equals(this.label, vertex.getLabel());
	}

	@Override
	public int hashCode() {
		return Objects.hash(label);
	}
}
