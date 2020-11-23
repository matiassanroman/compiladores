package compilador;

import java.util.ArrayList;

public class Conversor {
	private ArrayList<String> mapeo;
	
	// Constructor con los tipos que se nos asignaron
	public Conversor() {
		this.mapeo = new ArrayList<String>();
		this.mapeo.add("INTEGER");
		this.mapeo.add("FLOAT");
		this.mapeo.add("CADENA");
	}
	
	// Definicion de matrices que representan el tipo resultantes de operar y 
	// asignar junto con una tabla analoga, que establece si es posible o no 
	// operar/asignar entre esos tipos
	
	
	private String[][] compatibilidades= {
// Forma de lectura: tipo fila 'operacion' tipo columna da como resultado el tipo de casilla (x,y)
//	           INTEGER    FLOAT    CADENA	
/*INTEGER*/  {"INTEGER", "FLOAT",      "X"},
/*FLOAT*/    {  "FLOAT", "FLOAT",      "X"},
/*CADENA*/   {      "X",     "X",      "X"}  };
	
	private boolean[][] habilitacionOperacion = {
// Forma de lectura: posicion (x,y) indica si es posible operar entre el tipo de fila y el tipo de la columna
//		       INTEGER    FLOAT   CADENA	
/*INTEGER*/  {    true,    true ,  false},
/*  FLOAT*/  {    true,    true ,  false},
/* CADENA*/  {   false,   false ,  false}  };
	
	private String[][] asignaciones = {
// Forma de lectura: Al tipo fila, cuando se le asigna el tipo columna da como resultado el tipo de casilla (x,y)
//			   INTEGER    FLOAT    CADENA	
/*INTEGER*/  {"INTEGER",     "X",      "X"},
/*FLOAT*/    {  "FLOAT", "FLOAT",      "X"},
/*CADENA*/   {      "X",     "X", "CADENA"}  };
				
private boolean[][] habilitacionAsignacion = {
// Forma de lectura: posicion (x,y) indica si es posible operar entre el tipo de fila y el tipo de la columna
//		       INTEGER   FLOAT   CADENA	
/*INTEGER*/  {    true,  false , false},
/*  FLOAT*/  {    true,   true , false},
/* CADENA*/  {   false,  false ,  true}  };

	// Metodo privado que mapea el tipo dado a su possion en las matrices
	private int mapear(String tipo) {
		return this.mapeo.indexOf(tipo);
	}
	
	// Retorna si en compatible operar entre dos tipos dados
	public boolean esCompatibleOperarEntre(String tipoIzq, String tipoDer) {
		int fila    = mapear(tipoIzq);
		int columna = mapear(tipoDer);
		return this.habilitacionOperacion[fila][columna];
	}
	
	// Retorna el tipo resultante de operar, en forma de string
	public String TipoRetornoOperacion(String tipoIzq, String tipoDer) {
		int fila    = mapear(tipoIzq);
		int columna = mapear(tipoDer);
		return this.compatibilidades[fila][columna];
	}
	
	// Retorna si en compatible asigna al tipo de la izquierda
	// el tipo de la derecha
	public boolean esCompatibleAsignar(String tipoIzq, String tipoDer) {
		return this.habilitacionAsignacion[mapear(tipoIzq)][mapear(tipoDer)];
	}
	
	// Retorna si es compatible asignar entre dos tipos dados
	public String TipoRetornAsignacion(String tipoIzq, String tipoDer) {
		return this.asignaciones[mapear(tipoIzq)][mapear(tipoDer)];
	}
	
}
	