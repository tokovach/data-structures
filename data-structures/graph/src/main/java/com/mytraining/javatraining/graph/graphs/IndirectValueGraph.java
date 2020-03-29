package com.mytraining.javatraining.graph.graphs;

import static com.mytraining.javatraining.graph.vertex.VertexFactory.newVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.mytraining.javatraining.graph.edge.IndirectEdge;
import com.mytraining.javatraining.graph.vertex.Vertex;

public class IndirectValueGraph<L, V, E> implements ValueGraph<L, V, E> {
  private HashMap<L, Vertex<L, V>> vertices;
  private HashSet<IndirectEdge<L, E>> edges;

  public IndirectValueGraph() {
    this.vertices = new HashMap<>();
    this.edges = new HashSet<>();
  }

  @Override
  public boolean addVertex(L vertexLabel, V vertexValue) {
    return vertices.putIfAbsent(
            Objects.requireNonNull(vertexLabel), newVertex(vertexLabel, vertexValue))
        == null;
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
  public boolean addEdge(L startingVertexLabel, L targetVertexLabel, E edgeValue) {
    if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
      return false;
    }
    return edges.get(startingVertexLabel).add(targetVertexLabel)
        && edges.get(targetVertexLabel).add(startingVertexLabel);
  }

  @Override
  public void removeEdge(L startingVertexLabel, L targetVertexLabel) {
    if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
      return;
    }
    edges.get(startingVertexLabel).remove(targetVertexLabel);
    edges.get(targetVertexLabel).remove(startingVertexLabel);
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

  @Override
  public E getEdgeValue(L firstVertexLabel, L secondVertexLabel) {
    return null;
  }

  @Override
  public void setEdgeValue(L firstVertexLabel, L secondVertexLabel, E value) {}

  private boolean isEdgePossible(L startingVertexLabel, L targetVertexLabel) {
    if (!isVertexValid(Objects.requireNonNull(startingVertexLabel))
        || !isVertexValid(Objects.requireNonNull(targetVertexLabel))) {
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
