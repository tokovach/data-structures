package com.mytraining.javatraining.graph.graphs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IndirectGraphTest {
	private Graph<String, Integer> graph;
	private Integer expectedValue = 123;
	private Integer secondExpectedValue = 456;
	private String label = "test1";
	private String secondLabel = "test2";

	@Test
	void addVertex_CheckValue() {
		graph = new IndirectGraph<>();
		assertTrue(graph.addVertex(label, expectedValue));
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void addVertex_VertexDuplicate() {
		graph = new IndirectGraph<>();
		assertTrue(graph.addVertex(label, expectedValue));
		assertFalse(graph.addVertex(label, secondExpectedValue));
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void addVertex_CheckLabel_Null() {
		graph = new IndirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.addVertex(null, expectedValue));
	}

	@Test
	void addVertex_CheckValue_Null() {
		graph = new IndirectGraph<>();
		assertDoesNotThrow(() -> graph.addVertex(label, null));
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckVertices() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.removeVertex(label);
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckNull() {
		graph = new IndirectGraph<>();
		assertDoesNotThrow(() -> graph.removeVertex(label));
	}

	@Test
	void removeVertex_Check_NonExistentVertex() {
		graph = new IndirectGraph<>();
		assertDoesNotThrow(() -> graph.removeVertex(label));
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckNullLabel() {
		graph = new IndirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.removeVertex(null));
	}

	@Test
	void removeVertex_CheckEdges() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		graph.addEdge(secondLabel, label);
		graph.removeVertex(label);
		assertTrue(graph.getNeighbors(secondLabel).isEmpty());
		assertFalse(graph.isAdjacent(secondLabel, label));
		assertFalse(graph.isAdjacent(label, secondLabel));
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void addEdge_NewEdge() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertFalse(graph.addEdge(secondLabel, label));
		assertTrue(graph.isAdjacent(label, secondLabel));
		assertTrue(graph.isAdjacent(secondLabel, label));
		assertTrue(graph.getNeighbors(label).contains(secondLabel));
		assertTrue(graph.getNeighbors(secondLabel).contains(label));
	}

	@Test
	void addEdge_EdgeDuplicate() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertFalse(graph.addEdge(label, secondLabel));
	}

	@Test
	void addEdge_NullEdge() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		assertThrows(NullPointerException.class, () -> graph.addEdge(null, secondLabel));
		assertThrows(NullPointerException.class, () -> graph.addEdge(label, null));
	}

	@Test
	void removeEdge_ExistingEdge() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		graph.removeEdge(label, secondLabel);
		assertFalse(graph.isAdjacent(label, secondLabel));
		assertTrue(graph.getNeighbors(label).isEmpty());
	}

	@Test
	void getVertexValue_CheckValue() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void getVertexValue_Check_MissingLabel() {
		graph = new IndirectGraph<>();
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void getVertexValue_Check_NullLabel() {
		graph = new IndirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.getVertexValue(null));
	}

	@Test
	void setVertexValue_CheckValue() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.setVertexValue(label, secondExpectedValue);
		assertEquals(secondExpectedValue, graph.getVertexValue(label));
	}

	@Test
	void setVertexValue_CheckNullValue() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.setVertexValue(label, null);
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void setVertexValue_CheckNullLabel() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		assertThrows(NullPointerException.class, () -> graph.setVertexValue(null, expectedValue));
	}

	@Test
	void setVertexValue_WrongLabel() {
		graph = new IndirectGraph<>();
		assertDoesNotThrow(() -> graph.setVertexValue(label, expectedValue));
	}

	@Test
	void isAdjacent_Adjacent() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertTrue(graph.isAdjacent(secondLabel, label));
		assertTrue(graph.isAdjacent(label, secondLabel));
	}

	@Test
	void isAdjacent_NullLabel() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertThrows(NullPointerException.class, () -> graph.isAdjacent(null, label));
	}

	@Test
	void getNeighbors_HasNeighbour() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertTrue(graph.getNeighbors(label).contains(secondLabel));
		assertTrue(graph.getNeighbors(secondLabel).contains(label));
	}

	@Test
	void getNeighbors_NoNeighbour() {
		graph = new IndirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		assertTrue(graph.getNeighbors(secondLabel).isEmpty());
	}

	@Test
	void getNeighbors_NoSuchLabel() {
		graph = new IndirectGraph<>();
		assertNull(graph.getNeighbors(secondLabel));
	}

	@Test
	void getNeighbors_NullLabel() {
		graph = new IndirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.getNeighbors(null));
	}

}