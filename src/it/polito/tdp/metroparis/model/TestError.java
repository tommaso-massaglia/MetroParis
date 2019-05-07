package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class TestError {

	public static void main(String args[]) {
		
		Graph<Fermata, DefaultEdge> g =
				new SimpleDirectedGraph<>(DefaultEdge.class) ;
		
		Fermata f1 = new Fermata(1, "F1", null) ;
		Fermata f2 = new Fermata(2, "F2", null) ;
		Fermata f3 = new Fermata(3, "F3", null) ;
		
		g.addVertex(f1) ;
		g.addVertex(f2) ;
		g.addVertex(f3) ;
		
		System.out.println(g) ;
		
		Fermata f1n = new Fermata(1, null, null) ;
		Fermata f2n = new Fermata(2, null, null) ;
		Fermata f3n = new Fermata(3, null, null) ;
		
		g.addVertex(f1n) ;
		
		System.out.println(g) ;
		
		g.addEdge(f1n, f2n) ;

		System.out.println(g) ;
		
		List<Fermata> n1 = Graphs.neighborListOf(g, f1) ;
		
		System.out.println(n1) ;

		
	}
}
