package Algorithms;

import java.util.ArrayList;
import java.util.Random;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {
	
	private static final double CROSSOVER_RATE = 0.7; // Umbral para el emparejamiento 
	private static final int MAX_GENERATIONS = 5; 
	private static final int N = 10; // Initial population size
	private final int bitsPerAttribute = 10; // x, y, z
	private ArrayList<Individuo> population = new ArrayList<Individuo>();
	private Problema problem;

	public GeneticAlgorithm(Problema p) {
		this.problem = p;
		this.population = generatePopulation(GeneticAlgorithm.N);
	}

	@Override
	public Circulo BestSolution(Problema p) {
		Circulo bestCircle = null;
		float fitness;
		float probability[] = new float[N]; /* Probability for each individual to be used: pi = fi / fTotal*/
		float tmpAccumProbability = 0;
		float accumProbability[] = new float[N]; 
		float totalFitness = 0;
		int indexChromosomeA, indexChromosomeB; 
		int newIndividuals = 0;
		
		for (int i = 0; i < MAX_GENERATIONS; i++) {
			
			tmpAccumProbability = 0;
			totalFitness = sumAllFitness();
			ArrayList<Individuo> tmpPopulation = new ArrayList<Individuo>();
			
			for (int j = 0; j < N; j++) {
				fitness = this.population.get(j).getFitness();
				probability[j] = fitness / totalFitness;
				tmpAccumProbability += probability[j];
				accumProbability[j] = tmpAccumProbability;
			}

			newIndividuals = 0;
			
			// Iterar hasta que tengamos N nuevos individuos.
			while (newIndividuals < N) {
							
				indexChromosomeA = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 1: Llamar Función que te devuelve un indice del cromosoma
				indexChromosomeB = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 2: Llamar Función que te devuelve un indice del cromosoma

				if (crossChromosomes()) {
					//validCross(indexChromosomeA, indexChromosomeB)
				}
				else {
					// Mutar los cromosomas
				}
				
				newIndividuals += 1;
			}
			
			this.population = tmpPopulation; // Assign new population after generated it
				
		}
		
		return bestCircle;
	}
	
	/*
	 * Roulette: Select a random individual that fills the condition:
	 */
	public int selectIndividualIndex(float accumProbability[]) {
		boolean found = false;
		int index = 0;
		float randomProbability = Util.random(); // [0, 1)

		while (index < N && !found) {
			if (accumProbability[index] < randomProbability) {
				index++;
			}	
			else {
				found = true;
			}
		}
		
		return index;
	}
	
	public boolean crossChromosomes() {
		float randomProbability = Util.random(); // [0, 1)
		return randomProbability <= 0.7;
	}
	
	// Generating a random population of size N
	//Returns a list of N individuals
	public ArrayList<Individuo> generatePopulation(int n_individuals) {
		
		int dimension = 2^N;
		ArrayList<Individuo> new_population = new ArrayList<Individuo>();
		
		for (int i = 0; i < n_individuals; i++) {
			
			Circulo randomCircle = Circulo.random(dimension);
			Individuo newIndividual = new Individuo(randomCircle);
			newIndividual.setFitness(generateFitness(randomCircle));
			new_population.add(newIndividual);
		}
		
		return new_population;
	}
	
	public float generateFitness(Circulo c) {
		if (this.problem.esSolucion(c)) {
			return (float) c.getRadio();
		} else {
			return 0;
		}
	}
	
	public void printPopulation() {
		String individual = "";
		for (int i = 0; i < N; i++) {
			individual = "Cromosoma: ";
			individual += population.get(i).getCromosoma();
			individual += " | Fitness: ";
			individual += population.get(i).getFitness();
			System.out.println(individual);
		}
	}

	// Sum all the individuals fitness score 
	public float sumAllFitness() {
		float total = 0;
		for (int i = 0; i < N; i++) {
			total += this.population.get(i).getFitness();
		}
		return total;
	}
	
}
