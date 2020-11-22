%{
package compilador;
import java.io.IOException;
import java.util.ArrayList;
import accionesSemanticas.*;
%}

%token ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN CTE
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'

%%
programa : bloquePrograma
{
	mostrarMensaje("Reconoce bien el programa");
	System.out.println(polaca.toString());
}
		 | error
{
	yyerror("Programa invalido, error en linea: " + compilador.Compilador.nroLinea);
}
		 ;

bloquePrograma : bloquePrograma sentenciaDeclarativa
{
}
			   | bloquePrograma sentenciaEjecutable
{
}
			   | sentenciaDeclarativa
{
}
               | sentenciaEjecutable
{
}
               ;

sentenciaDeclarativa : tipo listaVariables ';'
{
	mostrarMensaje("Declaracion de una o mas variables en linea nro: " + compilador.Compilador.nroLinea);
}     
					 | declaracionProcedimiento 
{
	Par retorno = new Par("RET");
	polaca.agregarPaso(retorno);
}
					 ;

listaVariables : listaVariables ',' identificador
{
	setearAmbitoyDeclarada($3.sval,"");
	if(sePuedeUsar($3.sval) == 2){
		mostrarMensaje($3.sval + " esta Redeclarada.");
	}
}
			   | identificador
{
	setearAmbitoyDeclarada($1.sval,"");
	if(sePuedeUsar($1.sval) == 2){
		mostrarMensaje($1.sval + " esta Redeclarada.");
	}
}
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc
{
	mostrarMensaje("Procedimiento completo, en linea nro: " + compilador.Compilador.nroLinea);
	disminuirAmbito();
	compilador.Compilador.na = compilador.Compilador.na + compilador.Compilador.naa;
}
						 ;

