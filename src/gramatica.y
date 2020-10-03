%{
package Compilador;
%}

%token ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN
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

sentenciaDeclarativa : tipo listaVariables ';'        
					 | declaracionProcedimiento
					 | error listaVariables ';'      {yyerror("Error en la sentencia, tipo invalido");}
					 ;

listaVariables : listaVariables ',' identificador
			   | identificador
			   ;

declaracionProcedimiento : encabezadoProc bloqueProc {mostrarMensaje("Reconocio procedimiento completo");}
						 ;

encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' tipo ',' NS '=' tipo
			   | PROC identificador '(' ')' NA '=' tipo ',' NS '=' tipo
			   | PROC identificador '(' error ')' NA '=' tipo ',' NS '=' tipo {yyerror("Error en los parametros de procedimiento");}
			   ; 

parametrosProc : parametro
			   | parametro ',' parametro
			   | parametro ',' parametro ',' parametro

parametro : tipo identificador  {yyerror("Error en el parametro, tipo invalido");}
		  ;

bloqueProc : '{' bloque '}' {mostrarMensaje("Reconocio bloque de procedimiento");}
		   | '{' error '}'  {yyerror("Error en el cuerpo del procedimiento");}
		   ;

bloque : bloque sentenciaDeclarativa ';'
	   | bloque sentenciaEjecutable ';'
	   | sentenciaDeclarativa ';'
       | sentenciaEjecutable ';'
       ;

sentenciaEjecutable : asignacion
					| OUT '(' CADENA ')'                         
					| identificador '(' parametrosProc ')'
					| IF cuerpoIf END_IF 
					| cicloFor
					| OUT '(' error ')'  {yyerror("Error en la cadena");}
					| IF error END_IF    {yyerror("Error en el cuerpo del IF");}
					;

cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}' {mostrarMensaje("Reconocio ciclo FOR");}
         ;

condicionFor : inicioFor ';' condicion ';' incDec {mostrarMensaje("Reconocio condicion del FOR");}
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

cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}' ELSE {mostrarMensaje("Reconocio IF sin cuerpo en ELSE");}
				 ;

asignacion : identificador '=' expresion ';' {mostrarMensaje("Reconocio Asignacion");}
		   ;

expresion : expresion '+' termino {mostrarMensaje("Reconocio suma");}
		  | expresion '-' termino {mostrarMensaje("Reconocio resta");}
		  | termino               {mostrarMensaje("Reconocio termino");}
		  ;

termino : termino '*' factor {mostrarMensaje("Reconocio multiplicacion");}
		| termino '/' factor {mostrarMensaje("Reconocio division");}
		| factor             {mostrarMensaje("Reconocio factor");}
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

constante : INTEGER {mostrarMensaje("Reconocio constante entera");}
          ;

%%

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



