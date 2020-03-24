package com.mytraining.javatraining.graph.vertex;

public interface Vertex<L, V> {
	L getLabel();

	V getValue();

	void setValue(V value);

}
