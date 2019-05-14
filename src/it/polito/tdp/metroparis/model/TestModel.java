package it.polito.tdp.metroparis.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model() ;
		
		m.creaGrafo();
		
//		System.out.println(m.getGrafo()) ;
		System.out.format("Creati %d vertici e %d archi\n", m.getGrafo().vertexSet().size(),
				m.getGrafo().edgeSet().size()) ;
		
		Fermata source = m.getFermate().get(0) ;
		System.out.println("Parto da: "+source) ;
		List<Fermata> raggiungibili = m.fermateRaggiungibili(source) ;
		System.out.println("Fermate raggiunte: "+raggiungibili + " ("+raggiungibili.size()+")") ;

		Fermata target = m.getFermate().get(150);
		List <Fermata> percorso = m.percorsoFinoA(target);
		System.out.println("Percorso: ") ;
		System.out.println(percorso);
		
		System.out.println(m.trovaCamminoMinimo(source, target));
		
	}

}