encabezadoProc : | PROC identificador '(' ')'  NA '=' CTE ',' NS '=' CTE
{
	Par proc = new Par($1.sval+" "+$2.sval);
	polaca.agregarPaso(proc);
	mostrarMensaje("Procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc($2.sval, "0", $7.sval, $11.sval);
	setearAmbito($2.sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" + $2.sval;
	setearAmbitoNaNs($7.sval,$11.sval);
	if(sePuedeUsar($2.sval) == 2){
		mostrarMensaje($2.sval + " esta Redeclarada.");
	}
	verificarNa($7.sval,$2.sval);
}
				 | PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{
	PolacaInversa.subirNivelProc();
	polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
	polaca.agregarParametro(val_peek(12).sval+" "+val_peek(11).sval);
	polaca.agregarParametro(val_peek(8).sval);
	Par proc = new Par($1.sval+" "+$2.sval);
	polaca.agregarPaso(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc($2.sval, "1", $9.sval, $13.sval);
	setearAmbito($2.sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval;
	setearAmbitoNaNs($9.sval,$13.sval);
	if(sePuedeUsar($2.sval) == 2){
		mostrarMensaje($2.sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada($5.sval,$4.sval);
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{
	Par proc = new Par($1.sval+" "+$2.sval);
	polaca.agregarPaso(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc($2.sval, "2", $12.sval, $16.sval);
	setearAmbito($2.sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval;
	setearAmbitoNaNs($12.sval,$16.sval);
	if(sePuedeUsar($2.sval) == 2){
		mostrarMensaje($2.sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada($5.sval,$4.sval);
	setearAmbitoyDeclarada($8.sval,$7.sval);
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{
	Par proc = new Par($1.sval+" "+$2.sval);
	polaca.agregarPaso(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc($2.sval, "3", $15.sval, $19.sval);
	setearAmbito($2.sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" + $2.sval;
	setearAmbitoNaNs($15.sval,$19.sval);
	if(sePuedeUsar($2.sval) == 2){
		mostrarMensaje($2.sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada($5.sval,$4.sval);
	setearAmbitoyDeclarada($8.sval,$7.sval);
	setearAmbitoyDeclarada($11.sval,$10.sval);
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador error ')' NA '=' CTE ',' NS '=' CTE
{
	yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);
}
			   ; 

bloqueProc : '{' bloque '}'
{
	PolacaInversa.bajarNivelProc();
	int posProc = polaca.inicioProc();
	System.out.println("procedimiento de berga inicia "+ posProc);
}
		   ;

bloque : bloque sentenciaDeclarativa
{
}
	   | bloque sentenciaEjecutable
{
}
	   | sentenciaDeclarativa
{
}
       | sentenciaEjecutable
{
}
	   | error
{
	yyerror("Error: no puede haber un seccion vacia, en linea nro: "+ compilador.Compilador.nroLinea);
}
       ;

sentenciaEjecutable : asignacion
{
}
					| OUT '(' CADENA ')' ';'
{
	mostrarMensaje("Sentencia OUT, en linea " + compilador.Compilador.nroLinea);
	Par out = new Par($1.sval);
	Par cadena = new Par($3.sval);
	polaca.agregarPaso(cadena);
	polaca.agregarPaso(out);
}                 
					| OUT '(' error ')' ';'
{
	yyerror("Error: Formato de cadena incorrecto, en linea nro: "+ compilador.Compilador.nroLinea);
}
					| identificador '(' ')' ';'
{
	Par nomProc = new Par($1.sval); 
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	mostrarMensaje("Llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);
}
					| identificador '(' identificador ')' ';'
{
	Par nomProc = new Par($1.sval); 
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	mostrarMensaje("Llamada a procedimiento con 1 parametro en linea nro: " + compilador.Compilador.nroLinea);
}
					| identificador '(' identificador ',' identificador ')' ';'
{
	Par nomProc = new Par($1.sval); 
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	mostrarMensaje("Llamada a procedimiento con 2 parametros en linea nro: " + compilador.Compilador.nroLinea);
}
					| identificador '(' identificador ',' identificador ',' identificador ')' ';'
{
	Par nomProc = new Par($1.sval); 
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	mostrarMensaje("Llamada a procedimiento con 3 parametros en linea nro: " + compilador.Compilador.nroLinea);
}
					| identificador '(' error ')' ';'
{
	yyerror("Error: Cantidad no permitida de parametros, en linea nro: "+ compilador.Compilador.nroLinea);
}
					| IF cuerpoIf
{
	if (PolacaInversa.getFlagITE()){
		polaca.completarPolaca(PolacaInversa.getRetrocesosITE());
	}
	else
		polaca.completarPolaca(PolacaInversa.getRetrocesosIT());
	polaca.agregarLabel();
}
					| cicloFor
{
	mostrarMensaje("Ciclo FOR en linea nro: " + compilador.Compilador.nroLinea);
}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}'
{
	polaca.borrarVariablesControl();
	Par pasoEnBlanco = new Par("");
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI");
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
	polaca.completarFOR();
	polaca.borrarInicioFOR();
	polaca.borrarPasoIncompleto();
	polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());
}
         ;

condicionFor : inicioFor ';' condiFOR ';' incDec 
{
	polaca.borrarPasoPolaca();
}
			 ;

condiFOR : condicion
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
		;

inicioFor : identificador '=' constante
{
	polaca.agregarVariableControl($1.sval);
	Par id = new Par($1.sval);
	polaca.agregarPaso(id);
	Par asig = new Par($2.sval);
	polaca.agregarPaso(asig);
	polaca.agregarInicioFOR();
	polaca.agregarLabel();
}
		  ;

condicion : identificador comparador asignacion
{
	Par id = new Par($1.sval);
	Par comp = new Par($2.sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
		  | identificador comparador identificador
{
	Par id1 = new Par($1.sval);
	Par id2 = new Par($3.sval);
	Par comp = new Par($2.sval);
	polaca.agregarPaso(id1);
	polaca.agregarPaso(id2);
	polaca.agregarPaso(comp);
}
		  | identificador comparador constante
{
	Par id = new Par($1.sval);
	Par comp = new Par($2.sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
		  ;

incDec : UP constante   
{
	polaca.agregarVariableControl("+");
	polaca.agregarVariableControl($2.sval);
}
	   | DOWN constante 
{
	polaca.agregarVariableControl("-");
	polaca.agregarVariableControl($2.sval);
}
	   | error constante
{
	yyerror("Error: incremento/decremento mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
	   ;

bloqueSentencia : bloqueSentencia sentenciaEjecutable
{
}
				| sentenciaEjecutable
{
}
				;

cuerpoIf : cuerpoCompleto END_IF
{
	PolacaInversa.setFlagITE(true);
}
		 | cuerpoIncompleto END_IF
{
	PolacaInversa.setFlagITE(false); 
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoIncompleto();
}
		 ;
		 
cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE '{' bloqueElse '}'
{
}	   	  
			   | '(' condicionIf ')' senteciaUnicaThen ELSE '{' bloqueElse '}'
{
}
 			   | '(' condicionIf ')' '{' bloqueThen '}' ELSE senteciaUnicaElse
{
}
			   | '(' condicionIf ')' senteciaUnicaThen ELSE senteciaUnicaElse
{
}
			   ;  


senteciaUnicaThen : sentenciaEjecutable
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
}
				  ;

senteciaUnicaElse : sentenciaEjecutable
{
}
				  ;

condicionIf : condicion
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
			;

bloqueThen : bloqueSentencia
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
}
		   ;

bloqueElse : bloqueSentencia
{
}
		   ;

cuerpoIncompleto : '(' condicionIf ')' '{' bloqueThen '}'
{
}
				 | '(' condicionIf ')' senteciaUnicaThen
{
}
				 ; 

asignacion : identificador '=' expresion ';' 
{
	setearAmbito($1.sval);
	if(sePuedeUsar($1.sval) == 1){
		mostrarMensaje($1.sval + " No esta declarada.");
	}
	Par id =  new Par($1.sval);
	Par asig = new Par($2.sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(asig);
}
		   | error '=' expresion
{
	yyerror("Error: identificador mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
		   ;

expresion : expresion '+' termino 
{
	Par suma =  new Par("+");
	polaca.agregarPaso(suma);
}
		  | expresion '-' termino 
{
	Par resta =  new Par("-");
	polaca.agregarPaso(resta);
}
		  | termino
{
}
		  ;

termino : termino '*' factor 
{
	Par multi =  new Par("*");
	polaca.agregarPaso(multi);
}
		| termino '/' factor
{ 
	Par division =  new Par("/");
	polaca.agregarPaso(division);
}
		| factor
{
}		
		;

factor : constante
{
}
	   | identificador
{ 
	setearAmbito($1.sval);
	if(sePuedeUsar($1.sval) == 1)
		{mostrarMensaje($1.sval + " No esta declarada.");
	}
    Par id =  new Par($1.sval);
	polaca.agregarPaso(id);
} 
	   ;

comparador : '<='
{
}
		   | '>='
{
}
		   | '!='
{
}
		   | '=='
{
}
		   | '>'
{
}
		   | '<'
{
}
		   | error
{
	yyerror("Error: comparador no permitido, en linea nro: "+ compilador.Compilador.nroLinea);
}
		   ;

tipo : FLOAT
{
}
     | INTEGER
{
}
     ;

identificador : ID
{
}
			  ;

constante : ctePositiva
{
}
		  | cteNegativa
{
}
		  ;
ctePositiva : CTE     
{
	setearAmbito($1.sval);
	comprobarRango($1.sval,false);
	String valor = $1.sval;
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	Par cte =  new Par(valor);
	polaca.agregarPaso(cte);
}
			//| error 
{
	//yyerror("Error: constante positiva mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);
}
			;

cteNegativa	: '-' CTE 
{  
	setearAmbito($2.sval);
	comprobarRango($2.sval,true);
	String valor = $2.sval;
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	Par cte =  new Par('-'+valor);
	polaca.agregarPaso(cte);
}
			//| '-' error
//{
	//yyerror("Error: constante negativa mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);	
//}
		  ;

%%

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////// DEFINICIONES PROPIAS///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Compilador c;
ArrayList<String> errores = new ArrayList<String>();
Token t;
int lineaActual;
ArrayList<String> reconocidos = new ArrayList<String>();
PolacaInversa polaca = new PolacaInversa();

public Parser(Compilador c, ArrayList<String> errores)
{
this.c =c;
this.errores =errores;
}

int i= 0;

public int yylex() {
  
  try {
    Token token = c.getToken();
    this.lineaActual = token.getLinea();
    yylval = new ParserVal(t);
    yylval.sval = token.getLexema();
    return token.getToken();
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  return 0;
}

public void yyerror(String error){
	if (error.equals("syntax error"))
		this.errores.add(error + " en linea " + this.lineaActual);
	else
		errores.add(error);
}

public ArrayList<String> getErrores(){
  return this.errores;
}

public ArrayList<String> getReconocidos(){
  return this.reconocidos;
}

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}

void comprobarRango(String sval, boolean negativo){
	double flotante;
	int entero;

	//ES NEGATIVO???
	if(negativo) {	
		//ES FLOAT Y NEGATIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			String aux = "-" + sval;
			if ( AS10_Verificar_Rango_Float.estaEnRango(aux) ) {			
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				String auxx = AS10_Verificar_Rango_Float.normalizar(flotante);
				
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setValor("-"+auxx);
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setTipo("float");
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setUso("CTE");
				
				//compilador.Compilador.tablaSimbolo.get(auxx).remove(compilador.Compilador.tablaSimbolo.get(auxx).size()-1);
				/*
				Simbolo s = new Simbolo(AS10_Verificar_Rango_Float.normalizar(flotante));
				s.setValor("-"+s.getValor());
				s.setTipo("float");
				s.setUso("CTE");
				*/
				//compilador.Compilador.tablaSimbolo.put(s.getValor(),s);
				//compilador.Compilador.tablaSimbolo.get(auxx).add(s);
				mostrarMensaje("CTE FLOAT negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				mostrarMensaje("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
		//ES ENTERO Y NEGATIVO
		else{
			String aux = "-" + sval;
			if ( AS9_Verificar_Rango_Constante.estaEnRango(aux) ) {			
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setValor("-"+sval);
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("int");
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setUso("CTE");
				
				//compilador.Compilador.tablaSimbolo.put(s.getValor(),s);
				//compilador.Compilador.tablaSimbolo.get(sval).add(s);
				mostrarMensaje("CTE ENTERA negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				mostrarMensaje("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
	//ES POSITIVO	
	}else {
		// ES FLOAT Y POSTIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			if ( AS10_Verificar_Rango_Float.estaEnRango(sval) )
			mostrarMensaje("CTE FLOAT postiva esta dentro del rango");
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				mostrarMensaje("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
		// ES ENTERA Y POSITIVA
		else{
			if ( AS9_Verificar_Rango_Constante.estaEnRango(sval) )
			mostrarMensaje("CTE ENTERA postiva esta dentro del rango");
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				mostrarMensaje("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
		
	}
}

void disminuirAmbito(){
	String [] arreglo = compilador.Compilador.ambito.split("\\:"); 
	String aux = ""; 
	for(int i=0; i<arreglo.length-1; i++){
		if(i == 0)
			aux = arreglo[i];
		else
			aux = aux + ":" + arreglo[i]; 
	} 
	compilador.Compilador.ambito = aux;
}

void verificarNa(String sval, String proc){
	//0 no hay error
	//1 error na de proc x es negativo
	//2 na de proc x es mayor que el na del proc que lo contiene.

	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') 
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}

	if(compilador.Compilador.primero){
		compilador.Compilador.na = Integer.parseInt(sval); 
		compilador.Compilador.primero = false; 
		compilador.Compilador.naa = Integer.parseInt(sval);
	}
	else{
		if(Integer.parseInt(sval) == 0)
			compilador.Compilador.na = compilador.Compilador.na - 1; 
		else
			compilador.Compilador.na = compilador.Compilador.na - Integer.parseInt(sval); 

		if(compilador.Compilador.na < 0){
			//Error 1: la suma de los na actual supera al na de algun proc que lo engloba.  
			mostrarMensaje("La suma de los na actual supera al na del proc: " + proc + ".");
		} 
		if(compilador.Compilador.naa < Integer.parseInt(sval)){
			//Error 2: na de proc x es mayor que el na del proc que lo contiene.
			mostrarMensaje("Na de proc: " + proc + " es mayor que el Na del proc que lo contiene.");
		} 
		//compilador.Compilador.naa = Integer.parseInt(sval); 
	}
	compilador.Compilador.naa = Integer.parseInt(sval);
}

boolean nameManglingNs(String sval) {
	
	int cantidadAnidamientos = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).cantidadAnidamientos();
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Recorro la lista con todos los id con ese nombre
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + ":Main")) {
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				char idProc = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().charAt(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().length()-1);
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).size(); j++){
					//Compruebo que el ambito del id del Proc este contenido
					String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
					String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).get(j).ambitoSinNombre();
					if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
						//Compruebo que el NS sea >= que la cantidad de anidamientos
						if(compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).get(j).getNs() >= cantidadAnidamientos)
							return true;
					}
				}
				return false;
			}
		}
	}
	return false;
}

int sePuedeUsar(String sval){
	//0 se puede usar
	//1 no esta al alcance.
	//2 esta redeclarada
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//Es una variable?
		if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var")){
			//No esta declarada?
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Veo si es un id que esta dentro del Proc para evaluar el NS
				if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).cantidadAnidamientos() > 0){
					if(nameManglingNs(sval))
						return 0;
					//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
					//else
					//	return 1;
				}
				//Tomo el ambito de la id no declarada y busco si hay una declarada al alcance.
				String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
				if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
					return 1;
				}
				else{
					//System.out.println("Tamño: " + compilador.Compilador.tablaSimbolo.get(sval).size());
					for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size()-1; i++){
						if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc")) {
							//System.out.println("Tabla: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
							if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
								return 0;
							}
						}
					}
				}
				//No existe una id declarada al alcance.
				return 1;	
			}
			//Si esta declarada ver que no este Redeclarada.
			String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
			if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
				return 0;
			}else{
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size()-1; i++){
					if(ambitoId.equals(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito())){
						return 2;
					}
				}
			}
			return 0;
		//Es un Proc?
		}
		else{
			//Tomo el ambito de la id de proc y veo que no este en el mismo ambito.
			String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
			if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
				return 0;
			}
			else{
				//System.out.println("Tamño: " + compilador.Compilador.tablaSimbolo.get(sval).size());
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size()-1; i++){
					//System.out.println("AmbitoId: " + ambitoId);
					//System.out.println("Tabla: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
					if(ambitoId.equals(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito())){
						return 2;
					}
				}
			}
			//No existe una id declarada en el mismo ambito.
			return 0;
		}
	}
	//Si no esta en la tabla de simbolos no existe ninguna declaracion.
	return 1;
}

void setearProc(String sval, String cantParametros, String na, String ns){

	if(na.charAt(0) >= '0' && na.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i")){
			na = na.toString().substring(0, na.length()-2); 
		}
	
	if(ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(ns.contains("_") && ns.contains("i")){
			ns = ns.toString().substring(0, ns.length()-2); 
		}

	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setCantParametros(Integer.parseInt(cantParametros));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNa(Integer.parseInt(na));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNs(Integer.parseInt(ns));
	
	if (na.equals(ns)) {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setTipo("NA_PROC");
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setTipo("NS_PROC");
	}
	else {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-1).setTipo("NA_PROC");
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setTipo("NS_PROC");
	}

}

void setearAmbitoNaNs(String na, String ns){

	if(na.charAt(0) >= '0' && na.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i")){
			na = na.toString().substring(0, na.length()-2); 
		}
	
	if(ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(ns.contains("_") && ns.contains("i")){
			ns = ns.toString().substring(0, ns.length()-2); 
		}

	if (na.equals(ns)) {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setAmbito(compilador.Compilador.ambito,false);
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setAmbito(compilador.Compilador.ambito,false);
	}
	else {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-1).setAmbito(compilador.Compilador.ambito,false);
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setAmbito(compilador.Compilador.ambito,false);
	}

}

void setearAmbito(String sval){
	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') {
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}
		else{
			Double flotante = Double.parseDouble(sval.replace('f', 'E'));
			if (sval.contains("f")) {
				if(String.valueOf(flotante).contains("E"))
					sval = String.valueOf(flotante).replace('E', 'f');
			}
			if (sval.contains("."))
				sval = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}		

	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
}

void setearAmbitoyDeclarada(String sval, String tipo){
	if(tipo.equals("")){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
	}
	else{
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("PARAM_PROC");
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipoParametro(tipo);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setPasajeParametro("COPIA VALOR");
	}
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////FIN DEFINICIONES PROPIAS////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
