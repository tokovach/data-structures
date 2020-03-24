package com.mytraining.javatraining.graph.graphs;

import java.util.Set;

public interface Graph<L, V> {
	boolean addVertex(L vertexLabel, V vertexValue);

	void removeVertex(L vertexLabel);

	boolean addEdge(L startingVertexLabel, L targetVertexLabel);

	void removeEdge(L startingVertexLabel, L targetVertexLabel);

	V getVertexValue(L vertexLabel);

	void setVertexValue(L vertexLabel, V value);

	boolean isAdjacent(L firstVertexLabel, L secondVertexLabel);

	Set<L> getNeighbors(L vertexLabel);
}
