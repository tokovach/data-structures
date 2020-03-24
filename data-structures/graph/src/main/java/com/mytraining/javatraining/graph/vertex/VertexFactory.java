package com.mytraining.javatraining.graph.vertex;

public class VertexFactory {

	private VertexFactory() {throw new AssertionError();}

	public static <L, V> Vertex newVertex(L label, V value) {
		return new ValueVertex<>(label, value);
	}
}
