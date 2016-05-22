package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Representa el conjunto de círculos inicial del problema.
 * 
 * @author Antonio Sánchez
 */
public class Problema {

	/** Tamaño del cuadrado */
	public static int DIMENSION = 1024;
	
	// Lista de círculos
	private List<Circulo> circulos;

	/**
	 * Crea un problema sin círculos.
	 */
	public Problema() {
		circulos = new ArrayList<>();
	}
	
	/**
	 * Lee un problema a partir de un fichero de texto. 
	 */
	public Problema(String fichero) throws FileNotFoundException {
		
		Scanner sc = new Scanner(new File(fichero));
		circulos = new ArrayList<>();
		
		int numCirculos = sc.nextInt();
		for (int i=0; i<numCirculos; i++) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int r = sc.nextInt();
			circulos.add(new Circulo(x, y, r));
		}
		sc.close();
	}
	
	public List<Circulo> getCirculos() {
		return circulos;
	}

	public void addCirculo(Circulo c) {
		circulos.add(c);
	}
	
	/**
	 * Indica si el círculo c es una solución válida del problema: si está dentro
	 * del cuadrado y no solapa con ninguno de los círculos existentes.
	 */
	public boolean esSolucion(Circulo c) {
		// Está dentro del cuadrado
		if (!c.dentroDeCuadrado(DIMENSION))
			return false;
		
		// No tiene intersección con los otros círculos
		for (Circulo circulo: circulos) {
			if (circulo.interseccion(c))
				return false;
		}
		
		return true;
	}
}
