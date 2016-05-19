package Algorithms;

import java.util.ArrayList; 
import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class GeneticAlgorithm extends Algorithms {
	
	private static final int MAX_GENES = 3; 
	private static final int CROSS_POINTS = 2; 
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
		float totalFitness;
		
		for (int i = 0; i < MAX_GENERATIONS; i++) {
			
			totalFitness = sumAllFitness();
			this.population = generateNewPopulationOf(N, totalFitness); // Assign new population after generated it	
						
		}
		
		return bestCircle;
	}
	
	/*
	 * Calculate Probability for each individual to be used: pi = fi / fTotal
	 */
	public float[] calculateAccumProbability(float totalFitness) {
		float accumProbability[] = new float[N];
		float tmpAccumProbability;
		float fitness;
		
		tmpAccumProbability = 0;
		
		for (int j = 0; j < N; j++) {
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

		tmpCromosome = crossChromosomes(crossPoint, chromosomeA, chromosomeB);
		// TODO: REFACTOR THIS SHIT: TMPCIRCLE
		tmpCircle = new Circulo(Integer.parseInt(tmpCromosome.substring(0, bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(bitsPerAttribute, 2*bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(2*bitsPerAttribute, 3*bitsPerAttribute), 2));
		tmpFitness = generateFitness(tmpCircle);

		return new Individuo(tmpCromosome, tmpFitness);
	}
	
	
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
	
	
	public boolean canCrossChromosomes() {
		float randomProbability = Util.random(); // [0, 1)
		return randomProbability <= CROSSOVER_RATE;
	}
	
	public String crossChromosomes(int crossPoint, String chromosomeA, String chromosomeB) {
		String newChromosome = "";
		
		newChromosome += chromosomeA.substring(0, crossPoint * bitsPerAttribute);
		newChromosome += chromosomeB.substring((crossPoint * bitsPerAttribute), MAX_GENES * bitsPerAttribute);

		return newChromosome;
	}
	
	// Elegir punto de cruce
	public int crossPoint() {
		int index = 0;
		float a_random, interval, accumProb; 
		
		a_random = Util.random(); // [0, 1)
		interval =  (float) 1/CROSS_POINTS;
		accumProb = 0;
		
		while(a_random > accumProb) {
			accumProb += interval;
			index += 1;
		}

		return index;
	}
	
	// Generating a random population of size N
	//Returns a list of N individuals
	public ArrayList<Individuo> generatePopulation(int n_individuals) {
		
		int dimension = (int) Math.pow(2, N);
		ArrayList<Individuo> new_population = new ArrayList<Individuo>();
		System.out.println(dimension);
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
