package Algorithms;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.PriorityQueue;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {
	
	private static final int GENES_NUMBER = 3;  // Total number of genes
	private static final int GEN_DIMENSION = 10; // Dimension for each gene
	private static final int MAX_GENERATIONS = 5; 
	private static final int POPULATION_SIZE = 10; // Initial population size
	private PriorityQueue<Individuo> population = new PriorityQueue<Individuo>();
	private Problema problem;

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
		float totalFitness = sumAllFitness();
		
		while (newPopulation.size() < POPULATION_SIZE) {
			
			accumProbability = calculateAccumProbability(totalFitness); 
			Pair newPair = pickCouples(accumProbability); // returns list of size 2 chromosomes to work with them
			
			newPair.cross();  
			newPair.mutate();
			
			newPopulation.addAll(newPair.toList());
			// Calculate Fitness
		}
		
		this.population = newPopulation;
	}
	
	
	
	private Pair pickCouples(float[] accumProbability) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Calculate Probability for each individual to be used: pi = fi / fTotal
	 */
	public float[] calculateAccumProbability(float totalFitness) {
		float accumProbability[] = new float[POPULATION_SIZE];
		float tmpAccumProbability;
		float fitness;
		
		tmpAccumProbability = 0;
		
		for (int j = 0; j < POPULATION_SIZE; j++) {
			fitness = this.population.get(j).getFitness();
			tmpAccumProbability += fitness / totalFitness;
			accumProbability[j] = tmpAccumProbability;
		}
		
		return accumProbability;
	}
	
	/*
	 * Roulette: Select a random individual that fills the condition:
	 */
	public int selectIndividualIndex(float accumProbability[]) {
		int index = 0;
		boolean found = false;
		float randomProbability = Util.random(); // [0, 1)

		while (index < POPULATION_SIZE && !found) {
			if (accumProbability[index] < randomProbability) {
				index++;
			}	
			else {
				found = true;
			}
		}
		
		return index;
	}
	
	/*
	 * Cross 2 chromosomes and return a new indivudal
	 */
	public Individuo generateCrossedIndividual(int indexChromoA, int indexChromoB) {
		int crossPoint;
		float tmpFitness;
		Circulo tmpCircle;
		String tmpCromosome, chromosomeA, chromosomeB;
	
		crossPoint = crossPoint();
		
		chromosomeA = this.population.get(indexChromoA).getCromosoma();
		chromosomeB = this.population.get(indexChromoB).getCromosoma();

		// TODO: REFACTOR THIS SHIT: TMPCIRCLE
		tmpCromosome = crossChromosomes(crossPoint, chromosomeA, chromosomeB);
		tmpCircle = new Circulo(Integer.parseInt(tmpCromosome.substring(0, bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(bitsPerAttribute, 2*bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(2*bitsPerAttribute, 3*bitsPerAttribute), 2));
		tmpFitness = generateFitness(tmpCircle);

		return new Individuo(tmpCromosome, tmpFitness);
	}
	
	
	
	/*
	public ArrayList<Individuo> generateNewPopulationOf(int size, float totalFitness) {
		int insertedIndivuduals;
		float accumProbability[];

		int indexChromosomeA;
		int indexChromosomeB;
		
		Individuo newCrossedIndividual;
		ArrayList<Individuo> newPopulation = new ArrayList<Individuo>();
		
	
		accumProbability = calculateAccumProbability(totalFitness); 
		
		insertedIndivuduals = 0;
		
		// Iterar hasta que tengamos N nuevos individuos.
		while (insertedIndivuduals < size) {
						
			indexChromosomeA = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 1: Llamar Función que te devuelve un indice del cromosoma
			indexChromosomeB = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 2: Llamar Función que te devuelve un indice del cromosoma

			if (canCrossChromosomes()) {
				newCrossedIndividual = generateCrossedIndividual(indexChromosomeA, indexChromosomeB);
				newPopulation.add(newCrossedIndividual);
				insertedIndivuduals += 1;
			}
			else {
				// TODO: Mutate chromosomes
				// System.out.println("Va a mutar quien yo te diga...");
				// Mutar los cromosomas
			}
		}
		
		return newPopulation;
	}
	*/
		
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

	// Sum all the individuals fitness score 
	public float sumAllFitness() {
		float total = 0;

		for (Individuo e : population) {
			total += e.getFitness();
		}
		
		return total;
	}
	
}
