package compilador;

import java.util.ArrayList;

public class PolacaInversa {
	
	private ArrayList<Par> pasosPolaca;
	private ArrayList<Integer> pasosIncompletos;
	private ArrayList<Integer> iniciosDeFOR;
	private ArrayList<String> variablesControl;
	//private ArrayList<Par> procedimientos;
	private ArrayList<String> parametrosFormales;
	
	public static int retrocesosIfThenElse = 2;
	public static int retrocesosIfThen = 1;
	public static int retrocesosFOR = 1;
	public static boolean flagITF;
	public static int nivelProc = 0;
	
	public static boolean getFlagITE() { return flagITF; }
	public static void setFlagITE(boolean estado) { flagITF = estado; }
	
	public static int getRetrocesosITE() { return retrocesosIfThenElse;	}
	public static int getRetrocesosIT()  { return retrocesosIfThen; }
	public static int getRetrocesosFOR() { return retrocesosFOR; }
	
	public static void subirNivelProc() { nivelProc += 1; }
	public static void bajarNivelProc() { nivelProc -= 1; }
	
	/// CONSTRUCTOR
	public PolacaInversa() {
		this.pasosPolaca = new ArrayList<Par>();
		this.pasosIncompletos = new ArrayList<Integer>();
		this.iniciosDeFOR = new ArrayList<Integer>();
		this.variablesControl = new ArrayList<String>();
		//this.procedimientos = new ArrayList<Par>();
		this.parametrosFormales = new ArrayList<String>();
	}
	/// FIN CONSTRUNCTOR
	
	//////////////////////////////////////////////////////////////
	// FUNCIONALIDAD DE LA POLACA
	public void agregarPaso(Par paso) {
		paso.setClave(pasosPolaca.size());
		this.pasosPolaca.add(paso);
	}
	
	public void borrarPasoPolaca() {
		int pos = this.pasosPolaca.size()-1;
		this.pasosPolaca.remove(pos);
	}
	
	public String toString() {
			
		String salida = "Lista de pasos de la polaca inversa:\n";
		
		//Acomodo los indices
		int cantidad = 0;
		ArrayList<Par> aux = new ArrayList<Par>();
		
		for (int i = 0; i < pasosPolaca.size(); i++) {
			Par p = new Par(pasosPolaca.get(i).getValor());
			p.setClave(cantidad);
			aux.add(p);
			cantidad++;
		}
		
		pasosPolaca = aux;
		reordenarFinal();
		
		for (int i = 0; i < pasosPolaca.size(); i++) {
			if (i == pasosPolaca.size()-1)
				salida = salida + pasosPolaca.get(i).toString();
			else
				salida = salida + pasosPolaca.get(i).toString() + "\n";
		}
		
		return salida;
	
	}
	
	public ArrayList<Par> getPolaca(){
		return this.pasosPolaca;
	}
	
	public Par getPasoEnPos(int pos){
		return this.pasosPolaca.get(pos);
	}
	
	// FIN FUNCIONALIDAD POLACA
	//////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////
	// FUNCIONALIDAD PARA COMPLETAR PASOS INCOMPLETOS BF BI
	
	public void agregarPasoIncompleto() {
		this.pasosIncompletos.add(this.pasosPolaca.size()-1);	
		// tener en cuenta que para completa las pasos de la polaca debe recorrer
		// la lista de pasos incompletos y completar la lista de pasos desde el final 
		// hacia el principio
	}
	
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
	
	public void borrarPasoIncompleto() {
		int pos = this.pasosIncompletos.size()-1;
		this.pasosIncompletos.remove(pos);
	}
	
	// FIN FUNCIONALIDAD PARA COMPLETAR PASOS INCOMPLETOS BF BI
	//////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////
	// FUNCIONALIDAD PARA TRATAMIENTO DE CICLOS FOR
	
	public void agregarInicioFOR() {
		int inicio = this.pasosPolaca.size();
		this.iniciosDeFOR.add(inicio);
	}
	
	public void agregarVariableControl(String elemento) {
		this.variablesControl.add(elemento);
	}
	
	public void completarFOR() {
		int pos = this.pasosIncompletos.size()-1;
		int posACompletar = pasosIncompletos.get(pos);
		int salto = this.iniciosDeFOR.size()-1;
		int inicio = this.iniciosDeFOR.get(salto);
		String i = String.valueOf(inicio);
		this.pasosPolaca.get(posACompletar).setValor(i);
	}
	
	public void borrarInicioFOR() {
		int pos = this.iniciosDeFOR.size()-1;
		this.iniciosDeFOR.remove(pos);
	}
	
	private void borrarUnaVariableControl() {
		int pos = this.variablesControl.size()-1;
		this.variablesControl.remove(pos);
	}
	
	public void borrarVariablesControl() {
		int pos = this.variablesControl.size()-1;
		String incDec = this.variablesControl.get(pos);
		String comp = this.variablesControl.get(pos-1);
		String var = this.variablesControl.get(pos-2);
		Par parIncDec = new  Par(incDec);
		Par parComp = new  Par(comp);
		Par parVar = new  Par(var);
		Par parAsig = new  Par("=");
		agregarPaso(parIncDec);
		agregarPaso(parVar);
		agregarPaso(parComp);
		agregarPaso(parVar);
		agregarPaso(parAsig);	
		borrarUnaVariableControl();
		borrarUnaVariableControl();
		borrarUnaVariableControl();
	}
	
	// FIN FUNCIONALIDAD PARA TRATAMIENTO DE CICLOS FOR
	//////////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////////
	// FUNCIONALIDAD PARA AGREGAR LABELS
	
	public void agregarLabel() {
		Integer posInt = pasosPolaca.size();
		String posString = Integer.toString(pasosPolaca.size());
		Par paso = new Par("L"+posString);
		paso.setClave(posInt);
		this.pasosPolaca.add(paso);
	}
	
