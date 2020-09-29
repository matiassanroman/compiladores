%{
package Compilador;
%}

%token CTE ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INTEGER FLOAT PROC NS NA CADENA UP DOWN
%token '<=' '>=' '!=' '==' 

%left '+' '-'
%left '*' '/'


%%
programa : bloquePrograma {imprimir("Reconoce Programa");}
		 ;

bloquePrograma : bloquePrograma sentenciaDeclarativa
			   | bloquePrograma sentenciaEjecutables
			   | sentenciaDeclarativa
		       | sentenciaEjecutables
		       ;

sentenciaDeclarativa : PROC ID '(' listaParametros  ')' NA '=' constante ',' NS '=' constante bloqueSentencias  {imprimir("Reconocio declaracion de procedimiento");} 
					 ;

sentenciaEjecutables : sentenciaIf {imprimir("Reconoce If");}
					 | asignacion  {imprimir("Reconoce Asignacion");}
					 | cicloFor    {imprimir("Reconoce For");}
					 | OUT '(' CADENA ')'  {imprimir("Reconoce Out");}
					 | identificador '(' listaParametros ')' 
					 ;

listaParametros : listaParametros ',' identificador {imprimir("Reconoce ListaParametros");}
				| identificador
				;

bloqueSentencias : '{' listaSentencias '}' {imprimir("Reconoce bloqueSentencias");}
				 ;

listaSentencias : listaSentencias asignacion  {imprimir("Reconoce Lista sentencias asignacion");}
				| listaSentencias cicloFor    {imprimir("Reconoce Lista sentencias CicloFor");}
				| listaSentencias sentenciaIf {imprimir("Reconoce Lista sentencias SentenciaIf");}
				| sentenciaIf
				| cicloFor
				| asignacion
				;
				
sentenciaIf  : IF '(' condicion ')' THEN bloqueSentencias ELSE bloqueSentencias END_IF {imprimir("Reconoce If completo");}
			 | IF '(' condicion ')' THEN bloqueSentencias END_IF                       {imprimir("Reconoce If parcial");}
			 ;

cicloFor : FOR '(' condicionFor ')' bloqueSentencias  {imprimir("Reconoce cilcoFor");}
		 ;

condicionFor : inicioFor ';' comparacionFor ';' incDecFor identificador {imprimir("Reconoce condicionesDelFor");}
			 ;

inicioFor : identificador '=' constante   {imprimir("Reconoce inicioFor");}

comparacionFor : identificador comparador identificador  {imprimir("Reconoce comparacionFor	");}
			   ;

incDecFor : UP      {imprimir("Reconoce UP");}
		  | DOWN    {imprimir("Reconoce DOWN");}
		  ;

condicion : expresion comparador expresion   {imprimir("Reconoce condicion");}
		  | error expresion				  {yyerror("Error en la parte izquierda de la condición");}
		  | expresion error 			  {yyerror("Error en la parte derecha de la condición");}		  
		  ;

comparador : '<='			{$$.obj = "<=";}															
		   | '>='			{$$.obj = ">=";}
		   | '<'			{$$.obj = "<";}
		   | '>'			{$$.obj = ">";}
		   | '!='			{$$.obj = "!=";}
		   | '=='           {$$.obj = "==";}
		   ;

asignacion : identificador '=' expresion ';'    {imprimir("Reconocio Asignacion");}
		   | identificador error expresion  	{yyerror("Error en el operador de asignacion, se espera ==");}
		   ;



expresion : expresion '+' termino   {imprimir("Reconocio suma");}
		  | expresion '-' termino    {imprimir("Reconocio resta");}
		  | termino
		  ;

termino : termino '*' factor {imprimir("Reconocio multiplicaion");}
		| termino '/' factor {imprimir("Reconocio division");}
		| factor
		;

factor  :   constante
		|   identificador
		;

identificador   :   ID  { 
							{imprimir("Reconoce palabra reservada");}
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

private void imprimir(String string) throws IOException {
	System.out.println(string);
}