package it.polito.tdp.metroparis.model;

import java.util.Map;

import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

public class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultEdge> {
	
	Map<Fermata, Fermata> back ;


	public EdgeTraversedGraphListener(Map<Fermata, Fermata> back) {
		super();
		this.back = back;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
	
		back.put(e.getEdge().destinationVertex(), e.getEdge().sourceVertex() ); 
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {		
	}

}
