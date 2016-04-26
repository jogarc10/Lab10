package generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import main.Circulo;
import main.Util;

public class Generator {

	public static void main(String[] args) {
				
		int maxCirc = 5;
		
		try {
			PrintWriter writer = new PrintWriter("case001.txt");
			
			int n = Util.randomInt(1, 10);
			
			writer.print(Integer.toString(n));
			writer.print(" ");
			
			for (int i = 0; i < n; i++) {
				Circulo c = Circulo.random(maxCirc);
				
				writer.print(Integer.toString(c.getX()) + " ");
				writer.print(Integer.toString(c.getY()) + " ");
				writer.print(Integer.toString(c.getRadio()) + " ");
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
