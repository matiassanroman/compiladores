%{
package compilador;
import java.io.IOException;
import java.util.ArrayList;
import accionesSemanticas.*;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
%}

%token ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN CTE
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'

%%
programa : bloquePrograma
{
	mostrarMensaje("Reconoce bien el programa");
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
	setearTipoParam($1.sval);
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
		yyerror($3.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
			   | identificador
{
	setearAmbitoyDeclarada($1.sval,"");
	if(sePuedeUsar($1.sval) == 2){
		yyerror($1.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc
{
	disminuirAmbito();
	if(!(compilador.Compilador.anidamientos.size() == 0)){
		verificacionNa();
		compilador.Compilador.anidamientos.remove(compilador.Compilador.anidamientos.size()-1);
		compilador.Compilador.anidamientosNombre.remove(compilador.Compilador.anidamientosNombre.size()-1);
		compilador.Compilador.anidamientosLinea.remove(compilador.Compilador.anidamientosLinea.size()-1);
	}
}
						 ;

encabezadoProc : | PROC identificador '(' ')'  NA '=' CTE ',' NS '=' CTE
{
	if(verficarNANSEnteras($7.sval, $11.sval)){
		setearProc($2.sval, "0", $7.sval, $11.sval);
		//setearAmbito($2.sval);
		PolacaInversa.subirNivelProc();
		//Par proc = new Par($1.sval+" "+$2.sval);
		Par proc =  new Par($1.sval+" "+ compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" + $2.sval;
		setearAmbitoNaNs($7.sval,$11.sval);
		if(sePuedeUsar($2.sval) == 2){
			//mostrarMensaje($2.sval + " esta Redeclarada.");
			yyerror($2.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add($2.sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa($7.sval,$2.sval);
	}
	else{
		//mostrarMensaje("NA o NS no es una CTE ENTERA");
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
				 | PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{
	
	//mostrarMensaje("Procedimiento con 1 parametro en linea nro: "+compilador.Compilador.nroLinea);
	if(verficarNANSEnteras($9.sval, $13.sval)){
		setearProc($2.sval, "1", $9.sval, $13.sval);
		//setearAmbito($2.sval);
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		//polaca.agregarParametro($1.sval+" "+$2.sval);
		//polaca.agregarParametro($5.sval);
		polaca.agregarParametro($1.sval+" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
				
		//Par proc = new Par($1.sval+" "+$2.sval);
		Par proc =  new Par($1.sval+" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  $2.sval;
		setearAmbitoNaNs($9.sval,$13.sval);
		if(sePuedeUsar($2.sval) == 2){
			//mostrarMensaje($2.sval + " esta Redeclarada.");
			yyerror($2.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add($2.sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa($9.sval,$2.sval);
		setearAmbitoyDeclarada($5.sval,$4.sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($5.sval).get(compilador.Compilador.tablaSimbolo.get($5.sval).size()-1).getAmbito());
	}
	else{
		//mostrarMensaje("NA o NS no es una CTE ENTERA");
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{

	//mostrarMensaje("Procedimiento con 2 parametros en linea nro: "+compilador.Compilador.nroLinea);
	if(verficarNANSEnteras($12.sval, $16.sval)){
		setearProc($2.sval, "2", $12.sval, $16.sval);
		//setearAmbito($2.sval);
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		//polaca.agregarParametro($1.sval+" "+$2.sval);
		//polaca.agregarParametro($5.sval);
		//polaca.agregarParametro($8.sval);
		polaca.agregarParametro($1.sval+" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		
		//Par proc = new Par($1.sval+" "+$2.sval);
		Par proc =  new Par($1.sval+" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  $2.sval;
		setearAmbitoNaNs($12.sval,$16.sval);
		if(sePuedeUsar($2.sval) == 2){
			//mostrarMensaje($2.sval + " esta Redeclarada.");
			yyerror($2.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add($2.sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa($12.sval,$2.sval);
		setearAmbitoyDeclarada($5.sval,$4.sval);
		setearAmbitoyDeclarada($8.sval,$7.sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($5.sval).get(compilador.Compilador.tablaSimbolo.get($5.sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($8.sval).get(compilador.Compilador.tablaSimbolo.get($8.sval).size()-1).getAmbito());
	}
	else{
		//mostrarMensaje("NA o NS no es una CTE ENTERA");
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE
{
	
	//mostrarMensaje("Procedimiento con 3 parametros en linea nro: "+compilador.Compilador.nroLinea);
	if(verficarNANSEnteras($15.sval, $19.sval)){
		setearProc($2.sval, "3", $15.sval, $19.sval);
		//setearAmbito($2.sval);
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		//polaca.agregarParametro($1.sval+" "+$2.sval);
		//polaca.agregarParametro($5.sval);
		//polaca.agregarParametro($8.sval);
		//polaca.agregarParametro($11.sval);
		polaca.agregarParametro($1.sval+" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		
		//Par proc = new Par($1.sval+" "+$2.sval);
		Par proc =  new Par($1.sval +" "+compilador.Compilador.tablaSimbolo.get($2.sval).get(compilador.Compilador.tablaSimbolo.get($2.sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" + $2.sval;
		setearAmbitoNaNs($15.sval,$19.sval);
		if(sePuedeUsar($2.sval) == 2){
			//mostrarMensaje($2.sval + " esta Redeclarada.");
			yyerror($2.sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add($2.sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa($15.sval,$2.sval);
		setearAmbitoyDeclarada($5.sval,$4.sval);
		setearAmbitoyDeclarada($8.sval,$7.sval);
		setearAmbitoyDeclarada($11.sval,$10.sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($5.sval).get(compilador.Compilador.tablaSimbolo.get($5.sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($8.sval).get(compilador.Compilador.tablaSimbolo.get($8.sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get($11.sval).get(compilador.Compilador.tablaSimbolo.get($11.sval).size()-1).getAmbito());
	}
	else{
		//mostrarMensaje("NA o NS no es una CTE ENTERA");
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
			     | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador error ')' NA '=' CTE ',' NS '=' CTE
{
	yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);
}
			   ; 

bloqueProc : '{' bloque '}'
{
	PolacaInversa.bajarNivelProc();
	//polaca.borrarProcYParametros();
	//int posProc = polaca.inicioProc();
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
	//mostrarMensaje("Sentencia OUT, en linea " + compilador.Compilador.nroLinea);
	setearAmbito($3.sval);
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
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(0);
	setearAmbito($1.sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),0);

	//Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + $1.sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(0);
	setearAmbito($1.sval);

	//Par nomProc = new Par($1.sval); 
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);

	//Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado
	int aux = sePuedeUsar($1.sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam($1.sval)){
			mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
			yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	*/

}
					| identificador '(' identificador ')' ';'
{
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(1);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),1);
	String aux2 = comprobarAlcance($3.sval); 
	
	//Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + $1.sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("")){
		yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),1), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(1);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("")){
		//mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado.");
		yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/
}
					| identificador '(' identificador ',' identificador ')' ';'
{

	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(2);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),2);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);

	//Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + $1.sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux3.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),2), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(2);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/

}
					| identificador '(' identificador ',' identificador ',' identificador ')' ';'
{
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(3);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	setearAmbito($7.sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),3);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);
	String aux4 = comprobarAlcance($7.sval);

	//Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + $1.sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("") || aux4.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux3.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux4.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $7.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3,aux4));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),3), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(3);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	setearAmbito($7.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);
	String aux4 = comprobarAlcance($7.sval);

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("") || aux4.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else if(aux3.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $7.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3,aux4));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/

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

					| identificador cuerpoIf
{
	yyerror("Error: las palabras reservadas van en mayuscula, en linea nro: "+ compilador.Compilador.nroLinea);
}

					| cicloFor
{
	//mostrarMensaje("Ciclo FOR en linea nro: " + compilador.Compilador.nroLinea);
}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}'
{
	if(!erroresFor()){
		polaca.borrarVariablesControl();
		Par pasoEnBlanco = new Par("");
		polaca.agregarPaso(pasoEnBlanco);
		polaca.agregarPasoIncompleto();
		Par pasoBI = new Par("BI");
		polaca.agregarPaso(pasoBI);
		//polaca.agregarLabel();
		polaca.completarFOR();
		polaca.borrarInicioFOR();
		polaca.borrarPasoIncompleto();
		polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());
		polaca.agregarLabel();
	}
}
         ;

condicionFor : inicioFor ';' condiFOR ';' incDec 
{
	if(!erroresFor()){
		polaca.borrarPasoPolaca();
	}
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
	setearAmbito($1.sval);
	String aux = comprobarAlcance($1.sval); 
	if(!aux.equals("")){
		if(verficarIDEnteras(aux) && verficarCTEEnteras($3.sval)){
			polaca.agregarVariableControl(aux);
			Par id = new Par(aux);
			polaca.agregarPaso(id);
			Par asig = new Par($2.sval);
			polaca.agregarPaso(asig);
			polaca.agregarInicioFOR();
			polaca.agregarLabel();
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarCTEEnteras($3.sval)){
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("La CTE de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else 
				yyerror("El identificador de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror("El identificador del inicio del for: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  ;

condicion : identificador comparador asignacion
{	
	erroresForAsignacion(compilador.Compilador.nroLinea);
	setearAmbito($1.sval);
	String aux = comprobarAlcance($1.sval); 
	if(!aux.equals("")){
		if(verficarIDEnteras(aux)){
			Par id = new Par(aux);
			Par comp = new Par($2.sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(comp);
		}
		else{
			yyerror("El identificador de la comparacion del for: " +$1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}	
	}
	else{
		yyerror("El identificador de la comparacion del for: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  | identificador comparador identificador
{
	
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	String aux = comprobarAlcance($1.sval); 
	String aux2 = comprobarAlcance($3.sval); 

	if(!aux.equals("") && !aux2.equals("")){
		if(verficarIDEnteras(aux) && verficarIDEnteras(aux2)){
			Par id1 =  new Par(aux);
			Par id2 =  new Par(aux2);
			Par comp = new Par($2.sval);
			polaca.agregarPaso(id1);
			polaca.agregarPaso(id2);
			polaca.agregarPaso(comp);
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarIDEnteras(aux2)){
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("El identificador de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else 
				yyerror("El identificador de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(aux.equals("") && aux2.equals("")){
			yyerror("El identificador de la comparacion del for: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
			yyerror("El identificador de la comparacion del for: " + $3.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else if(aux.equals(""))
			yyerror("El identificador de la comparacion del for: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("El identificador de la comparacion del for: " + $3.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  | identificador comparador constante
{
	setearAmbito($1.sval);
	String aux = comprobarAlcance($1.sval); 

	if(!aux.equals("")){
		if(verficarIDEnteras(aux) && verficarCTEEnteras($3.sval)){
			Par id =  new Par(aux);
			Par comp = new Par($2.sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(comp);
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarCTEEnteras($3.sval)){
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("La CTE de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + $1.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else
				yyerror("La CTE de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror("El identificador de la comparacion del for: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		if(!verficarCTEEnteras($3.sval))
			yyerror("La CTE de la comparacion del for: " + $3.sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  ;

incDec : UP constante   
{	
	if(verficarCTEEnteras($2.sval)){
		polaca.agregarVariableControl("+");
		polaca.agregarVariableControl($2.sval);
	}
	else
		yyerror("CTE del UP del for No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
}
	   | DOWN constante 
{
	if(verficarCTEEnteras($2.sval)){
		polaca.agregarVariableControl("-");
		polaca.agregarVariableControl($2.sval);
	}
	else
		yyerror("CTE del DOWN del for No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
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

//------------------------------------------------



condicionDelIf : identificador comparador asignacion
{	
	erroresIfAsignacion(compilador.Compilador.nroLinea);
	setearAmbito($1.sval);
	String aux = comprobarAlcance($1.sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		Par comp = new Par($2.sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(comp);	
	}
	else{
		yyerror("El identificador de la comparacion del if: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  | identificador comparador identificador
{	
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	String aux = comprobarAlcance($1.sval); 
	String aux2 = comprobarAlcance($3.sval); 

	if(!aux.equals("") && !aux2.equals("")){
		Par id1 =  new Par(aux);
		Par id2 =  new Par(aux2);
		Par comp = new Par($2.sval);
		polaca.agregarPaso(id1);
		polaca.agregarPaso(id2);
		polaca.agregarPaso(comp);
	}
	else{
		if(aux.equals("") && aux2.equals("")){
			yyerror("El identificador de la comparacion del if: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
			yyerror("El identificador de la comparacion del if: " + $3.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else if(aux.equals(""))
			yyerror("El identificador de la comparacion del if: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("El identificador de la comparacion del if: " + $3.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  | identificador comparador constante
{
	setearAmbito($1.sval);
	String aux = comprobarAlcance($1.sval); 

	if(!aux.equals("")){
		Par id =  new Par(aux);
		Par comp = new Par($2.sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(comp);
	}
	else{
		yyerror("El identificador de la comparacion del if: " + $1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
		  ;


//-----------------------------------------------

condicionIf : condicionDelIf
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
	String aux = comprobarAlcance($1.sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		Par asig = new Par($2.sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(asig);	

		String [] aux2 = aux.split("\\@");
		for(int j=0; j<compilador.Compilador.tablaSimbolo.get(aux2[0]).size(); j++){
			if(compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).getAmbito().equals(aux)) 
				if(compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).isDeclarada())
					compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).setLineaConv(compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}

	/*
	if(sePuedeUsar($1.sval) == 1){
		//mostrarMensaje($1.sval + " No esta declarada.");
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		//Par id =  new Par($1.sval);
		//Par id =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		//Par id =  new Par(getAmbitoVerdadero($1.sval));
		sePuedeUsar2($1.sval);
		Par id = new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par asig = new Par($2.sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(asig);
		compilador.Compilador.nroLineaConversion.add(String.valueOf(compilador.Compilador.nroLinea));
	}
	*/
}
		   | error '=' expresion
{
	yyerror("Error: identificador mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
		   | identificador '=' error
{
	yyerror("Error: asignacion mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);
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
	String aux = comprobarAlcance($1.sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		polaca.agregarPaso(id);
	}
	else{
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}

	/*
	if(sePuedeUsar($1.sval) == 1){
		//mostrarMensaje($1.sval + " No esta declarada.");
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		//Par id =  new Par($1.sval);
		//Par id =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		//Par id =  new Par(getAmbitoVerdadero($1.sval));
		sePuedeUsar2($1.sval);
		Par id = new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		polaca.agregarPaso(id);
	}
	*/
	
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
	double flotante;
	String result;
	if (valor.contains("_i"))
		result = valor.replace("_i", "");
	else {
		if (valor.contains("f")) {
			flotante = Double.parseDouble(valor.replace('f', 'E'));
			if(String.valueOf(flotante).contains("E"))
				result = String.valueOf(flotante).replace('f', 'E');
			else
				result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		else {
			flotante = Double.parseDouble(valor);
			result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}

	Par cte =  new Par(result);
	polaca.agregarPaso(cte);
	
	/*
	if (valor.contains("f")) {
		valor = valor.replace('f', 'E');
		valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
		valor = valor.replace('f', 'E');
	}
	*/
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
	double flotante;
	String result;
	if (valor.contains("_i"))
		result = valor.replace("_i", "");
	else {
		if (valor.contains("f")) {
			flotante = Double.parseDouble(valor.replace('f', 'E'));
			if(String.valueOf(flotante).contains("E"))
				result = String.valueOf(flotante).replace('f', 'E');
			else
				result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		else {
			flotante = Double.parseDouble(valor);
			result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}

	Par cte =  new Par('-'+result);
	polaca.agregarPaso(cte);

	/*
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	*/
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
static PolacaInversa polaca = new PolacaInversa();

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
				//mostrarMensaje("CTE FLOAT negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				//mostrarMensaje("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
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
				//mostrarMensaje("CTE ENTERA negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				//mostrarMensaje("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
	//ES POSITIVO	
	}else {
		// ES FLOAT Y POSTIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			if ( AS10_Verificar_Rango_Float.estaEnRango(sval) ){
				//mostrarMensaje("CTE FLOAT postiva esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				//mostrarMensaje("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
		// ES ENTERA Y POSITIVA
		else{
			if ( AS9_Verificar_Rango_Constante.estaEnRango(sval) ){
				//mostrarMensaje("CTE ENTERA postiva esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				//mostrarMensaje("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
		
	}
}

void disminuirAmbito(){
	String [] arreglo = compilador.Compilador.ambito.split("\\@"); 
	String aux = ""; 
	for(int i=0; i<arreglo.length-1; i++){
		if(i == 0)
			aux = arreglo[i];
		else
			aux = aux + "@" + arreglo[i]; 
	} 
	compilador.Compilador.ambito = aux;
}

void setearVerificacionNa(String sval, String proc){
	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') 
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}
	
	compilador.Compilador.anidamientos.add(Integer.parseInt(sval));
	//int tamano = compilador.Compilador.anidamientos.size();
	/*
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++)
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " tiene problemas con los NA de: " + compilador.Compilador.anidamientos.get(i).getValor() + " Error en linea: " + compilador.Compilador.nroLinea);
			}
	*/

	/*
	System.out.println("INICIO");
	for(int i=0; i<compilador.Compilador.anidamientos.size(); i++) {
		System.out.println("NOMBRE: " + compilador.Compilador.anidamientosNombre.get(i));
		System.out.println("NA: " + compilador.Compilador.anidamientos.get(i));
	}
	System.out.println("FIN");
	
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++){
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " Error en linea: " + compilador.Compilador.nroLinea);
				break;
			}
		}
	*/
		
}

void verificacionNa(){

	int tamano = compilador.Compilador.anidamientos.size();
	if(tamano > 1)
		if(compilador.Compilador.anidamientos.get(tamano-2) <= compilador.Compilador.anidamientos.get(tamano-1)){
			String msj1 = "Conflicto en los niveles de anidamientos de los procedimientos." + ".\n";
			String msj2 = "Descripcion: el proc con el nombre: " + compilador.Compilador.anidamientosNombre.get(tamano-2) + " de la linea: " + compilador.Compilador.anidamientosLinea.get(tamano-2) + " tiene un NA: " + compilador.Compilador.anidamientos.get(tamano-2) + " y es menor o igual que el proc con el nombre: " + compilador.Compilador.anidamientosNombre.get(tamano-1) + " que esta en la linea: " + compilador.Compilador.anidamientosLinea.get(tamano-1) + " y tiene un Na: " + compilador.Compilador.anidamientos.get(tamano-1) + ".";
			yyerror(msj1 + msj2);
		}
}

boolean nameManglingNs(String sval) {
	
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	//System.out.println("USOOO:" + ambitoId);
	//Recorro la lista con todos los id con ese nombre
	//for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
	for(int i=compilador.Compilador.tablaSimbolo.get(sval).size()-1; i>=0; i--) {
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main") && (compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())) {
			//System.out.println("ACAAAAAAAAAAAAAAAAAAA: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				String [] arreglo = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().split("\\@");
				String idProc = arreglo[arreglo.length-1];
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(idProc).size(); j++){
						//System.out.println("entro");
						//System.out.println("ID DENTRO DE PROC NO DECLARADOS: " + compilador.Compilador.tablaSimbolo.get(sval).get(j).getValor());
						//Compruebo que el ambito del id del Proc este contenido
						String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
						String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(idProc).get(j).ambitoSinNombre();
						//System.out.println("ambitoSinVar: " + compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito());
						//System.out.println("ambitoSinProc: " + compilador.Compilador.tablaSimbolo.get(idProc).get(j).getAmbito());
						//System.out.println("NSSSSS: " + compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs());
						if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
							//Compruebo que el NS sea >= que la cantidad de anidamientos
							String [] id = ambitoSinNombreVar.split("\\@"); 
							String [] proc = ambitoSinNombreProc.split("\\@"); 
							//System.out.println("TAMANO: " + (id.length - proc.length));
							if(compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs() >= ((id.length - proc.length)-1)) {
								return true;
							}		
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
	
	//Tomo el ambito de la id (asignacion)
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//Es una variable?
		if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var")){
			//No esta declarada?
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Veo si es un id que esta dentro del Proc para evaluar el NS
				if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(sval + "@Main")){
					if(nameManglingNs(sval))
						return 0;
				}
				//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
					//Compruebo que el id no sea proc y que el ambito sea Main
					if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main")) {
						if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())
							return 0;
					}
				}
				//No existe una id declarada al alcance.
				return 1;	
			}
			//Si esta declarada ver que no este Redeclarada.
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
			//Veo si el id de Proc no esta declarado para buscar si existe en el ambito
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Recorro la lista de id de proc
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
					//Busco que esos id esten declarados
					if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada() && compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc")){
						String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
						String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
						//Pregunto si tienen el mismo ambito
						if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
							return 0;
						}
						//No se admite recursion
						String [] recurAux = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre().split("\\@");
						if(sval.equals(recurAux[recurAux.length-1])){
							//mostrarMensaje("No se permite recursion.");
							//yyerror("El Proc: " + sval + " intenta hacer recursion y no esta permitido. Error en linea: " + compilador.Compilador.nroLinea);
						}
						//Esta al alcance?
						if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
							return 0;							
						}
					}
				}
				return 1;
			}	
			else {
				//Si esta declarada ver que no este Redeclarada
				if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
						return 0;
				}else{
					for(int j=0; j<compilador.Compilador.tablaSimbolo.get(sval).size()-1; j++){
						if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(compilador.Compilador.tablaSimbolo.get(sval).get(j).getAmbito())){
							return 2;
						}
					}
				}
				return 0;	
			}			
		}
	}
	//Si no esta en la tabla de simbolos no existe ninguna declaracion.
	return 1;

}

void setearTipoParam(String sval){

	//Se setean todos las variables que son declaradas con su tipo
	Set<String> keys = c.getTablaSimbolo().keySet();
	Iterator<String> itr = keys.iterator();
	String str;
	
	while (itr.hasNext()) { 
		str = itr.next();
		Simbolo s = compilador.Compilador.tablaSimbolo.get(str).get(compilador.Compilador.tablaSimbolo.get(str).size()-1);

		if(s.getTipo().equals("Var") && s.isDeclarada() && s.getTipoParametro() == null)
			compilador.Compilador.tablaSimbolo.get(str).get(compilador.Compilador.tablaSimbolo.get(str).size()-1).setTipoParametro(sval);
	}

}
/*
boolean verificarParamFormales(String sval, String proc){
	//Verifico que el parametro real que venga este al alcance, declarado y que sea del mismo tipo que el de la definicion (parametro formal)
	if(sePuedeUsar(sval) == 0) {
		String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(proc).get(compilador.Compilador.tablaSimbolo.get(proc).size()-1).ambitoSinNombre();
		//Busco el proc declarado
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(proc).size(); i++) {
			if(compilador.Compilador.tablaSimbolo.get(proc).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(proc).get(i).isDeclarada()) {
				String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
				//Pregunto si tienen el mismo ambito
				if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
					return true;
				}
				//Esta al alcance?
				else if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
					return true;							
				}
			}		
		}
	}
	else
		return false;
}
*/


boolean verificarCantParam(String sval){

	int cantLlamdor = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getCantParametros();
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Busco que esos id esten declarados y sea Proc
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada()){
			String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
			String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
			//Pregunto si tienen el mismo ambito
			if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
				if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getCantParametros() == cantLlamdor)
					return true;
			}
			//Esta al alcance?
			if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
				if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getCantParametros() == cantLlamdor)
					return true;							
			}
		}
	}
	return false;
}


boolean verficarCTEEnteras(String cte){
	
	if(cte.charAt(0) >= '0' && cte.charAt(0) <= '9') 
		if(cte.contains("_") && cte.contains("i")){
			return true;
		}

	return false;

}

boolean verficarNANSEnteras(String na, String ns){
	
	if(na.charAt(0) >= '0' && na.charAt(0) <= '9' && ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i") && ns.contains("_") && ns.contains("i")){
			return true;
		}

	return false;

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
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setCantParametros(Integer.parseInt(cantParametros));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNa(Integer.parseInt(na));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNs(Integer.parseInt(ns));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);

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
		else if(sval.contains("f") || sval.contains(".")){
			Double flotante = Double.parseDouble(sval.replace('f', 'E'));
			if (sval.contains("f")) {
				if(String.valueOf(flotante).contains("E"))
					sval = String.valueOf(flotante).replace('E', 'f');
			}
			if (sval.contains("."))
				sval = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Proc") && !(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada())){
		/*
		for(int i=compilador.Compilador.tablaSimbolo.get(sval).size()-1; i>=0; i--){
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada()){
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
				break;
			}
		}*/
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito();
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var") && !(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada())){
		//compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito();
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getUso().equals("CADENA")){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
	}	

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

String getAmbitoVerdaderoVerdadero(String sval) {
	
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Recorro la lista con todos los id con ese nombre
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main") && (compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())) {
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				String [] arreglo = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().split("\\@");
				String idProc = arreglo[arreglo.length-1];
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(idProc).size(); j++){
					//Compruebo que el ambito del id del Proc este contenido
					String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
					String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(idProc).get(j).ambitoSinNombre();
					if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
						//Compruebo que el NS sea >= que la cantidad de anidamientos
						String [] id = ambitoSinNombreVar.split("\\@"); 
						String [] proc = ambitoSinNombreProc.split("\\@"); 
						if(compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs() >= ((id.length - proc.length)-1)) {
							return compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito();
						}
							
					}
				}
				return "";
			}
		}
	}
	return "";
}

String getAmbitoVerdadero(String sval){
	
	//Tomo el ambito de la id (asignacion)
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//No esta declarada?
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
			//Veo si es un id que esta dentro del Proc para evaluar el NS
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(sval + "@Main")){
				if(!getAmbitoVerdaderoVerdadero(sval).equals(""))
					return getAmbitoVerdaderoVerdadero(sval);
			}
			//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
			for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
				//Compruebo que el id no sea proc y que el ambito sea Main
				if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main")) {
					if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())
						return compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito();
				}
			}	
		}
	}
	return "";
}

String getAmbitoProc(String ambitoGeneral) {
	String [] arreglo = ambitoGeneral.split("\\@");
	String aux = arreglo[arreglo.length-1];
	//System.out.println("AUX: " + aux);
	for(int i=0; i<arreglo.length-1; i++)
		aux = aux + "@" + arreglo[i];
	//System.out.println("AUX 2: " + aux);
	return aux;	
}

int getNsProc(String ambitoProc, String sval) {
	String [] arreglo = ambitoProc.split("\\@");
	String aux = sval;
	for(int j=0; j<arreglo.length-1; j++)
		aux = aux + "@" + arreglo[j];
	
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(aux))
			return compilador.Compilador.tablaSimbolo.get(sval).get(i).getNs();
	}
	return 0;
}

int getNivelVar(String sval) {
	String [] arreglo = sval.split("\\@");
	return arreglo.length-1;
}

boolean dadoProcVerDeclaracionVar(String ambitoProc, String sval) {
	String [] ambitoProcedimiento = ambitoProc.split("\\@");
	String ambitoVar = sval;
	if(ambitoProcedimiento.length > 1) {
		for(int i=1; i<ambitoProcedimiento.length; i++) {
			ambitoVar =  ambitoVar + "@" + ambitoProcedimiento[i];
		}
		ambitoVar = ambitoVar + "@" + ambitoProcedimiento[0];
	}
	else {
		ambitoVar = ambitoVar + "@" + ambitoProc;
	}
	
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(ambitoVar))
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada() && (compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Var") || compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("PARAM_PROC")))
				return true;				
	}
	return false;				
}

String construirAmbito (String[] array) {
	String delimiter = "@";
    return String.join(delimiter, array);
}

String[] construirAmbitoMenosUltimo (String[] array) {
	String aux = "";
	for(int j=0; j<array.length-1; j++)
		if(j==0)
			aux = array[j];
		else
			aux = aux + "@" + array[j];
	return aux.split("\\@");
	
}

String construirAmbitoMenosUltimoString (String array2) {
	String [] array = array2.split("\\@");
	String aux = "";
	for(int j=0; j<array.length-1; j++)
		if(j==0)
			aux = array[j];
		else
			aux = aux + "@" + array[j];
	String delimiter = "@";
    return String.join(delimiter, aux);
	
}

boolean verificarAnidamientos(String ambitoProc, String ambitoVar, int ns) {	
	String proc = "";
	String [] aux = ambitoProc.split("\\@");
	for(int i=1; i<aux.length; i++)
		if(i==1)
			proc = aux[i];
		else
			proc = proc + "@" + aux[i];
	
	String var = "";
	String [] aux2 = ambitoVar.split("\\@");
	for(int i=1; i<aux2.length; i++)
		if(i==1)
			var = aux2[i];
		else
			var = var + "@" + aux2[i];
	
	//En proc tengo f2@main@f1 => main@f1@f2
	//Nivel son las veces que me voy metiendo hasta encontrar donde esta declarado
	//var es el ambito de la variable ax1@main@f1@f2 => main@f1@f2
	proc = proc + "@" + aux[0];
	int nivel = 0;
	
	while(var.length() > 0) {
		if(var.equals(proc)) {
			if(ns >= nivel) {
				return true;
			}
			else {
				return false;
			}
		}
		var = construirAmbitoMenosUltimoString(var);
		nivel++;
	}
	
	return false;
}

public String ambitoSinNombre(String sval) {
	String [] arreglo = sval.split("\\@");
	String auxSinNombre = "";
	boolean primero = true;
	for(int z=1; z<arreglo.length; z++) {
		if(primero) {
			primero = false;
			auxSinNombre = arreglo[z];
		}
		else
			auxSinNombre = auxSinNombre + "@" + arreglo[z];
	}
	return auxSinNombre;
}

/*
boolean verificarCantParam(String alcanceProc, int cantParam){

	//sval llamado con su ambito => b@Main@a@b
	String [] nombreProc = alcanceProc.split("\\@");

	for(int i=compilador.Compilador.tablaSimbolo.get(nombreProc[0]).size()-1; i>=0; i--){
		if((compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).isDeclarada()) && (compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getTipo().equals("Proc")))
			if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito().equals(alcanceProc))
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return true;
	}
	return false;
}
*/

String comprobarAlcanceProc(String sval, int cantParam){

	//sval llamador con su ambito => b@Main@a@b
	String [] nombreProc = sval.split("\\@");
	String ambitoSinNombreLlamador = ambitoSinNombre(sval);
	for(int i=compilador.Compilador.tablaSimbolo.get(nombreProc[0]).size()-1; i>=0; i--){
		//Si es un Proc declarado
		if((compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).isDeclarada()) && (compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getTipo().equals("Proc"))){
			//Caso Hermano: llamador esta en el mismo ambito que el llamado.
			if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito().equals(sval)){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return sval;
			}
			//Caso Recursivo: llamador esta dentro del llamado.
			else if(sval.equals(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito()+nombreProc[0])){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito();
			}
			String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).ambitoSinNombre();
			//Esta al alcance?
			if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito();							
			}
		}
	}
	return "";	
}

String comprobarAlcance(String sval) {
	String ambito = "Main" + compilador.Compilador.ambito;
	String [] ambitoAux = ambito.split("\\@");
	String ambitoUsoVar = sval + "@" + ambito;
	String [] ambitoArr = ambito.split("\\@");
	boolean primero = true;
	for(int i = ambitoArr.length-1; i>=0; i--) {
		if(!ambitoArr[i].equals("Main")) {
			if(primero) {
				primero = false;
				if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
					//System.out.println("SALIDA 1: " + sval + "@" + construirAmbito(ambitoAux));
					return sval + "@" + construirAmbito(ambitoAux);
				}
					
			}
			else {
				if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
					if(verificarAnidamientos(getAmbitoProc(construirAmbito(ambitoAux)), ambitoUsoVar,getNsProc(construirAmbito(ambitoAux),ambitoArr[i]))) {
						//System.out.println("SALIDA 2: " + sval + "@" + construirAmbito(ambitoAux));
						return sval + "@" + construirAmbito(ambitoAux);
					}
				}
			}
		}
		else {
			if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
				//System.out.println("SALIDA 3: " + sval + "@" + construirAmbito(ambitoAux));
				return sval + "@" + construirAmbito(ambitoAux);
			}
		}
		
		ambitoAux = construirAmbitoMenosUltimo(ambitoAux);
	}
	return "";		
}

boolean verficarIDEnteras(String id){
	
	String [] var = id.split("\\@");
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(var[0]).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getAmbito().equals(id))
			if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getTipoParametro() != null)
				if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getTipoParametro().equals("INTEGER"))
					return true;
	}

	return false;

}

boolean erroresFor(){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains("for"))
			return true;

	return false;
}

void erroresForAsignacion(int linea){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains(String.valueOf(linea)))
			this.errores.set(i,"El id de la comparacion del for: " + this.errores.get(i));
}

void erroresIfAsignacion(int linea){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains(String.valueOf(linea)))
			this.errores.set(i,"El id de la comparacion del if: " + this.errores.get(i));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////FIN DEFINICIONES PROPIAS////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
