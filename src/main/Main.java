package main;
import java.io.FileNotFoundException;

import Algorithms.BruteForceAlgorithm;
import Algorithms.GeneticAlgorithm;
import Algorithms.RandomAlgorithm;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		int N_CASES = 4;
		Circulo bestCircle;
	
		for (int i = 1; i <= N_CASES; i++) {
			String filename = "case00" + i;
			Problema p = new Problema(filename + ".txt");
			
			System.err.println("New Set of problems!!");
			
			System.out.println("Random Algorithm");

			RandomAlgorithm random = new RandomAlgorithm();
			bestCircle = random.BestSolution(p);
			System.out.println(bestCircle.toString());
			//genetic.printPopulation();

			System.out.println("Brute force Algorithm");
			
			BruteForceAlgorithm brute = new BruteForceAlgorithm();
			bestCircle = brute.BestSolution(p);
			System.out.println(bestCircle.toString());
			//genetic.printPopulation();

			System.out.println("Genetic Algorithm");
			
			GeneticAlgorithm genetic = new GeneticAlgorithm(p);
			bestCircle = genetic.BestSolution(p);
			System.out.println(bestCircle.toString());
			//genetic.printPopulation();
			
			System.out.println("-------");
		}	
		
		

		
		
		
//		Circulo bruteCircle, randomCircle, geneticCircle;
//		int populationSize = 10;
//
//		GeneticAlgorithm genetic = new GeneticAlgorithm(p);
//		genetic.printPopulation();
//		
//		System.out.println("------------------------");
//		
//		geneticCircle = genetic.BestSolution(p);		
//		genetic.printPopulation();
//		
//		System.out.println("");
//		System.out.println("Best circle: " + geneticCircle.toString());
//		
//		
	}

}
