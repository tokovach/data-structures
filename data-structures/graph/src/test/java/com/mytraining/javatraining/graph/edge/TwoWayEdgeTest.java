package com.mytraining.javatraining.graph.edge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TwoWayEdgeTest {
	TwoWayEdge<Integer, String> edge = new TwoWayEdge<>(1, 2, "hui");
	TwoWayEdge<Integer, String> edge1 = new TwoWayEdge<>(2, 1, "hui");

	@Test
	void equal_SameValues(){
		assertEquals(edge.hashCode(),edge1.hashCode());
		assertEquals(edge, edge1);
	}
}