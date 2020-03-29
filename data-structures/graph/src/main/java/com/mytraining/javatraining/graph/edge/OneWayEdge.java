package com.mytraining.javatraining.graph.edge;

import java.util.Objects;

public class OneWayEdge<L, E> implements DirectEdge<L, E> {
  private final L vertexLabel;
  private final int hashCode;
  private E value;

  OneWayEdge(L targetVertex, E value) {
    this.vertexLabel = targetVertex;
    this.hashCode = Objects.hash(targetVertex);
    this.value = value;
  }

  @Override
  public L getTargetVertex() {
    return vertexLabel;
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
    if (!(o instanceof DirectEdge)) {
      return false;
    }
    DirectEdge edge = (DirectEdge) o;
    return Objects.equals(vertexLabel, edge.getTargetVertex());
  }

  @Override
  public int hashCode() {
    return hashCode;
  }
}
