package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultWeightedEdge > {

		Graph<Fermata, DefaultWeightedEdge> grafo;
		
		public EdgeTraversedGraphListener(Graph<Fermata, DefaultWeightedEdge> grafo) {
			super();
			this.grafo = grafo;
		}
		
		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {			
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
			
			Fermata sourceVertex = grafo.getEdgeSource(ev.getEdge());
			Fermata targetVertex = grafo.getEdgeTarget(ev.getEdge());
		
			// Back codifica relazioni del tipo child->parent
			
			if (!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
				backVisit.put(targetVertex, sourceVertex);
			}
			else if (!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) {
				backVisit.put(sourceVertex, targetVertex);
			}
			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {			
		}
		
	}

	private Graph<Fermata, DefaultWeightedEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	private Map<Fermata, Fermata> backVisit;

	public void creaGrafo() {

		// Crea l'oggetto grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultWeightedEdge.class);

		// Aggiungi i vertici
		MetroDAO dao = new MetroDAO();
		this.fermate = dao.getAllFermate();

		// crea idMap
		this.fermateIdMap = new HashMap<>();
		for (Fermata f : this.fermate)
			fermateIdMap.put(f.getIdFermata(), f);

		Graphs.addAllVertices(this.grafo, this.fermate);

		// Aggiungi gli archi (opzione 1)
		/*
		 * for( Fermata partenza : this.grafo.vertexSet() ) { //
		 * System.out.print(partenza.getIdFermata()+" "); for( Fermata arrivo:
		 * this.grafo.vertexSet() ) {
		 * 
		 * if(dao.esisteConnessione(partenza, arrivo)) { this.grafo.addEdge(partenza,
		 * arrivo) ; }
		 * 
		 * } }
		 */

		// Aggiungi gli archi (opzione 2)

		for (Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);

			for (Fermata arrivo : arrivi)
				this.grafo.addEdge(partenza, arrivo);
		}

		// Aggiungi i pesi agli archi
		
		List<ConnessioneVelocita> archipesati = dao.getConnessioneVelocita();
		for (ConnessioneVelocita cp : archipesati) {
			Fermata partenza = fermateIdMap.get(cp.getStazP());
			Fermata arrivo = fermateIdMap.get(cp.getStazA());
			double distanza = LatLngTool.distance(partenza.getCoords(), arrivo.getCoords(), LengthUnit.KILOMETER);
			double peso = distanza / cp.getVelocita() * 3600;
			grafo.setEdgeWeight(partenza, arrivo, peso);
		}

	}

	public List<Fermata> fermateRaggiungibili(Fermata source) {

		List<Fermata> result = new ArrayList<Fermata>();
		backVisit = new HashMap<>();

		GraphIterator<Fermata, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, source);
//		GraphIterator<Fermata, DefaultWeightedEdge> it = new DepthFirstIterator<>(this.grafo, source) ;

		it.addTraversalListener(new Model.EdgeTraversedGraphListener(grafo));
		backVisit.put(source, null);

		while (it.hasNext()) {
			result.add(it.next());
		}

		return result;

	}

	public List<Fermata> percorsoFinoA(Fermata target) {

		if (!backVisit.containsKey(target)) {
			// il target non � raggiungibile dalla source
			return null;
		}

		List<Fermata> percorso = new LinkedList<Fermata>();

		Fermata f = target;

		while (f != null) {
			percorso.add(0, f);
			f = backVisit.get(f);
		}
		
		return percorso; 

	}

	public Graph<Fermata, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Fermata> getFermate() {
		return fermate;
	}
	
	public List<Fermata> trovaCamminoMinimo (Fermata partenza, Fermata arrivo){
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(this.grafo);
		GraphPath<Fermata, DefaultWeightedEdge> path = dijkstra.getPath(partenza, arrivo);
		return path.getVertexList();
	}

}