	// FIN FUNCIONALIDAD PARA AGREGAR LABELS
	//////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////
	// FUNCIONALIDAD PARA PASAJE DE PARAMETROS
	
	public void agregarParametro(String procParam) {
		this.parametrosFormales.add(procParam);
	}

	public int inicioProc(String nombre) {
		int pos =  this.parametrosFormales.size()-1;
		while (pos>=0 && !parametrosFormales.get(pos).contains("PROC "+nombre)) {
			pos--;
		}
		return pos-1;
	}
	
	public void asignarParametros(ArrayList<String> parametrosInvocados, int inicio) {
		// 'parametros' son los parametros de la invocacion
		// inicio es el nivel de ese procedimiento en la lista de 
		int cant = parametrosInvocados.size();
		int posParamFormales = inicio+2;
		for (int i=0; i < cant; i++) {
			Par operando1 = new Par(parametrosInvocados.get(i));
			Par operador  = new Par("=");
			Par operando2 = new Par(this.parametrosFormales.get(posParamFormales));
			this.agregarPaso(operando1);
			this.agregarPaso(operando2);
			this.agregarPaso(operador);
			posParamFormales++;
		}
	}
	
	public void borrarProcYParametros() {
		int i=0;
		while( (i < this.parametrosFormales.size()) && (i+1 < this.parametrosFormales.size()) ) {
			if (this.parametrosFormales.get(i+1).contains("PROC")) {
				int nivelActual = Integer.parseInt(this.parametrosFormales.get(i));
				if ( Math.abs(nivelActual - nivelProc) >= 2 ) {
					this.parametrosFormales.remove(i);
					this.parametrosFormales.remove(i);
					int j=i;
					while ((j < this.parametrosFormales.size()-2)&&!this.parametrosFormales.get(j+2).contains("PROC")) {
						this.parametrosFormales.remove(i);
						j++;
					}
					this.parametrosFormales.remove(i);
				}
			}
			i++;
		}
	}
	
	public void mostrarParametrosFormales() {
		for (int i=0; i < parametrosFormales.size(); i++)
			System.out.println(this.parametrosFormales.get(i).toString());
	}
	
	// FIN FUNCIONALIDAD PARA PASAJE DE PARAMETROS
	//////////////////////////////////////////////////////////////
	
//	
//	public void agregarProcedimiento(Par proc){
//		proc.setClave(pasosPolaca.size());
//		this.procedimientos.add(proc);
//	}
//	
//	public void eliminarProc(){
//		if(nivelProc > 1) {
//			int pos = procedimientos.size()-1;
//			this.procedimientos.remove(pos);
//		}
//	}
//	
//	public String buscarInicioProc(String nombreProc) {
//		for ( int i=this.procedimientos.size()-1 ; i>=0 ; i--)
//			if (this.procedimientos.get(i).tieneMismoValor(nombreProc))
//				return Integer.toString(this.procedimientos.get(i).getClave());
//		return "-1";
//	}
//	
//	public void mostrarProcs(){
//		for (int i=0; i< procedimientos.size(); i++)
//			System.out.println(procedimientos.get(i).toString());
//	}
	
	public void reordenarFinal() {
		
		ArrayList<Par> proc = new ArrayList<Par>();
		ArrayList<Par> aux2 = new ArrayList<Par>();
		ArrayList<Par> aux3 = new ArrayList<Par>();
	
		for(int i=0; i<pasosPolaca.size(); i++) {
			if(pasosPolaca.get(i).getValor().length() > 3 && pasosPolaca.get(i).getValor().substring(0, 4).equals("PROC")) {
				proc.add(pasosPolaca.get(i));
			}
			else if(pasosPolaca.get(i).getValor().length() > 2 && pasosPolaca.get(i).getValor().substring(0, 3).equals("RET")) 
				proc.add(pasosPolaca.get(i));
		}
				
		int recorrido = proc.size();
		int inic = 0, fin = 0;
		
		while(recorrido > 0) {
			int j= 0;
			boolean p= true;
			while(j < proc.size() && p){
				if(proc.get(j).getValor().contains("RET")) {
					inic = proc.get(j-1).getClave();
					fin = proc.get(j).getClave();
					proc.remove(j-1);
					proc.remove(j-1);
					p = false;	
					recorrido = recorrido - 2;
				}
				j++;
			}
			
			if(aux2.size() == 0) {
				for(int i=inic; i<=fin; i++) {
					aux2.add(pasosPolaca.get(i));
				}
			}
			else {
				for(int i=inic; i<=fin; i++) {
					boolean esta = false;
					for(int z=0; z<aux2.size(); z++) {
						if(aux2.get(z).getClave().equals(i))
							esta = true;
					}
					if(!esta) {
						aux2.add(pasosPolaca.get(i));
					}
				}
			}			
		}
		
		//aux2 estan los proc
		//aux3 el main
		if(aux2.size() > 0) {
			for(int i=0; i<pasosPolaca.size(); i++) {
				if(!pasosPolaca.get(i).getValor().contains("PROC")) {
					aux3.add(pasosPolaca.get(i));
				}
				else
					break;
			}
			
			aux3.addAll(aux2);
			ArrayList<Par> aux4 = new ArrayList<Par>();
			
			for(int i=pasosPolaca.size()-1; i>=0; i--) {
				if(!pasosPolaca.get(i).getValor().contains("RET")) {
					aux4.add(pasosPolaca.get(i));
				}
				else {
					for(int z=aux4.size()-1; z>=0; z--) {
						aux3.add(aux4.get(z));
					}
					break;
				}
			}
			pasosPolaca = aux3;
		}
	}
}