%{
package compilador;
import java.io.IOException;
import java.util.ArrayList;
%}

%token ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN CTE
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'

%%
programa : bloquePrograma {mostrarMensaje("Reconoce bien el programa");
System.out.println(polaca.toString());}
		 ;

bloquePrograma : bloquePrograma sentenciaDeclarativa
			   | bloquePrograma sentenciaEjecutable
			   | sentenciaDeclarativa
               | sentenciaEjecutable 
               ;

sentenciaDeclarativa : tipo listaVariables ';'       {mostrarMensaje("Reconocio declaracion de una o mas variables en linea nro: "+compilador.Compilador.nroLinea);}
					 | declaracionProcedimiento		
					 | error listaVariables ';'      {yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
					 ;

listaVariables : listaVariables ',' identificador { setearAmbitoyDeclarada($3.sval); if(sePuedeUsar($3.sval) == 2){mostrarMensaje($3.sval + " esta Redeclarada.");} }
			   | identificador                    { setearAmbitoyDeclarada($1.sval); if(sePuedeUsar($1.sval) == 2){mostrarMensaje($1.sval + " esta Redeclarada.");} }
			   | error    {yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc {mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea); disminuirAmbito(); compilador.Compilador.na = compilador.Compilador.na + compilador.Compilador.naa;  }
						 ;

encabezadoProc : PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 													{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($9.sval);  setearAmbito($13.sval); setearAmbitoyDeclarada($5.sval); }
			   | PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 							{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($12.sval); setearAmbito($16.sval); setearAmbitoyDeclarada($5.sval); setearAmbitoyDeclarada($8.sval); }
			   | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 	    {mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($15.sval); setearAmbito($19.sval); setearAmbitoyDeclarada($5.sval); setearAmbitoyDeclarada($8.sval); setearAmbitoyDeclarada($11.sval); }
			   | PROC identificador '(' ')'  NA '=' CTE ',' NS '=' CTE                                                                      {mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($7.sval);  setearAmbito($11.sval); verificarNa($7.sval,$2.sval); }
			   | PROC identificador '(' error ')' NA '=' CTE ',' NS '=' CTE                                                                 {yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
			   ; 

parametrosProc : parametro
			   | parametro ',' parametro
			   | parametro ',' parametro ',' parametro
			   ;

parametro : tipo identificador  {mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);  setearAmbito($2.sval); }
		  | error identificador {yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
		  ;

bloqueProc : '{' bloque '}' {mostrarMensaje("Reconocio bloque de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
		   | '{' error '}'  {yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
		   ;

bloque : bloque sentenciaDeclarativa
	   | bloque sentenciaEjecutable 
	   | sentenciaDeclarativa 
       | sentenciaEjecutable
       ;

sentenciaEjecutable : asignacion
					| OUT '(' CADENA ')' ';' {mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}                     
					| identificador '(' parametrosProc ')' ';' {mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
					| identificador '(' ')' ';' {mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea); }
					| IF cuerpoIf END_IF { 	if (PolacaInversa.getFlagITE()){
												polaca.completarPolaca(PolacaInversa.getRetrocesosITE());
											}
											else
												polaca.completarPolaca(PolacaInversa.getRetrocesosIT());
											}
					| cicloFor {mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
					| OUT '(' error ')' ';'  {yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
					| IF error END_IF    {yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}' {polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());}
		 | FOR '(' error ')' '{' bloqueSentencia '}'     {yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
		 | FOR '(' condicionFor ')' '{' error '}'        {yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
         ;

condicionFor : inicioFor ';' condiFOR ';' incDec {mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);
polaca.borrarPasoPolaca();}
			 ;

condiFOR : condicion {Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);}
		;

inicioFor : identificador '=' constante { Par id = new Par($1.sval);
polaca.agregarPaso(id);Par asig = new Par($2.sval);
polaca.agregarPaso(asig);}
		  ;

condicion : identificador comparador asignacion {
			Par id = new Par($1.sval);Par comp = new Par($2.sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp);  }
		  | identificador comparador identificador{
			Par id1 = new Par($1.sval); Par id2 = new Par($3.sval); Par comp = new Par($2.sval);
			polaca.agregarPaso(id1); polaca.agregarPaso(id2); polaca.agregarPaso(comp); }
		  | identificador comparador constante{
			Par id = new Par($1.sval); Par comp = new Par($2.sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp); }
		  ;

incDec : UP constante   {mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);
	//Par mas = new Par("+"); 
	//polaca.agregarPaso(mas);
	}
	   | DOWN constante {mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);
	   //Par menos = new Par("-"); 
	   //polaca.agregarPaso(menos);
	   }
	   ;

bloqueSentencia : bloqueSentencia sentenciaEjecutable
				| sentenciaEjecutable
				;

cuerpoIf : cuerpoCompleto {	PolacaInversa.setFlagITE(true); }
		 | cuerpoIncompleto { PolacaInversa.setFlagITE(false); 
		 polaca.borrarPasoPolaca();
		 polaca.borrarPasoPolaca();
		 polaca.borrarPasoIncompleto();}
		 ;
		 
cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE '{' bloqueElse '}'	{mostrarMensaje("Reconocio IF con ELSE en linea nro: "+compilador.Compilador.nroLinea);}		   	  
			   ;  

condicionIf : condicion {
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);
				}
			;

bloqueThen : bloqueSentencia {Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);}
		   ;

bloqueElse : bloqueSentencia
		   ;

cuerpoIncompleto : '(' condicionIf ')' '{' bloqueThen '}'  {mostrarMensaje("Reconocio IF sin ELSE en linea nro: "+compilador.Compilador.nroLinea);}


asignacion : identificador '=' expresion ';' {mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);
            setearAmbito($1.sval); if(sePuedeUsar($1.sval) == 1){mostrarMensaje($1.sval + " No esta declarada.");}
			Par id =  new Par($1.sval);
			Par asig = new Par($2.sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(asig); }
		   ;

expresion : expresion '+' termino {mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea); 
			Par suma =  new Par("+");
			polaca.agregarPaso(suma);  }
		  | expresion '-' termino {mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);
		    						Par resta =  new Par("-");
									polaca.agregarPaso(resta); }
		  | termino               
		  ;

termino : termino '*' factor {mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea); 
							  Par multi =  new Par("*");
							  polaca.agregarPaso(multi);		}
		| termino '/' factor {mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea); 
							  Par division =  new Par("/");
							  polaca.agregarPaso(division);}
		| factor          
		;

