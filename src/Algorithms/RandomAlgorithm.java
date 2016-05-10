package Algorithms;

import main.Circulo;
import main.Problema;
import main.Util;

public class RandomAlgorithm extends Algorithms{
	
	
	
	@Override
	public Circulo BestSolution(Problema p) {
		Circulo solution = new Circulo(0,0,0);
		Circulo actSolution;
		int k, bestRadio = 0;
		
		for(int i = 1; i< Problema.DIMENSION * 2; i++){
			
			int dimensiones = Util.randomInt(1, Problema.DIMENSION);
			actSolution = Circulo.random(dimensiones);
			if(p.esSolucion(actSolution)){
				if(dimensiones > bestRadio){
					solution = actSolution;
					bestRadio = dimensiones;
				}
			}
		}
		
		return solution;
	}

}
