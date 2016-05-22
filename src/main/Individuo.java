package main;

/**
 * Representa un individuo de la población como cromosoma. Almacena el valor de fitness 
 * por comodidad, para ordenar la población.
 */
public class Individuo implements Comparable<Individuo> {
	
	// Número de genes para representar cada entero
	private final int PADDING = 10;
	
	// Representación binaria del círculo con centro (x,y) y radio r. Se representa como
	// la concatenación de las cadenas x + y + r donde cada una consta de 10 dígitos binarios.
	private String cromosoma;
	
	// Fitness de este individuo
	private float fitness;
	
	/**
	 * Constructor a partir del cromosoma. 
	 */
	public Individuo(String cromosoma, float fitness) {
		this.cromosoma = cromosoma;
		this.fitness = fitness;
	}
	
	/**
	 * Constructor a partir del círculo.
	 */
	public Individuo(Circulo c) {
		cromosoma = int2str(c.getX()) + int2str(c.getY()) + int2str(c.getRadio());
	}

	// Get substring

	public String getChromosomeStart(int crossPoint) {
		return this.cromosoma.substring(0, crossPoint);
	}
	
	public String getChromosomeEnd(int crossPoint) {
		return this.cromosoma.substring(crossPoint);
	}
	
	public void swapBit(int index) {
		StringBuilder myCromosoma = new StringBuilder(this.cromosoma);
		
		 System.out.println("Index: " + index);
		 System.out.println("OLD: " + this.cromosoma);
		
		if (this.cromosoma.charAt(index) == '1')
			myCromosoma.setCharAt(index, '0');
		else
			myCromosoma.setCharAt(index, '1');
		
		this.cromosoma = myCromosoma.toString();
		 System.out.println("NEW: " + this.cromosoma);
	}

	/**
	 * Devuelve el círculo representado por este cromosoma.
	 */
	public Circulo toCirculo() {
		int x = Integer.parseInt(cromosoma.substring(0, PADDING), 2);
		int y = Integer.parseInt(cromosoma.substring(PADDING, 2*PADDING), 2);
		int r = Integer.parseInt(cromosoma.substring(2*PADDING, 3*PADDING), 2);
		return new Circulo(x, y, r);
	}
	
	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fitness) {
		this.fitness = fitness;
	}

	public String getCromosoma() {
		return cromosoma;
	}

	/*
	 * Devuelve la cadena que representa al número n en binario con 10 posiciones.
	 * Las posiciones de la izquierda sobrantes se ponen a cero.
	 */
	private String int2str(int n) {
		return String.format("%"+PADDING+"s", Integer.toBinaryString(n)).replace(' ', '0');
	}

	/**
	 * Comparación por fitness para ordenar la población de individuos.
	 */
	@Override
	public int compareTo(Individuo o) {
		return Float.compare(fitness, o.fitness);
	}

	@Override
	public String toString() {
		return "Gen [cadena=" + cromosoma + ", fitness=" + fitness + "]";
	}
}
