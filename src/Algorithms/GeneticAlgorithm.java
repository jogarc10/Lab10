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
		float fitness;
		float probability[] = new float[N]; /* Probability for each individual to be used: pi = fi / fTotal*/
		float accumProbability[] = new float[N]; 
		float tmpAccumProbability = 0;
		String tmpCromosome;
		float tmpFitness;
		float totalFitness = 0;
		int indexChromosomeA, indexChromosomeB; 
		int newIndividual = 0;
		int crossPoint = 0;
		String chromosomeA, chromosomeB;
		ArrayList<Individuo> tmpPopulation;
		Circulo tmpCircle;
		
		for (int i = 0; i < MAX_GENERATIONS; i++) {
			
			tmpAccumProbability = 0;
			totalFitness = sumAllFitness();
			tmpPopulation = new ArrayList<Individuo>();
			
			for (int j = 0; j < N; j++) {
				fitness = this.population.get(j).getFitness();
				probability[j] = fitness / totalFitness;
				tmpAccumProbability += probability[j];
				accumProbability[j] = tmpAccumProbability;
			}

			newIndividual = 0;
			
			// Iterar hasta que tengamos N nuevos individuos.
			while (newIndividual < N) {
							
				indexChromosomeA = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 1: Llamar Función que te devuelve un indice del cromosoma
				indexChromosomeB = selectIndividualIndex(accumProbability); // Indice aleatorio del cromosoma 2: Llamar Función que te devuelve un indice del cromosoma

//				System.out.println("Index cromo A: " + indexChromosomeA);
//				System.out.println("Index cromo B: " + indexChromosomeB);

				if (canCrossChromosomes()) {
//					System.out.println("Cruzando el super cromosoma...");
					
					crossPoint = crossPoint();
					
					chromosomeA = population.get(indexChromosomeA).getCromosoma();
					chromosomeB = population.get(indexChromosomeB).getCromosoma();

					tmpCromosome = crossChromosomes(crossPoint, chromosomeA, chromosomeB);
					
//					System.out.println("Cromo A: " + chromosomeA);
//					System.out.println("Cromo B: " + chromosomeB);					
//					System.out.println("crossPoint: " + crossPoint);
//					System.out.println("Cromo NP: " + tmpCromosome);
					
					tmpCircle = new Circulo(Integer.parseInt(tmpCromosome.substring(0, bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(bitsPerAttribute, 2*bitsPerAttribute), 2),Integer.parseInt(tmpCromosome.substring(2*bitsPerAttribute, 3*bitsPerAttribute), 2));
					tmpFitness = generateFitness(tmpCircle);		
					tmpPopulation.add(new Individuo(tmpCromosome, tmpFitness)); 
					newIndividual += 1;
				}
				else {
//					System.out.println("Va a mutar quien yo te diga...");
					// Mutar los cromosomas
				}
//				System.out.println("------------------------");
			}
			
			this.population = tmpPopulation; // Assign new population after generated it
				
		}
		
		return bestCircle;
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
	
	public boolean canCrossChromosomes() {
		float randomProbability = Util.random(); // [0, 1)
		return randomProbability <= CROSSOVER_RATE;
	}
	
	public String crossChromosomes(int crossPoint, String chromosomeA, String chromosomeB) {
		String newChromosome = "";
		
//		System.out.println("\tCrossss chromosomes");
//		System.out.println("\t" + chromosomeA);
//		System.out.println("\t" + chromosomeB);
//		
		newChromosome += chromosomeA.substring(0, crossPoint * bitsPerAttribute);
		newChromosome += chromosomeB.substring((crossPoint * bitsPerAttribute), MAX_GENES * bitsPerAttribute);

//		System.out.println("\tnewChromosome" + newChromosome);
		
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
