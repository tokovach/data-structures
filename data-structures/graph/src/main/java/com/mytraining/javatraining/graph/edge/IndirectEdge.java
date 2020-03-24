package com.mytraining.javatraining.graph.edge;

import java.util.Set;

public interface IndirectEdge<V,E> {

	Set<V> getVertices();

	E getValue();

	void setValue(E value);

}
