package com.mytraining.javatraining.graph.edge;

import java.util.Objects;

public class OneWayEdge<V, E> implements DirectEdge<V, E> {
	private final V targetVertex;
	private E value;

	OneWayEdge(V targetVertex, E value) {
		this.targetVertex = targetVertex;
		this.value = value;
	}

	@Override
	public V getTargetVertex() {
		return targetVertex;
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
		return Objects.equals(targetVertex, edge.getTargetVertex());
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetVertex);
	}
}
