package com.mytraining.javatraining.graph.edge;

public interface DirectEdge<V, E> {

	V getTargetVertex();

	E getValue();

	void setValue(E value);
}
