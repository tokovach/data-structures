package com.mytraining.javatraining.graph.vertex;

import java.util.Comparator;
import java.util.Objects;

public class ValueVertex<L, V> implements Vertex<L, V>, Comparable<ValueVertex<L, V>> {
  private final Comparator<ValueVertex<L, V>> COMPARATOR =
      Comparator.comparingInt((ValueVertex<L, V> vertex) -> vertex.getLabel().hashCode())
          .thenComparingInt(vertex -> vertex.getValue().hashCode());
  private final L label;
  private final int hashCode;
  private V value;

  ValueVertex(L label, V value) {
    this.label = label;
    this.hashCode = Objects.hash(label);
    this.value = value;
  }

  @Override
  public L getLabel() {
    return label;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public void setValue(V value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Vertex)) {
      return false;
    }
    Vertex vertex = (Vertex) o;
    return Objects.equals(this.label, vertex.getLabel());
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public int compareTo(ValueVertex<L, V> vertex) {
    return COMPARATOR.compare(this, vertex);
  }
}
