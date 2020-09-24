%{
package Compilador;
%}


%token CTE ID IF THEN ELSE END_IF OUT FUNC RETURN FOR INT FLOAT PROC
WHILE PRINT INT BEGIN END ULONG DO UNTIL CLASS EXTENDS TO_ULONG VOID CADENA  
%token '==' '!=' '>=' '<=' 

%left '+' '-'
%left '*' '/'

%%

programa : lista_declaraciones bloqueEjecutable 							{print("Reconoce bien el lenguaje");}
		 | lista_declaraciones 												{yyerror($1, "Falta Bloque ejecutable (BEGIN END)");}
		 | lista_declaraciones error 										{yyerror("Error en el Bloque ejecutable  ");}
		 | error bloqueEjecutable											{yyerror("Error en el Bloque Declarativo ");}
;



declaraciones_dentro_de_clase :	declaracionAtributo
							  | declaracionMetodo
;

declaraciones : declaracionVariable
;

lista_declaraciones_dentro_de_clase : lista_declaraciones_dentro_de_clase declaraciones_dentro_de_clase 
									| declaraciones_dentro_de_clase
;
lista_declaraciones : lista_declaraciones declaraciones 
					| declaraciones
;

declaracionVariable : tipo lista_de_ID ';'														{
																								 AsignarInfo($1.sval, "VARIABLE");}
																				 
					| tipo ID ';'																{if (!a.TablaSimbolo.get($2.sval).estaDeclarado()){
																									AsignarTipo($1.sval, $2.sval);
																									asignarUso($2.sval,"VARIABLE");
																									setDeclarado($2.sval);
																									asignarAmbiente($2.sval);}
																								 else 
																									yyerror("El nombre del Variable "+$2.sval+" ya fue utilizado");}
;

declaracionMetodo : iniMetodo '(' ')' BEGIN sentenciasEjecutables END							{
																								asignarUso($1.sval,"METODO");
																								asignarAmbiente($1.sval);
																								setDeclarado($1.sval));
																								Terceto t = new Terceto(contadorTerceto,"RET","","",a.TablaSimbolo);
																								contadorTerceto++;
																								listaTercetos.add(t);
																								}
				  | iniMetodo '(' ')' BEGIN  END												{
																								 asignarUso($2.sval,"METODO");
																								 asignarAmbiente($2.sval);
																								 setDeclarado($2.sval);
																								Terceto t = new Terceto(contadorTerceto,"RET","","",a.TablaSimbolo);
																								contadorTerceto++;
																								listaTercetos.add(t);}
				  | iniMetodo '(' error ')' BEGIN sentenciasEjecutables END		    			{yyerror("No se permite pasaje por parámetros");}
				  | iniMetodo '('  BEGIN sentenciasEjecutables END								{yyerror("Falta ')'");}
				  | iniMetodo ')' BEGIN sentenciasEjecutables END								{yyerror("Falta '('");}
				  | iniMetodo '(' ')' sentenciasEjecutables END									{yyerror("Falta BEGIN");}
				  | iniMetodo '('  BEGIN  END														{yyerror("Falta ')'");}
				  | iniMetodo ')' BEGIN  END														{yyerror("Falta '('");}
				  | iniMetodo '(' ')'  END														{yyerror("Falta BEGIN");}
				  
;

iniMetodo: VOID ID																				{Terceto t = new Terceto(contadorTerceto,"FUNCTION",$2.sval,"",a.TablaSimbolo);
																								contadorTerceto++;
																								listaTercetos.add(t);
																								$$.sval = $2.sval;
																								}	
		;

tipo : INT																			
     | ULONG
;

lista_de_ID : ID ',' lista_de_ID																{listaIdentificadores.add($1.sval);
																								 asignarAmbiente($1.sval);}
			| ID ',' ID																			{listaIdentificadores.add($1.sval);
																								 listaIdentificadores.add($3.sval);
																								asignarAmbiente($1.sval);
																								asignarAmbiente($3.sval);}
;

