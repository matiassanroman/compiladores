%{
package compilador;
%}

%token ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN CTE
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'

%%
programa : bloquePrograma {mostrarMensaje("Reconoce bien el programa");}
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

declaracionProcedimiento : encabezadoProc bloqueProc {mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea); disminuirAmbito();}
						 ;

encabezadoProc : PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 													{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($9.sval);  setearAmbito($13.sval); setearAmbitoyDeclarada($5.sval); }
			   | PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 							{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($12.sval); setearAmbito($16.sval); setearAmbitoyDeclarada($5.sval); setearAmbitoyDeclarada($8.sval); }
			   | PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE 	    {mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($15.sval); setearAmbito($19.sval); setearAmbitoyDeclarada($5.sval); setearAmbitoyDeclarada($8.sval); setearAmbitoyDeclarada($11.sval); }
			   | PROC identificador '(' ')'  NA '=' CTE ',' NS '=' CTE                                                                      {mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc($2.sval); setearAmbito($2.sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  $2.sval; if(sePuedeUsar($2.sval) == 2){mostrarMensaje($2.sval + " esta Redeclarada.");} setearAmbito($7.sval);  setearAmbito($11.sval); }
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
					| IF cuerpoIf END_IF   
					| cicloFor {mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
					| OUT '(' error ')' ';'  {yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
					| IF error END_IF    {yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}' 
		 | FOR '(' error ')' '{' bloqueSentencia '}'     {yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
		 | FOR '(' condicionFor ')' '{' error '}'        {yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
         ;

condicionFor : inicioFor ';' condicion ';' incDec {mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
			 ;

inicioFor : identificador '=' constante
		  ;

condicion : identificador comparador asignacion
		  | identificador comparador identificador
		  | identificador comparador constante
		  ;

incDec : UP constante   {mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
	   | DOWN constante {mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
	   ;

bloqueSentencia : bloqueSentencia sentenciaEjecutable
				| sentenciaEjecutable
				;

cuerpoIf : cuerpoCompleto
		 | cuerpoIncompleto 
		 ;
		 
cuerpoCompleto : '(' condicion ')' '{' bloqueSentencia '}' ELSE '{' bloqueSentencia '}'	{mostrarMensaje("Reconocio IF con cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}		   	  
			   ; 

cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}' {mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
				 ;

asignacion : identificador '=' expresion ';' {mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea); setearAmbito($1.sval); if(sePuedeUsar($1.sval) == 1){mostrarMensaje($1.sval + " No esta declarada.");} }
		   ;

expresion : expresion '+' termino {mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea); }
		  | expresion '-' termino {mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea); }
		  | termino               
		  ;

termino : termino '*' factor {mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea); }
		| termino '/' factor {mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea); }
		| factor             
		;

factor : constante
	   | identificador { setearAmbito($1.sval); if(sePuedeUsar($1.sval) == 1){mostrarMensaje($1.sval + " No esta declarada.");} }
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

identificador : ID {mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea); }
			  ;

constante : CTE     {mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea); setearAmbito($1.sval); }
		  | '-' CTE {mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea); setearAmbito($1.sval); }
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


