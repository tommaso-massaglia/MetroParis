package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> grafo ;
	private List<Fermata> fermate ;
	private Map<Integer, Fermata> fermateIdMap ;
	
	public void creaGrafo() {
		
		// Crea l'oggetto grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class) ;
		
		// Aggiungi i vertici
		MetroDAO dao = new MetroDAO() ;
		this.fermate = dao.getAllFermate() ;
		
		// crea idMap
		this.fermateIdMap = new HashMap<>() ;
		for(Fermata f: this.fermate)
			fermateIdMap.put(f.getIdFermata(), f) ;
		
		Graphs.addAllVertices(this.grafo, this.fermate) ;
		
		// Aggiungi gli archi (opzione 1)
		/*
		for( Fermata partenza : this.grafo.vertexSet() ) {
//			System.out.print(partenza.getIdFermata()+" ");
			for( Fermata arrivo: this.grafo.vertexSet() ) {
				
				if(dao.esisteConnessione(partenza, arrivo)) {
					this.grafo.addEdge(partenza, arrivo) ;
				}
				
			}
		}
		*/
		
		
		// Aggiungi gli archi (opzione 2)
		
		for( Fermata partenza : this.grafo.vertexSet() ) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap) ;
			
			for(Fermata arrivo: arrivi) 
				this.grafo.addEdge(partenza, arrivo) ;
		}
		
		
		// Aggiungi gli archi (opzione 3)


		
		
	}

	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Fermata> getFermate() {
		return fermate;
	}

}