lista_herencias : ID ',' lista_herencias														{if (a.TablaSimbolo.get($1.sval).estaDeclarado())
																									listaHerencias.add($1.sval);
																								 else
																									yyerror("La clase "+$1.sval+" no esta declarada");}

			    | ID ',' ID																		{if (a.TablaSimbolo.get($1.sval).estaDeclarado())
																									listaHerencias.add($1.sval);
																								 else
																									yyerror("La clase "+$1.sval+" no esta declarada");
																								if (a.TablaSimbolo.get($3.sval).estaDeclarado())
																									listaHerencias.add($3.sval);
																								 else
																									yyerror("La clase "+$3.sval+" no esta declarada");}
;

bloqueEjecutable : BEGIN sentenciasEjecutables END
				 | BEGIN sentenciasEjecutables 													{yyerror("Falta 'END' para cerrar el bloque de sentencias");}
				 | BEGIN END 
				 | sentenciaEjecutable ';'		
				 | sentenciaControl
				 | sentenciaEjecutable															{yyerror("Falta ;");}
				 | error ';'																	{yyerror("Error en la sentencia ejecutable");}
;

sentenciasEjecutables : sentenciaEjecutable ';'
					  | sentenciaControl
					  | sentenciaControl sentenciasEjecutables					  
					  | sentenciaEjecutable 													{yyerror("Falta ;");}
					  | sentenciaEjecutable ';' sentenciasEjecutables
;


sentenciaEjecutable : asignacion
					| PRINT'('CADENA')'					   										{listaIdentificadores.add($3.sval);
																										Terceto t = new Terceto(contadorTerceto,"PRINT",$3.sval,"",a.TablaSimbolo);
																										contadorTerceto ++;
																										listaTercetos.add(t);                                      
																										$$.obj = t;
																								}
					| PRINT'('error')'					   										{yyerror("Falta cadena para imprimir");}
					| PRINT'('CADENA					   										{yyerror("Falta parentesis de cierre en la sentencia de impresion");} 
					| PRINT  CADENA')'					   										{yyerror("Falta parentesis de apertura en la sentencia de impresion");}
					| PRINT '('')'						   										{yyerror("CADENA faltante en la sentencia de impresion");}
					| '('CADENA')'                         										{yyerror("'PRINT' faltante en la sentencia de impresion");}
					| llamadaMetodo
;

sentenciaControl  : IF condIf cpoIf														
				  | IF  error	END_IF																{yyerror("Declaración de IF errónea");}
				  | IF '(' ')' bloqueEjecutable entroElse bloqueEjecutable END_IF 											{yyerror("Falta condicion");} 
				  | IF  condicion ')' bloqueEjecutable entroElse bloqueEjecutable END_IF 									{yyerror("Falta '(' ");} 
				  | IF '(' condicion  bloqueEjecutable entroElse bloqueEjecutable END_IF 									{yyerror("Falta ')' ");} 

			      | iniciaDo bloqueEjecutable cpoDo 									 			{
																									Terceto t = new Terceto(contadorTerceto,"FIN_DO","-","-", a.TablaSimbolo);	
																									listaTercetos.add(t);
																									contadorTerceto++;
																									Integer i = pilaTercetos.pop();
																									listaTercetos.get(i).setOperando2(t);
																									}
				  | iniciaDo bloqueEjecutable UNTIL '(' error ')'										{yyerror("Condicion inválida");}
				  | iniciaDo bloqueEjecutable UNTIL  condicion ')'										{yyerror($2, "Falta ')' ");} 
				  | iniciaDo bloqueEjecutable UNTIL '('  condicion 										{yyerror($2, "Falta '(' ");} 	
;

iniciaDo: DO																						{
																									inicioDO = contadorTerceto;
																									Terceto inido = new Terceto(contadorTerceto, "INIDO", inicioDO, "", a.TablaSimbolo);
																									listaTercetos.add(inido);
																									contadorTerceto++;
																									}
		;
		
cpoDo: UNTIL '(' condicion ')'																		{
																									
																									Terceto bd = new Terceto(contadorTerceto,"BD","","",a.TablaSimbolo);
																									bd.setOperando1(listaTercetos.get(listaTercetos.size()-1));
																									contadorTerceto++;
																									listaTercetos.add(bd);
																									pilaTercetos.push(bd.getNro());
																									
																									Terceto bi = new Terceto(contadorTerceto, "BI", "", "", a.TablaSimbolo);
																									bi.setOperando1(listaTercetos.get(inicioDO));
																									contadorTerceto++;
																									listaTercetos.add(bi);
																									}

	 ;