factor : constante
	   | identificador { setearAmbito($1.sval); if(sePuedeUsar($1.sval) == 1){mostrarMensaje($1.sval + " No esta declarada.");}
                         Par id =  new Par($1.sval);
					     polaca.agregarPaso(id); } 
	   ;

comparador : '<=' {mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
		   | '>=' {mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
		   | '!=' {mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
		   | '==' {mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
		   | '>'  {mostrarMensaje("Reconocio comparador mayor en linea nro: "+compilador.Compilador.nroLinea);}
		   | '<'  {mostrarMensaje("Reconocio comparador menor en linea nro: "+compilador.Compilador.nroLinea);}
		   ;

tipo : FLOAT   {mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
     | INTEGER {mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
     ;

identificador : ID {mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);
					            }
			  ;

constante : CTE     {mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);
                     setearAmbito($1.sval);
					 Par cte =  new Par($1.sval);
					 polaca.agregarPaso(cte);            }
		  | '-' CTE {mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);  
                     setearAmbito($2.sval);
		  			 Par cte =  new Par("-"+$1.sval);
					 polaca.agregarPaso(cte);            }
          ;

%%

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
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

	if(compilador.Compilador.primero){
		compilador.Compilador.na = Integer.parseInt(sval); 
		compilador.Compilador.primero = false; 
		compilador.Compilador.naa = Integer.parseInt(sval);
	}else{
		compilador.Compilador.na = compilador.Compilador.na - Integer.parseInt(sval); 
		if(compilador.Compilador.na < 0){
			//Error 1: la suma de los na actual supera al na de algun proc que lo engloba.  
			mostrarMensaje("La suma de los na actual supera al na de algun proc que lo engloba.");
		} 
		if(compilador.Compilador.naa < Integer.parseInt(sval)){
			//Error 2: na de proc x es mayor que el na del proc que lo contiene.
			mostrarMensaje("Na de proc: " + proc + " es mayor que el Na del proc que lo contiene.");
		} 
		compilador.Compilador.naa = Integer.parseInt(sval); 
	}
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
				//Tomo el ambito de la id no declarada y busco si hay una declarada al alcance.
				String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
				if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
					return 1;
				}else{
					//System.out.println("Tamño: " + compilador.Compilador.tablaSimbolo.get(sval).size());
					for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size()-1; i++){
						//System.out.println("AmbitoId: " + ambitoId);
						//System.out.println("Tabla: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
						if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
							return 0;
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
		}else{
			//Tomo el ambito de la id de proc y veo que no este en el mismo ambito.
			String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
			if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
				return 0;
			}else{
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

void setearProc(String sval){
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("Proc");
}

void setearAmbito(String sval){
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
}

void setearAmbitoyDeclarada(String sval){
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
}