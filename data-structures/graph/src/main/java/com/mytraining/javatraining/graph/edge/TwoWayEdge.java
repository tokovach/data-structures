package com.mytraining.javatraining.graph.edge;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TwoWayEdge<L, E> implements IndirectEdge<L, E> {
  private final Set<L> vertices;
  private final int hashCode;
  private E value;

  TwoWayEdge(L firstVertexLabel, L secondVertexLabel, E value) {
    validateVertices(firstVertexLabel, secondVertexLabel);
    this.vertices =
        Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(firstVertexLabel, secondVertexLabel)));
    this.hashCode = Objects.hash(vertices);
    this.value = value;
  }

  @Override
  public Set<L> getVertices() {
    return vertices;
  }

  @Override
  public E getValue() {
    return value;
  }

  @Override
  public void setValue(E value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof IndirectEdge)) {
      return false;
    }
    IndirectEdge<L, E> edge = (IndirectEdge<L, E>) o;
    return vertices.equals(edge.getVertices());
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  private void validateVertices(L firstVertexLabel, L secondVertexLabel) {
    if (Objects.requireNonNull(firstVertexLabel) == Objects.requireNonNull(secondVertexLabel)) {
      throw new IllegalArgumentException();
    }
  }
}