condIf: '(' condicion ')' 																			{
																										// CREO EL TERCETO CON BF , NOSE DONDE TERMINA PORQUE PUEDE TENER ELSE
																										Terceto t = new Terceto(contadorTerceto,"BF",$2.obj,"",a.TablaSimbolo);
																										t.setOperando1((Terceto)listaTercetos.get(listaTercetos.size()-1)); 			   	// Agrego a op1 el terceto anterior (la condicion)
																										contadorTerceto ++;
																										listaTercetos.add(t); 
																										pilaTercetos.push(t.getNro());                                         	// agrego el terceto incompleto a la pila
																										$$.obj = t;
																										
																									}


;

cpoIf:  bloqueEjecutable entroElse bloqueEjecutable END_IF
																									{	//TERMINE LA ESTRUCTURA DEL IF COMPLETA CON ELSE							
																										Integer i = pilaTercetos.pop();
																										if (listaTercetos.get(i).getOperador() == "BI"){ 
																											Terceto finif = new Terceto(contadorTerceto,"FINIF","-","-",a.TablaSimbolo);
																											contadorTerceto ++;
																											listaTercetos.add(finif);
																											listaTercetos.get(i).setOperando1(listaTercetos.get(contadorTerceto-1));
																										}
																										
																									
																									}
		| bloqueEjecutable	END_IF																			
																									{
																									//TERMINE LA ESTRUCTURA DEL IF COMPLETA NO TIENE ELSE
																									Integer i = pilaTercetos.pop();
																									if (listaTercetos.get(i).getOperador() == "BI")
																										listaTercetos.get(i).setOperando1(listaTercetos.size());
																										
																									Terceto finif = new Terceto(contadorTerceto,"FINIF","-","-",a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(finif);
																									if (listaTercetos.get(i).getOperador() == "BF")
																										listaTercetos.get(i).setOperando2(listaTercetos.get(listaTercetos.size()-1));
																									
																									}
;

entroElse : ELSE 																			{	Terceto t = new Terceto(contadorTerceto,"BI","-","-",a.TablaSimbolo);    // NO SE DONDE TERMINA EL IF COMPLETO
																								contadorTerceto ++;
																								listaTercetos.add(t);
																								Integer i = pilaTercetos.pop();
																								if (listaTercetos.get(i).getOperador() == "BF")               // VOY A COMPLETAR EL TERCETO CREADO EN LA CONDICION
																								{
																						
																									Terceto label_bi = new Terceto(contadorTerceto-1,"LABEL",listaTercetos.size(),"-",a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(label_bi);
																									listaTercetos.get(i).setOperando2(listaTercetos.get(contadorTerceto-1));       //LE SETEO EL ULTIMO TERCETO LEIDO
																								}
																								else 
																									listaTercetos.get(i).setOperando1(listaTercetos.get(contadorTerceto-1));
																								pilaTercetos.push(t.getNro());                                              // AGREGO EL INCOMPLETO A LA PILA
																								$$.obj = t;
																							}
 
condicion : expresion comparacion expresion  
																								{	
																									String operador = (String)$2.obj;
																									TercetoOperacion t = new TercetoOperacion(contadorTerceto,operador,$1.obj,$3.obj,a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(t);     
																									$$.obj = t;
																								}
		  | error expresion																		{yyerror("Error en la parte izquierda de la condición");}
		  | expresion error 																	{yyerror("Error en la parte derecha de la condición");}		  
;

comparacion : '<='			{$$.obj = "<=";}															
			| '>='			{$$.obj = ">=";}
			| '=='			{$$.obj = "==";}
			| '<'			{$$.obj = "<";}
			| '>'			{$$.obj = ">";}
			| '<>'			{$$.obj = "<>";}
;


llamadaMetodo : ID '.' ID '(' ')' 																{
																								Terceto t = new Terceto(contadorTerceto, "CALL", $3.sval, $1.sval, a.TablaSimbolo);
																								listaTercetos.add(t);
																								contadorTerceto++;
																								} 
			  | ID '.' ID ')'																	{yyerror("Falta '('");} 
			  | ID '.' ID '(' 																	{yyerror("Falta ')'");} 
			  | ID '.' ID '(' error ')'															{yyerror("No se permite pasaje de parámetros");} 
			  | ID '(' ')' 																		{yyerror("Error en la llamada a Metodo");} 
;

asignacion: identificador   ':='    expresion   {
												    Simbolo s = a.TablaSimbolo.get(((Simbolo)$1.obj).getValor());
													String tipo1 = s.getTipo();
													String tipo2 = ((Asignable)$3.obj).getTipo();
													if(!declaradoYaccesible(((Simbolo)$1.obj).getValor())){
													    yyerror("Variable no declarada");
													}
													else{
														if(tipo1.equals(tipo2)){
												    	    Terceto t = new TercetoOperacion(contadorTerceto, ":=",s,$3.obj,a.TablaSimbolo);
															contadorTerceto++;
															listaTercetos.add(t);
															$$.obj = t;
														}
														else{
															yyerror("Asignacion entre variables de tipos no compatible");
														}
													}
												}

			| ID error expresion 				{yyerror("Operador de asignacion erróneo");}
;

expresion: expresion '+' termino																
																								{
																								Asignable asOp1 = ((Asignable)$1.obj);
																								Asignable asOp2 = ((Asignable)$3.obj);

																								if (asOp1.getTipo().equals(asOp2.getTipo())){
																									TercetoOperacion t = new TercetoOperacion(contadorTerceto,"+",asOp1,asOp2,a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(t);
																									$$.obj = t;
																								}
																								else
																									yyerror("Error semantico: suma de tipos incompatibles");
																								}
		 | expresion '-' termino
																								{
																								Asignable asOp1 = ((Asignable)$1.obj);
																								Asignable asOp2 = ((Asignable)$3.obj);

																								if (asOp1.getTipo().equals(asOp2.getTipo())){
																									TercetoOperacion t = new TercetoOperacion(contadorTerceto,"-",asOp1,asOp2,a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(t);
																									$$.obj = t;
																								}
																								else
																									yyerror("Error semantico: resta de tipos incompatibles");
																								}
		 | termino
		 | TO_ULONG '(' expresion ')'															{
																									if(((Asignable)$3.obj).getTipo().equals("int")){
																										TercetoOperacion t = new TercetoOperacion(contadorTerceto,"TO_ULONG",(Asignable)$3.obj,"",a.TablaSimbolo);
																										t.setTipo("ulong");
																										contadorTerceto++;
																										listaTercetos.add(t);
																										$$.obj = t;
																									}
																									else{
																										yyerror("Error semántico: No se puede convertir la expresion a ulong");
																									}
																								}
		 | TO_ULONG '(' error ')'                                                      			{yyerror("Expresión inválida");}
;

termino: termino '*' factor
																								{
																								Asignable asOp1 = ((Asignable)$1.obj);
																								Asignable asOp2 = ((Asignable)$3.obj);

																								if (asOp1.getTipo().equals(asOp2.getTipo())){
																									TercetoOperacion t = new TercetoOperacion(contadorTerceto,"*",asOp1,asOp2,a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(t);
																									$$.obj = t;
																								}
																								else
																									yyerror("Error semantico: resta de tipos incompatibles");
																								}
	   | termino '/' factor
	   																								{
																								Asignable asOp1 = ((Asignable)$1.obj);
																								Asignable asOp2 = ((Asignable)$3.obj);

																								if (asOp1.getTipo().equals(asOp2.getTipo())){
																									TercetoOperacion t = new TercetoOperacion(contadorTerceto,"/",asOp1,asOp2,a.TablaSimbolo);
																									contadorTerceto ++;
																									listaTercetos.add(t);
																									$$.obj = t;
																								}
																								else
																									yyerror("Error semantico: division de tipos incompatibles");
																								}
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
						    Simbolo s = check_rango($2.sval);
							$$.obj = s;
						}
;

%%

private void check_rango(ParserVal num){
	System.out.println("El CTE NEGATIVO es "+num.t.toString());	
}

private void print(String string) throws IOException {
	System.out.println(string);
	//a.imprimirArchivo(string);
}