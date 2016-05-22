package Algorithms;

import java.util.Vector;

import main.Circulo;
import main.Individuo;
import main.Problema;
import main.Util;

public class Pair {

	private Individuo[] individuals = new Individuo[2];

	private static final int TOTAL_GENS = 3; // x, y, z
	private static final int BITS_PER_GEN = 10; // x, y, z
	private static final double CROSSOVER_RATE = 0.7; // Umbral para el emparejamiento
	private static final double MUTATION_PROBABILITY = 0.1;

	public Pair(Individuo A, Individuo B) {
		individuals[0] = A;
		individuals[1] = B;
	}

	/******************************
	******** Cross methods ********
	*******************************/

	public void cross() {
		if (canCrossChromosome()) {
			int crossPoint = randomCrossPoint(); //  Randomly divide chromosomes into 2 segments
			swapChromosomes(crossPoint);
		}
	}

	private int randomCrossPoint() {
		int crossPoint = Util.randomInt(0, TOTAL_GENS);
		return crossPoint * BITS_PER_GEN;
	}

	private boolean canCrossChromosome() {
		float randomProbabilityA = Util.random(); // [0, 1)
		return randomProbabilityA <= CROSSOVER_RATE;
	}

	private void swapChromosomes(int crossPoint) {
		String chromoA;
		String chromoB;

		chromoA = individuals[1].getChromosomeStart(crossPoint)
				+ individuals[0].getChromosomeEnd(crossPoint);

		chromoB = individuals[0].getChromosomeStart(crossPoint)
				+ individuals[1].getChromosomeEnd(crossPoint);

		this.individuals[0] = new Individuo(chromoA, 0);
		this.individuals[1] = new Individuo(chromoB, 0);
	}

	public void calculateFitness() {
		Circulo tmpCircle;
		float tmpRadio, newFitness;
		for (int i = 0; i < this.individuals.length; i++) {
			tmpCircle = this.individuals[i].toCirculo();
			
			if (isValidCircle(tmpCircle)) {
				tmpRadio = (float) tmpCircle.getRadio();
				newFitness = tmpRadio;
			}
			else {
				newFitness = 0;
			}
			
			this.individuals[i].setFitness(newFitness);
		}
	}
	
	public boolean isValidCircle(Circulo c) {
		
		// Está dentro del cuadrado
		if (!c.dentroDeCuadrado(Problema.DIMENSION))
			return false;
		
		// No tiene intersección con los otros círculos
		for (Circulo circulo: GeneticAlgorithm.problem.getCirculos()) {
			if (circulo.interseccion(c))
				return false;
		}
		
		return true;
	}

	/*************************
	******** Mutation ********
	**************************/

	public void mutate() {
		int bitsPerChromosome = TOTAL_GENS * BITS_PER_GEN;

		for (int i = 0; i < this.individuals.length; i++) {
			for (int bit_index = 0; bit_index < bitsPerChromosome; bit_index++) {
				if (canMutate()) {
					this.individuals[i].swapBit(bit_index);
				}
			}
		}
	}

	private boolean canMutate() {
		float randomProbabilityA = Util.random(); // [0, 1)
		return randomProbabilityA <= MUTATION_PROBABILITY;
	}

	/*******************************
	 * Getters
	 ******************************/

	public Vector<Individuo> toList() {
		Vector<Individuo> individualsList = new Vector<Individuo>();

		for (Individuo i : individuals) {
			individualsList.add(i);
		}

		return individualsList;
	}

}
