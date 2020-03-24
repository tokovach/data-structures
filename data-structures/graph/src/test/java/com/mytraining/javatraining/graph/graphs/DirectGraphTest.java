package com.mytraining.javatraining.graph.graphs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DirectGraphTest {
	private Graph<String, Integer> graph;
	private Integer expectedValue = 123;
	private Integer secondExpectedValue = 456;
	private String label = "test1";
	private String secondLabel = "test2";

	@Test
	void addVertex_CheckValue() {
		graph = new DirectGraph<>();
		assertTrue(graph.addVertex(label, expectedValue));
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void addVertex_VertexDuplicate() {
		graph = new DirectGraph<>();
		assertTrue(graph.addVertex(label, expectedValue));
		assertFalse(graph.addVertex(label, secondExpectedValue));
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void addVertex_CheckLabel_Null() {
		graph = new DirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.addVertex(null, expectedValue));
	}

	@Test
	void addVertex_CheckValue_Null() {
		graph = new DirectGraph<>();
		assertDoesNotThrow(() -> graph.addVertex(label, null));
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckVertices() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.removeVertex(label);
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckNull() {
		graph = new DirectGraph<>();
		assertDoesNotThrow(() -> graph.removeVertex(label));
	}

	@Test
	void removeVertex_Check_NonExistentVertex() {
		graph = new DirectGraph<>();
		assertDoesNotThrow(() -> graph.removeVertex(label));
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void removeVertex_CheckNullLabel() {
		graph = new DirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.removeVertex(null));
	}

	@Test
	void removeVertex_CheckEdges() {
		graph = new DirectGraph<>();
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
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		graph.addEdge(secondLabel, label);
		assertTrue(graph.isAdjacent(label, secondLabel));
		assertTrue(graph.isAdjacent(secondLabel, label));
		assertTrue(graph.getNeighbors(label).contains(secondLabel));
		assertTrue(graph.getNeighbors(secondLabel).contains(label));
	}

	@Test
	void addEdge_EdgeDuplicate() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertFalse(graph.addEdge(label, secondLabel));
	}

	@Test
	void addEdge_NullEdge() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		assertThrows(NullPointerException.class, () -> graph.addEdge(null, secondLabel));
		assertThrows(NullPointerException.class, () -> graph.addEdge(label, null));
	}

	@Test
	void removeEdge_ExistingEdge() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		graph.removeEdge(label, secondLabel);
		assertFalse(graph.isAdjacent(label, secondLabel));
		assertTrue(graph.getNeighbors(label).isEmpty());
	}

	@Test
	void getVertexValue_CheckValue() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		assertEquals(expectedValue, graph.getVertexValue(label));
	}

	@Test
	void getVertexValue_Check_MissingLabel() {
		graph = new DirectGraph<>();
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void getVertexValue_Check_NullLabel() {
		graph = new DirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.getVertexValue(null));
	}

	@Test
	void setVertexValue_CheckValue() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.setVertexValue(label, secondExpectedValue);
		assertEquals(secondExpectedValue, graph.getVertexValue(label));
	}

	@Test
	void setVertexValue_CheckNullValue() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.setVertexValue(label, null);
		assertNull(graph.getVertexValue(label));
	}

	@Test
	void setVertexValue_CheckNullLabel() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		assertThrows(NullPointerException.class, () -> graph.setVertexValue(null, expectedValue));
	}

	@Test
	void setVertexValue_WrongLabel() {
		graph = new DirectGraph<>();
		assertDoesNotThrow(() -> graph.setVertexValue(label, expectedValue));
	}

	@Test
	void isAdjacent_Adjacent() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertFalse(graph.isAdjacent(secondLabel, label));
		assertTrue(graph.isAdjacent(label, secondLabel));
	}

	@Test
	void isAdjacent_NullLabel() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertThrows(NullPointerException.class, () -> graph.isAdjacent(null, label));
	}

	@Test
	void getNeighbors_HasNeighbour() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertTrue(graph.getNeighbors(label).contains(secondLabel));
	}

	@Test
	void getNeighbors_NoNeighbour() {
		graph = new DirectGraph<>();
		graph.addVertex(label, expectedValue);
		graph.addVertex(secondLabel, secondExpectedValue);
		graph.addEdge(label, secondLabel);
		assertTrue(graph.getNeighbors(secondLabel).isEmpty());
	}

	@Test
	void getNeighbors_NoSuchLabel() {
		graph = new DirectGraph<>();
		assertNull(graph.getNeighbors(secondLabel));
	}

	@Test
	void getNeighbors_NullLabel() {
		graph = new DirectGraph<>();
		assertThrows(NullPointerException.class, () -> graph.getNeighbors(null));
	}
}