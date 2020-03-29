package com.mytraining.javatraining.graph.graphs;

import static com.mytraining.javatraining.graph.edge.EdgeFactory.newDirectEdge;
import static com.mytraining.javatraining.graph.vertex.VertexFactory.newVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.mytraining.javatraining.graph.edge.DirectEdge;
import com.mytraining.javatraining.graph.vertex.Vertex;

public class DirectValueGraph<L, V, E> implements ValueGraph<L, V, E> {
  private HashMap<L, Vertex<L, V>> vertices;
  private HashMap<L, HashSet<DirectEdge<L, E>>> edges;

  public DirectValueGraph() {
    this.vertices = new HashMap<>();
    this.edges = new HashMap<>();
  }

  @Override
  public boolean addVertex(L vertexLabel, V vertexValue) {
    return vertices.putIfAbsent(
                Objects.requireNonNull(vertexLabel), newVertex(vertexLabel, vertexValue))
            == null
        && edges.putIfAbsent(vertexLabel, new HashSet<>()) == null;
  }

  @Override
  public void removeVertex(L vertexLabel) {
    if (isVertexValid(Objects.requireNonNull(vertexLabel))) {
      edges.remove(vertexLabel);
      removeVertexFromEdges(vertexLabel);
      vertices.remove(vertexLabel);
    }
  }

  @Override
  public boolean addEdge(L startingVertexLabel, L targetVertexLabel, E edgeValue) {
    if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
      return false;
    }
    return edges.get(startingVertexLabel).add(newDirectEdge(targetVertexLabel, edgeValue));
  }

  @Override
  public void removeEdge(L startingVertexLabel, L targetVertexLabel) {
    if (!isEdgePossible(startingVertexLabel, targetVertexLabel)) {
      return;
    }
    removeEdgeByLabel(edges.get(startingVertexLabel), targetVertexLabel);
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
  public E getEdgeValue(L firstVertexLabel, L secondVertexLabel) {
    DirectEdge<L, E> edge = getEdge(firstVertexLabel, secondVertexLabel);
    if (edge != null) {
      return edge.getValue();
    }
    return null;
  }

  @Override
  public void setEdgeValue(L firstVertexLabel, L secondVertexLabel, E value) {}

  @Override
  public boolean isAdjacent(L firstVertexLabel, L secondVertexLabel) {
    if (!isEdgePossible(firstVertexLabel, secondVertexLabel)) {
      return false;
    }
    return getEdge(firstVertexLabel, secondVertexLabel) != null;
  }

  @Override
  public Set<L> getNeighbors(L vertexLabel) {
    if (edges.containsKey(vertexLabel)) {
      Set<L> vertexLabels = new HashSet<>();
      for (DirectEdge<L, E> e : edges.get(Objects.requireNonNull(vertexLabel))) {
        vertexLabels.add(e.getTargetVertex());
      }
      return vertexLabels;
    }
    return null;
  }

  private boolean isVertexValid(L vertexLabel) {
    return edges.containsKey(vertexLabel) && vertices.containsKey(vertexLabel);
  }

  private void removeVertexFromEdges(L removableVertexLabel) {
    for (HashSet<DirectEdge<L, E>> edges : edges.values()) {
      removeEdgeByLabel(edges, removableVertexLabel);
    }
  }

  private boolean isEdgePossible(L startingVertexLabel, L targetVertexLabel) {
    if (!isVertexValid(Objects.requireNonNull(startingVertexLabel))
        || !isVertexValid(Objects.requireNonNull(targetVertexLabel))) {
      return false;
    }
    return startingVertexLabel != targetVertexLabel;
  }

  private DirectEdge<L, E> getEdge(L startingVertexLabel, L targetVertexLabel) {
    for (DirectEdge<L, E> edge : edges.get(startingVertexLabel)) {
      if (edge.getTargetVertex().equals(targetVertexLabel)) {
        return edge;
      }
    }
    return null;
  }

  private void removeEdgeByLabel(HashSet<DirectEdge<L, E>> edges, L removableVertexLabel) {
    edges.removeIf(edge -> edge.getTargetVertex().equals(removableVertexLabel));
  }
}
