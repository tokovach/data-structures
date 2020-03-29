package com.mytraining.javatraining.graph.edge;

import com.mytraining.javatraining.graph.vertex.Vertex;

public class EdgeFactory {

  private EdgeFactory() {
    throw new AssertionError();
  }

  public static <L, E> DirectEdge newDirectEdge(L vertexLabel, E value) {
    return new OneWayEdge<>(vertexLabel, value);
  }

  public static <L, E> IndirectEdge newIndirectEdge(L firstVertex, L secondVertex, E value) {
    return new TwoWayEdge<>(firstVertex, secondVertex, value);
  }
}
