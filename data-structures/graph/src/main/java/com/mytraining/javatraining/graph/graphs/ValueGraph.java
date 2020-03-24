package com.mytraining.javatraining.graph.graphs;

import java.util.List;
import java.util.Set;

public interface ValueGraph<L, V, E> {
	boolean addVertex(L vertexLabel, V vertexValue);

	void removeVertex(L vertexLabel);

	boolean addEdge(L startingVertexLabel, L targetVertexLabel);

	void removeEdge(L startingVertexLabel, L targetVertexLabel);

	V getVertexValue(L vertexLabel);

	void setVertexValue(L vertexLabel, V value);

	boolean isAdjacent(L firstVertexLabel, L secondVertexLabel);

	Set<L> getNeighbors(L vertexLabel);

	E getEdgeValue(L firstVertexLabel, L secondVertexLabel);

	void setEdgeValue(L firstVertexLabel, L secondVertexLabel, E value);
}
