package compilador;

import java.util.ArrayList;

public class PolacaInversa {
	
	private ArrayList<Par> pasosPolaca;
	private ArrayList<Integer> pasosIncompletos;
	
	public static int retrocesosIfThenElse = 2;
	public static int retrocesosIfThen = 1;
	
	public static int getRetrocesosITE() { return retrocesosIfThenElse;	}
	public static int getRetrocesosIT() { return retrocesosIfThen; }
	
	public PolacaInversa() {
		this.pasosPolaca = new ArrayList<Par>();
		this.pasosIncompletos = new ArrayList<Integer>();
	}
	
	public void agregarPaso(Par par) {
		par.setClave(pasosPolaca.size());
		this.pasosPolaca.add(par);
	}
	
	public void agregarPasoIncompleto() {
		this.pasosIncompletos.add(this.pasosPolaca.size()-1);	
		// tener en cuenta que para completa las pasos de la polaca debe recorrer
		// la lista de pasos incompletos y completar la lista de pasos desde el final 
		// hacia el principio
	}
	
	// metodo para completar
	public void completarPolaca(int cantSaltos) {
		// 
		int ref = pasosPolaca.size();
		int saltos = this.pasosIncompletos.size()-cantSaltos;
		for (int i = this.pasosIncompletos.size()-1; i >= saltos; i--) {
			Integer pos = this.pasosIncompletos.get(i);
			String referencia = String.valueOf(ref);
			this.pasosPolaca.get(pos).setValor(referencia);
			ref = pasosPolaca.get(pos).getClave()+2;
			this.pasosIncompletos.remove(i);
		}
	}
	
	public String toString() {
		String salida = "Lista de pasos de la polaca inversa:\n";
		for (int i = 0; i < pasosPolaca.size(); i++) {
			if (i == pasosPolaca.size()-1)
				salida = salida + pasosPolaca.get(i).toString();
			else
				salida = salida + pasosPolaca.get(i).toString() + "\n";
		}
		return salida;
	}
	
	
	
	
	
	
	
}
