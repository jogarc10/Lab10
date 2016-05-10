package Algorithms;

import main.Circulo;
import main.Problema;

public class BruteForceAlgorithm extends Algorithms{

	@Override
	public Circulo BestSolution(Problema p) {
		Circulo solution = new Circulo(0,0,0);
		Circulo actSolution;
		int k, bestRadio = 0;
		
		for(int i = 1; i< Problema.DIMENSION; i++){
			for(int j = 1; j < Problema.DIMENSION; j++){
				k = 1;
				while (p.esSolucion(actSolution = new Circulo(i,j,k))){
					k++;
				}
				if(k > bestRadio){
					solution = actSolution;
					bestRadio = k;
				}
			}
		}
		
		return solution;
	}

}
