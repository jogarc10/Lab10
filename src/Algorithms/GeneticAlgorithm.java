package Algorithms;

import java.util.Random;

import main.Circulo;
import main.Individuo;
import main.Problema;

public class GeneticAlgorithm extends Algorithms {
	
	private static final int N = 10; // Initial population size
	private final int bitsPerAttribute = 10; // x, y, z
	private Individuo population[] = new Individuo[N];
	private Problema problem;

	/* Constructor: Generate an initial random population */
	public GeneticAlgorithm(Problema p) {
		this.problem = p;
		this.population = generatePopulation(GeneticAlgorithm.N);
	}

	@Override
	public Circulo BestSolution(Problema p) {
		return null;
	}
	
	/* 
	 * Generating a random population of size N
	 * Returns a list of N individuals
	 */
	public Individuo[] generatePopulation(int n_individuals) {
		
		Individuo new_population[] = new Individuo[n_individuals];
		
		for (int i = 0; i < n_individuals; i++) {
			String cromosoma = generateChromosome();
			float fitness = generateFitness(cromosoma);
			new_population[i] = new Individuo(cromosoma, fitness);
		}
		
		return new_population;
	}
	
	public String generateChromosome() {
		int size = 2^this.bitsPerAttribute;
		Circulo c = Circulo.random(size);
		String chromosome = "";
		
		return chromosome;
	}
	
//	public String generateChromosome() {
//		String chromosome = "";
//		int lenght = this.bitsPerAttribute; /* Number of bits */
//		
//		chromosome.concat(generateBits(lenght));
//		chromosome.concat(generateBits(lenght));
//		chromosome.concat(generateBits(lenght));
//		
//		return chromosome;
//	}
	
	/* 
	 * Generate a random string of bits given
	 * the length of the string
	 */
	public String generateBits(int bits_number) {
		int n, length;
		Random rg;
		
		length = 2^bits_number; /* length = 2 to the power of bits_number */
		rg = new Random();
	    n = rg.nextInt(length);
	    
	    return Integer.toBinaryString(n);
	}
	
	public int binaryToDecimal(String binary_number) {
		// TODO
		return -1;
	}
	
	public float generateFitness(String chromosome) {
		Circulo circle;
		int x, y, radius;
		float chromosome_fitness;
		String x_binary, y_binary, r_binary;

		x_binary = chromosome.substring(0, this.bitsPerAttribute - 1);
		y_binary = chromosome.substring(this.bitsPerAttribute, (2 * this.bitsPerAttribute) - 1);
		r_binary = chromosome.substring((2 * this.bitsPerAttribute), (3 * this.bitsPerAttribute) - 1);
		
		x = Integer.parseInt(x_binary, 2); /* From binary String (based 2) to Decimal */
		y = Integer.parseInt(y_binary, 2);
		radius = Integer.parseInt(r_binary, 2);
		
		circle = new Circulo(x, y, radius);
		
		chromosome_fitness = 0;
		
		return chromosome_fitness;
	}
	

}
