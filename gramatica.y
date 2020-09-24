%{
package Compilador;
%}

%token CTE ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NI NA CADENA UP DOWN
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'

programa : PENDIENTE

bloqueSentencias : PENDIENTE

sentenciaEjecutables : PENDIENTE

bloqueSentencias : PENDIENTE

sentenciaControl  : IF '(' condicion ')' THEN bloqueSentencias ELSE bloqueSentencias END_IF
				  | IF '(' condicion ')' THEN bloqueSentencias END_IF
				  | PENDIENTE ALGUNOS ERRORES U OTRAS DECLARACIONES
				  ;

cicloFor : FOR '(' condicionFor ')' bloqueSentencias
		 | POSIBLES ERRORES U OTRAS DECLARACIONES
		 ;

condicionFor : inicioFor ';' comparacionFor ';' incDecFor identificador
			 | PENDIENTE ALGUNOS ERRORES U OTRAS DECLARACIONES
			 ;

inicioFor : identificador '=' constante

comparacionFor : identificador comparador identificador
			   | PENDIENTE ALGUNOS ERRORES U OTRAS DECLARACIONES
			   ;

incDecFor : UP
		  | DOWN
		  :

condicion : expresion comparador expresion  
		  | error expresion				  {yyerror("Error en la parte izquierda de la condición");}
		  | expresion error 			  {yyerror("Error en la parte derecha de la condición");}		  
;

comparador : '<='			{$$.obj = "<=";}															
		   | '>='			{$$.obj = ">=";}
		   | '<'			{$$.obj = "<";}
		   | '>'			{$$.obj = ">";}
		   | '!='			{$$.obj = "!=";}
		   ;

asignacion : identificador '==' expresion
		   | identificador error expresion  	{yyerror("Error en el operador de asignacion, se espera ==")}
		   ;

tipo : INTEGER
	 | FLOAT
	 | CADENA
	 ;

expresion : expresion '+' termino
		  | expresion '-' termino
		  | termino
		  ;

termino : termino '*' factor
		| termino '/' factor
		| factor
		;

factor  :   constante
		|   identificador
		;

identificador   :   ID  { 
                            Simbolo s = a.TablaSimbolo.get($1.sval);                                              
                            $$.obj = s;
                        }
				;

constante   :   CTE {
                        Simbolo s = a.TablaSimbolo.get($1.sval);
                        $$.obj = s;
                    }

			| '-' CTE   {
						    Simbolo s = verificarRango($2.sval);
							$$.obj = s;
						}
			;

%%

private void verificarRango(ParserVal num){
	System.out.println("El CTE NEGATIVO es "+num.t.toString());	
}

private void print(String string) throws IOException {
	System.out.println(string);
	//a.imprimirArchivo(string);
}