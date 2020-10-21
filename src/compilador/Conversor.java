package compilador;

import java.util.ArrayList;

public class Conversor {
	private ArrayList<String> mapeo;
	
	public Conversor() {
		this.mapeo = new ArrayList<String>();
		this.mapeo.add("INTEGER");
		this.mapeo.add("FLOAT");
		this.mapeo.add("CADENA");
	}
	
	private String[][] compatibilidades= {
// Forma de lectura: tipo fila 'operacion' tipo columna da como resultado el tipo de casilla (x,y)
//	           INTEGER    FLOAT    CADENA	
/*INTEGER*/  {"INTEGER", "FLOAT",      "X"},
/*FLOAT*/    {  "FLOAT", "FLOAT",      "X"},
/*CADENA*/   {      "X",     "X", "CADENA"}  };
	
	private boolean[][] habilitacionOperacion = {
// Forma de lectura: posicion (x,y) indica si es posible operar entre el tipo de fila y el tipo de la columna
//		       INTEGER    FLOAT   CADENA	
/*INTEGER*/  {    true,    true ,  false},
/*  FLOAT*/  {    true,    true ,  false},
/* CADENA*/  {   false,   false ,   true}  };
	
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

	private int mapear(String tipo) {
		return this.mapeo.indexOf(tipo);
	}
	
	public boolean esCompatibleOperarEntre(String tipoIzq, String tipoDer) {
		int fila    = mapear(tipoIzq);
		int columna = mapear(tipoDer);
		return this.habilitacionOperacion[fila][columna];
	}
	
	public String TipoRetornoOperacion(String tipoIzq, String tipoDer) {
		int fila    = mapear(tipoIzq);
		int columna = mapear(tipoDer);
		return this.compatibilidades[fila][columna];
	}

	public boolean esCompatibleAsignar(String tipoIzq, String tipoDer) {
		return this.habilitacionAsignacion[mapear(tipoIzq)][mapear(tipoDer)];
	}
	
	public String TipoRetornAsignacion(String tipoIzq, String tipoDer) {
		return this.asignaciones[mapear(tipoIzq)][mapear(tipoDer)];
	}
	
}
	