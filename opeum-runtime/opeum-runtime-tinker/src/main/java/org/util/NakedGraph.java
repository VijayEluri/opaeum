package org.util;

import java.util.List;
import java.util.Set;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.blueprints.pgm.TransactionalGraph;
import com.tinkerpop.blueprints.pgm.Vertex;

public interface NakedGraph extends TransactionalGraph, IndexableGraph  {
	void incrementTransactionCount();
	long getTransactionCount();
	Vertex getRoot();
	Vertex addVertex(String className);
	Set<Edge> getEdgesBetween(Vertex v1, Vertex v2, String... labels);
	void addRoot();
	long countVertices();
	long countEdges();
	void registerListeners();
	void createSchema(List<String> className);
}