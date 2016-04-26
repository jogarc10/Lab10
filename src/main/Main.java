package main;
import java.io.FileNotFoundException;

import Algorithms.BruteForce;
import Algorithms.RandomMethod;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Problema p = new Problema("case001.txt");
		Circulo brute, random;
		
		BruteForce b = new BruteForce();
		brute = b.BestSolution(p);
		RandomMethod r = new RandomMethod();
		random = r.BestSolution(p);
		
	}

}
