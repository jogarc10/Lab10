package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {

	private static final int ELITE_SIZE = 4;
	private static final int GENES_NUMBER = 3;  // Total number of genes
	private static final int GEN_DIMENSION = 10; // Dimension for each gene
	private static final int MAX_GENERATIONS = 100;
	private static final int POPULATION_SIZE = 10; // Initial population size
	private PriorityQueue<Individuo> population = new PriorityQueue<Individuo>();
	private float[] populationProbability = new float[POPULATION_SIZE];
	private Problema problem;
	private float totalFitness;

	public GeneticAlgorithm(Problema p) {
		this.problem = p;
		this.population = generateInitialPopulation();
	}

	/*
	 * Generating a random population of size N
	 * Returns a priority queue of N individuals
	 */
	private PriorityQueue<Individuo> generateInitialPopulation() {
		PriorityQueue<Individuo> new_population = new PriorityQueue<Individuo>();
		int dimension = (int) Math.pow(2, GEN_DIMENSION); // maximum radio

		for (int i = 0; i < POPULATION_SIZE; i++) {
			Circulo randomCircle = Circulo.random(dimension);
			Individuo newIndividual = new Individuo(randomCircle);
			newIndividual.setFitness(generateFitness(randomCircle));
			new_population.add(newIndividual);
			// TODO: Update probability.
		}

		return new_population;
	}

	@Override
	public Circulo BestSolution(Problema p) {
		Circulo bestCircle = new Circulo(0, 0, 0);
		Individuo bestGenerationIndividual;

		for (int i = 0; i < MAX_GENERATIONS; i++) {
			generateAndUpdateNewPopulation(); // Assign new population after generated it
			// TODO: Cada nueva generacion coger el mejor circulo
			// Si el circulo es mejor que el encontrado hasta la fecha, actualizarlo.
			bestGenerationIndividual = this.population.peek();
		}

		return bestCircle;
	}

	public void generateAndUpdateNewPopulation() {
		int createdChromosomes = 0;
		PriorityQueue<Individuo> newPopulation = new PriorityQueue<Individuo>();
		float[] accumProbability = new float[POPULATION_SIZE];
		float totalFitness;
		Pair newPair;
		
		this.totalFitness = sumAllFitness();
//		updateProbabilities(); // TODO

		// Merge population
		while (newPopulation.size() < POPULATION_SIZE - ELITE_SIZE) {
			
			newPair = routlettePickCouple();
			
			newPair.cross();
			newPair.mutate();
			newPair.calculateFitness();
			newPopulation.addAll(newPair.toList());
		}

		// Get elite from old population and add it to new population
		Individuo topEliteElement;
		for (int i = 0; i < ELITE_SIZE; i++) {
			if (population.size() > 0) {
				topEliteElement = population.poll();
				newPopulation.add(topEliteElement);
			}
		}

		this.population = newPopulation;
	}
	
	/*
	 *  Sum all the individuals fitness score
	 */
	public float sumAllFitness() {
		float total = 0;

		for (Individuo e : population) {
			total += e.getFitness();
		}

		return total;
	}

	/*
	 * Calculate Probability for each individual to be used: pi = fi / fTotal
	 * and returns 2 Individuals
	 */
	
	public Pair routlettePickCouple() {
		Individuo[] pickedCouple = new Individuo[2]; 
		float tmpAccumProbability = 0;
		float fitness;
		Object[] individualsArray = this.population.toArray();
		
		boolean found = false;
		
		Individuo tmpIndividual;
		
		// Calculate probabilities
		for (int i = 0; i < individualsArray.length; i++) {
			tmpIndividual = (Individuo) individualsArray[i];
			fitness = tmpIndividual.getFitness();
			tmpAccumProbability += fitness / this.totalFitness;
			this.populationProbability[i] = tmpAccumProbability;
			
			System.out.println("Individual" + i);
			System.out.println(tmpIndividual);
			
		}
		
		System.out.println("----");
		System.out.println("Picking elements...");
		
		// Routlete.
		int index;
		float randomProbability; // [0, 1)
		
		for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
			randomProbability = Util.random();
			index = 0;
			while (index < POPULATION_SIZE && !found) {
				if (this.populationProbability[index] < randomProbability) {
					index++;
				}
				else {
					found = true;
				}
			}
			pickedCouple[pairIndex] = (Individuo) individualsArray[index];
		}
		
		System.out.println("YOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		System.out.println("-----------------------------------------------");
		
		return new Pair(pickedCouple[0], pickedCouple[1]);
	}

	private float generateFitness(Circulo c) {
		if (this.problem.esSolucion(c)) {
			return (float) c.getRadio();
		} else {
			return 0;
		}
	}

	public void printPopulation() {
		String individual = "";

		for (Individuo e : population) {
			individual = "Cromosoma: ";
			individual += e.getCromosoma();
			individual += " | Fitness: ";
			individual += e.getFitness();
			System.out.println(individual);
		}
	}

}
