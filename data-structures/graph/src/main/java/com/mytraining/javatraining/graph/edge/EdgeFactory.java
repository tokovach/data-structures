package com.mytraining.javatraining.graph.edge;

import com.mytraining.javatraining.graph.vertex.Vertex;

public class EdgeFactory {

	private EdgeFactory() {throw new AssertionError();}

	public static <V, E> DirectEdge newDirectEdge(V targetVertex, E value) {
		return new OneWayEdge<>(targetVertex, value);
	}

	public static <V, E> IndirectEdge newIndirectEdge(V firstVertex, V secondVertex, E value) {
		return new TwoWayEdge<>(firstVertex, secondVertex, value);
	}
}
