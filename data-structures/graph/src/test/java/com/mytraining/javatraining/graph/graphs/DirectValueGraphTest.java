package com.mytraining.javatraining.graph.graphs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DirectValueGraphTest {
  private ValueGraph<String, Integer, String> graph;
  private Integer expectedValue = 123;
  private Integer secondExpectedValue = 456;
  private String label = "test1";
  private String secondLabel = "test2";
  private String edgeValue = "yes";
  private String secondEdgeValue = "no";

  @Test
  void addVertex_CheckValue() {
    graph = new DirectValueGraph<>();
    assertTrue(graph.addVertex(label, expectedValue));
    assertEquals(expectedValue, graph.getVertexValue(label));
  }

  @Test
  void addVertex_VertexDuplicate() {
    graph = new DirectValueGraph<>();
    assertTrue(graph.addVertex(label, expectedValue));
    assertFalse(graph.addVertex(label, secondExpectedValue));
    assertEquals(expectedValue, graph.getVertexValue(label));
  }

  @Test
  void addVertex_CheckLabel_Null() {
    graph = new DirectValueGraph<>();
    assertThrows(NullPointerException.class, () -> graph.addVertex(null, expectedValue));
  }

  @Test
  void addVertex_CheckValue_Null() {
    graph = new DirectValueGraph<>();
    assertDoesNotThrow(() -> graph.addVertex(label, null));
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void removeVertex_CheckVertices() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.removeVertex(label);
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void removeVertex_CheckNull() {
    graph = new DirectValueGraph<>();
    assertDoesNotThrow(() -> graph.removeVertex(label));
  }

  @Test
  void removeVertex_Check_NonExistentVertex() {
    graph = new DirectValueGraph<>();
    assertDoesNotThrow(() -> graph.removeVertex(label));
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void removeVertex_CheckNullLabel() {
    graph = new DirectValueGraph<>();
    assertThrows(NullPointerException.class, () -> graph.removeVertex(null));
  }

  @Test
  void removeVertex_CheckEdges() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    graph.addEdge(secondLabel, label, secondEdgeValue);
    graph.removeVertex(label);
    assertTrue(graph.getNeighbors(secondLabel).isEmpty());
    assertFalse(graph.isAdjacent(secondLabel, label));
    assertFalse(graph.isAdjacent(label, secondLabel));
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void addEdge_NewEdge() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    graph.addEdge(secondLabel, label, secondEdgeValue);
    assertTrue(graph.isAdjacent(label, secondLabel));
    assertTrue(graph.isAdjacent(secondLabel, label));
    assertTrue(graph.getNeighbors(label).contains(secondLabel));
    assertTrue(graph.getNeighbors(secondLabel).contains(label));
  }

  @Test
  void addEdge_EdgeDuplicate() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    assertFalse(graph.addEdge(label, secondLabel, edgeValue));
  }

  @Test
  void addEdge_NullEdge() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    assertThrows(NullPointerException.class, () -> graph.addEdge(null, secondLabel, edgeValue));
    assertThrows(NullPointerException.class, () -> graph.addEdge(label, null, edgeValue));
  }

  @Test
  void removeEdge_ExistingEdge() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    graph.removeEdge(label, secondLabel);
    assertFalse(graph.isAdjacent(label, secondLabel));
    assertTrue(graph.getNeighbors(label).isEmpty());
  }

  @Test
  void getVertexValue_CheckValue() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    assertEquals(expectedValue, graph.getVertexValue(label));
  }

  @Test
  void getVertexValue_Check_MissingLabel() {
    graph = new DirectValueGraph<>();
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void getVertexValue_Check_NullLabel() {
    graph = new DirectValueGraph<>();
    assertThrows(NullPointerException.class, () -> graph.getVertexValue(null));
  }

  @Test
  void setVertexValue_CheckValue() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.setVertexValue(label, secondExpectedValue);
    assertEquals(secondExpectedValue, graph.getVertexValue(label));
  }

  @Test
  void setVertexValue_CheckNullValue() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.setVertexValue(label, null);
    assertNull(graph.getVertexValue(label));
  }

  @Test
  void setVertexValue_CheckNullLabel() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    assertThrows(NullPointerException.class, () -> graph.setVertexValue(null, expectedValue));
  }

  @Test
  void setVertexValue_WrongLabel() {
    graph = new DirectValueGraph<>();
    assertDoesNotThrow(() -> graph.setVertexValue(label, expectedValue));
  }

  @Test
  void isAdjacent_Adjacent() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    assertFalse(graph.isAdjacent(secondLabel, label));
    assertTrue(graph.isAdjacent(label, secondLabel));
  }

  @Test
  void isAdjacent_NullLabel() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    assertThrows(NullPointerException.class, () -> graph.isAdjacent(null, label));
  }

  @Test
  void getNeighbors_HasNeighbour() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    assertTrue(graph.getNeighbors(label).contains(secondLabel));
  }

  @Test
  void getNeighbors_NoNeighbour() {
    graph = new DirectValueGraph<>();
    graph.addVertex(label, expectedValue);
    graph.addVertex(secondLabel, secondExpectedValue);
    graph.addEdge(label, secondLabel, edgeValue);
    assertTrue(graph.getNeighbors(secondLabel).isEmpty());
  }

  @Test
  void getNeighbors_NoSuchLabel() {
    graph = new DirectValueGraph<>();
    assertNull(graph.getNeighbors(secondLabel));
  }

  @Test
  void getNeighbors_NullLabel() {
    graph = new DirectValueGraph<>();
    assertThrows(NullPointerException.class, () -> graph.getNeighbors(null));
  }
}
