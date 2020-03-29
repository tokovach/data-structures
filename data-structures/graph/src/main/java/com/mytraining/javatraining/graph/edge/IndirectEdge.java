package com.mytraining.javatraining.graph.edge;

import java.util.Set;

public interface IndirectEdge<L,E> {

	Set<L> getVertices();

	E getValue();

	void setValue(E value);

}
