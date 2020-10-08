%{
package Compilador;
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

listaVariables : listaVariables ',' identificador
			   | identificador
			   | error    {yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc {mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea);}
						 ;

encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' CTE ',' NS '=' CTE {mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea);}
			   | PROC identificador '(' ')' NA '=' CTE ',' NS '=' CTE                {mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea);}
			   | PROC identificador '(' error ')' NA '=' CTE ',' NS '=' CTE {yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
			   ; 

parametrosProc : parametro
			   | parametro ',' parametro
			   | parametro ',' parametro ',' parametro
			   ;

parametro : tipo identificador  {mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);}
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
					| identificador '(' ')' ';' {mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);}
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

asignacion : identificador '=' expresion ';' {mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);}
		   ;

expresion : expresion '+' termino {mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea);}
		  | expresion '-' termino {mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);}
		  | termino               
		  ;

termino : termino '*' factor {mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea);}
		| termino '/' factor {mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea);}
		| factor             
		;

factor : constante
	   | identificador
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

identificador : ID {mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);}
			  ;

constante : CTE     {mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);}
		  | '-' CTE {mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);}
          ;

%%

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



