package com.mytraining.javatraining.graph.graphs;

import static com.mytraining.javatraining.graph.vertex.VertexFactory.newVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.mytraining.javatraining.graph.vertex.Vertex;

public class DirectGraph<L, V> implements Graph<L, V> {
	private HashMap<L, Vertex<L, V>> vertices;
	private HashMap<L, HashSet<L>> edges;

	public DirectGraph() {
		this.vertices = new HashMap<>();
		this.edges = new HashMap<>();
	}

	@Override
	public boolean addVertex(L vertexLabel, V vertexValue) {
		return vertices.putIfAbsent(Objects.requireNonNull(vertexLabel), newVertex(vertexLabel, vertexValue)) == null &&
						edges.putIfAbsent(vertexLabel, new HashSet<>()) == null;
	}

	@Override
	public void removeVertex(L vertexLabel) {
		if (isVertexValid(Objects.requireNonNull(vertexLabel))) {
			edges.remove(vertexLabel);
			removeEdges(vertexLabel);
			vertices.remove(vertexLabel);
		}
	}

	@Override
	public boolean addEdge(L startingVertexLabel, L targetVertexLabel) {
		if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
			return false;
		}
		return edges.get(startingVertexLabel).add(targetVertexLabel);
	}

	@Override
	public void removeEdge(L startingVertexLabel, L targetVertexLabel) {
		if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
			return;
		}
		edges.get(startingVertexLabel).remove(targetVertexLabel);
	}

	@Override
	public V getVertexValue(L vertexLabel) {
		if (vertices.containsKey(Objects.requireNonNull(vertexLabel))) {
			return vertices.get(vertexLabel).getValue();
		}
		return null;
	}

	@Override
	public void setVertexValue(L vertexLabel, V value) {
		if (vertices.containsKey(Objects.requireNonNull(vertexLabel))) {
			vertices.get(vertexLabel).setValue(value);
		}
	}

	@Override
	public boolean isAdjacent(L firstVertexLabel, L secondVertexLabel) {
		if (!isEdgePossible(firstVertexLabel, secondVertexLabel)) {
			return false;
		}
		return edges.get(firstVertexLabel).contains(secondVertexLabel);
	}

	@Override
	public Set<L> getNeighbors(L vertexLabel) {
		return edges.get(Objects.requireNonNull(vertexLabel));
	}

	private boolean isEdgePossible(L startingVertexLabel, L targetVertexLabel) {
		if (!isVertexValid(Objects.requireNonNull(startingVertexLabel)) || !isVertexValid(
						Objects.requireNonNull(targetVertexLabel))) {
			return false;
		}
		return startingVertexLabel != targetVertexLabel;
	}

	private boolean isVertexValid(L vertexLabel) {
		return edges.containsKey(vertexLabel) && vertices.containsKey(vertexLabel);
	}

	private void removeEdges(L removableVertexLabel) {
		for (HashSet<L> edges : edges.values()) {
			edges.remove(removableVertexLabel);
		}
	}
}
