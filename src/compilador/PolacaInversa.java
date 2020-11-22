package compilador;

import java.util.ArrayList;

public class PolacaInversa {
	
	private ArrayList<Par> pasosPolaca;
	private ArrayList<Integer> pasosIncompletos;
	private ArrayList<Integer> iniciosDeFOR;
	private ArrayList<String> variablesControl;
	//private ArrayList<Par> procedimientos;
	private ArrayList<String> parametros;
	
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
		this.parametros = new ArrayList<String>();
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
		this.parametros.add(procParam);
	}

	public int inicioProc(String nombre) {
		int pos =  this.parametros.size()-1;
		while (pos>=0 && !parametros.get(pos).contains("PROC "+nombre)) {
			pos--;
		}
		return pos-1;
	}
	
	public void borrarProcYParametros() {
		int pos = this.parametros.size()-1;		
		while (pos >=0 && !this.parametros.get(pos).contains("PROC")) {
			pos--;
		}
		
//		if (nivelProc > 1) {
//			for(int i=parametros.size()-1; (i>=0 && !parametros.get(i).contains("PROC"));i--)
//				this.parametros.remove(i);
//			int pos = this.parametros.size()-1;
//			this.parametros.remove(pos);
//			nivelProc--;
//		}
	}
	
	public void asignarParametros(ArrayList<String> parametros, int inicio) {
		int cant = parametros.size();
		int posP = inicio+2;
		//for (int i=0, i < cant, i++)
			// par1 oper1
			// par2 oper2
			// par3 operacion
			// agregar pasos a la polaca
	}
	
	
	public void mostrarParametros() {
		for (int i=0; i < parametros.size(); i++)
			System.out.println(this.parametros.get(i).toString());
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
	
}