package com.mytraining.javatraining.graph.edge;

public interface DirectEdge<L, E> {

	L getTargetVertex();

	E getValue();

	void setValue(E value);
}
