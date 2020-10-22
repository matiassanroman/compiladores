package compilador;

import java.util.ArrayList;

public class PolacaInversa {
	
	private ArrayList<Par> pasosPolaca;
	private ArrayList<Integer> pasosIncompletos;
	
	public PolacaInversa() {
		this.pasosPolaca = new ArrayList<Par>();
		this.pasosIncompletos = new ArrayList<Integer>();
	}
	
	public void agregarPaso(Par par) {
		par.setClave(pasosPolaca.size());
		this.pasosPolaca.add(par);
	}
	
	public void agregarPasoIncompleto(Integer pos) {
		this.pasosIncompletos.add(pos);	
		// tener en cuenta que para completa las pasos de la polaca debe recorrer
		// la lista de pasos incompletos y completar la lista de pasos desde el 
		// hacia el principio
	}
	
	// metodo para completar
	public void completarPolaca() {
		// 
		int ref = pasosPolaca.get(pasosPolaca.size()-1).getClave();
		for (int i = this.pasosIncompletos.size()-1;i >=0; i--) {
			Integer pos = this.pasosIncompletos.get(i);
			String referencia = String.valueOf(ref);
			this.pasosPolaca.get(pos).setValor(referencia);
			ref = pasosPolaca.get(pos).getClave()+2;
		}
	}
	
	public String toString() {
		String salida = "";
		for (int i = 0; i < pasosPolaca.size(); i++) {
			if (i == pasosPolaca.size()-1)
				salida = salida + pasosPolaca.get(i).toString();
			else
				salida = salida + pasosPolaca.get(i).toString() + "\n";
		}
		return salida;
	}
	
	
	
	
	
	
	
}
