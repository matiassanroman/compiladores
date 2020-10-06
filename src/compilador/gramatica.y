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

sentenciaDeclarativa : tipo listaVariables ';'       {mostrarMensaje("Reconocio declaracion de una o mas variables");}
					 | declaracionProcedimiento
					 | error listaVariables ';'      {yyerror("Error en la sentencia, tipo invalido");}
					 ;

listaVariables : listaVariables ',' identificador
			   | identificador
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc {mostrarMensaje("Reconocio procedimiento completo");}
						 ;

encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' tipo ',' NS '=' tipo {mostrarMensaje("Reconocio PROC con parametros");}
			   | PROC identificador '(' ')' NA '=' tipo ',' NS '=' tipo                {mostrarMensaje("Reconocio PROC sin parametros");}
			   | PROC identificador '(' error ')' NA '=' tipo ',' NS '=' tipo {yyerror("Error en los parametros de procedimiento");}
			   ; 

parametrosProc : parametro
			   | parametro ',' parametro
			   | parametro ',' parametro ',' parametro
			   ;

parametro : tipo identificador  {mostrarMensaje("Reconocio parametro");}
		  ;

bloqueProc : '{' bloque '}' {mostrarMensaje("Reconocio bloque de procedimiento");}
		   | '{' error '}'  {yyerror("Error en el cuerpo del procedimiento");}
		   ;

bloque : bloque sentenciaDeclarativa
	   | bloque sentenciaEjecutable 
	   | sentenciaDeclarativa 
       | sentenciaEjecutable 
       ;

sentenciaEjecutable : asignacion
					| OUT '(' CADENA ')' ';' {mostrarMensaje("Reconocio OUT CADENA");}                      
					| identificador '(' parametrosProc ')' ';' {mostrarMensaje("Reconocio llamda a procedimiento");}
					| IF cuerpoIf END_IF 
					| cicloFor {mostrarMensaje("Reconocio ciclo FOR");}
					| OUT '(' error ')' ';'  {yyerror("Error en la cadena");}
					| IF error END_IF    {yyerror("Error en el cuerpo del IF");}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}' 
         ;

condicionFor : inicioFor ';' condicion ';' incDec {mostrarMensaje("Reconocio encabezado del FOR");}
			 ;

inicioFor : identificador '=' constante
		  ;

condicion : identificador comparador asignacion
		  | identificador comparador identificador
		  | identificador comparador constante
		  ;

incDec : UP constante   {mostrarMensaje("Reconocio incremento-UP del FOR");}
	   | DOWN constante {mostrarMensaje("Reconocio decremento-UP del FOR");}
	   ;

bloqueSentencia : bloqueSentencia sentenciaEjecutable
				| sentenciaEjecutable
				;

cuerpoIf : cuerpoCompleto
		 | cuerpoIncompleto 
		 ;
		 
cuerpoCompleto : '(' condicion ')' '{' bloqueSentencia '}' ELSE '{' bloqueSentencia '}'	{mostrarMensaje("Reconocio IF con cuerpo en ELSE");}		   	  
			   ; 

cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}' {mostrarMensaje("Reconocio IF sin cuerpo en ELSE");}
				 ;

asignacion : identificador '=' expresion ';' {mostrarMensaje("Reconocio Asignacion");}
		   ;

expresion : expresion '+' termino {mostrarMensaje("Reconocio suma");}
		  | expresion '-' termino {mostrarMensaje("Reconocio resta");}
		  | termino               
		  ;

termino : termino '*' factor {mostrarMensaje("Reconocio multiplicacion");}
		| termino '/' factor {mostrarMensaje("Reconocio division");}
		| factor             
		;

factor : constante
	   | identificador
	   ;

comparador : '<=' {mostrarMensaje("Reconocio comparador menor-igual");}
		   | '>=' {mostrarMensaje("Reconocio comparador mayor-igual");}
		   | '!=' {mostrarMensaje("Reconocio comparador distinto");}
		   | '==' {mostrarMensaje("Reconocio comparador igual");}
		   ;

tipo : FLOAT   {mostrarMensaje("Reconocio tipo FLOAT");}
     | INTEGER {mostrarMensaje("Reconocio tipo INTEGER");}
     ;

identificador : ID {mostrarMensaje("Reconocio identificador");}
			  ;

constante : CTE     {mostrarMensaje("Reconocio constante");}
		  | '-' CTE {mostrarMensaje("Reconocio constante negativa");}
          ;

%%

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}


