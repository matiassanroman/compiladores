//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;
import java.io.IOException;
import java.util.ArrayList;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short IF=258;
public final static short THEN=259;
public final static short ELSE=260;
public final static short END_IF=261;
public final static short OUT=262;
public final static short FUNC=263;
public final static short RETURN=264;
public final static short FOR=265;
public final static short INTEGER=266;
public final static short FLOAT=267;
public final static short PROC=268;
public final static short NS=269;
public final static short NA=270;
public final static short CADENA=271;
public final static short UP=272;
public final static short DOWN=273;
public final static short CTE=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    2,    5,    5,
    5,    6,    8,    8,    8,    8,    8,   10,   10,   10,
   11,   11,    9,    9,   12,   12,   12,   12,    3,    3,
    3,    3,    3,    3,    3,    3,   15,   15,   15,   16,
   19,   18,   21,   21,   21,   20,   20,   17,   17,   14,
   14,   24,   26,   27,   28,   25,   13,   29,   29,   29,
   30,   30,   30,   31,   31,   23,   23,   23,   23,   23,
   23,    4,    4,    7,   22,   22,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   13,   16,   19,   11,   12,    1,    3,    5,
    1,    2,    3,    3,    2,    2,    1,    1,    1,    5,
    5,    4,    3,    1,    5,    3,    7,    7,    7,    5,
    1,    3,    3,    3,    3,    2,    2,    2,    1,    1,
    1,   10,    1,    1,    1,    6,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   74,    0,    0,    0,   73,   72,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   29,   34,   11,    0,
   10,    0,    0,    0,   50,   51,    0,    0,    0,    2,
    3,    0,    0,    0,    0,   12,    8,    0,   36,    0,
   53,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    6,    0,    0,   21,    0,    0,   75,    0,   65,   64,
    0,    0,   63,    0,   27,   28,    0,    9,   66,   67,
   68,   69,   70,   71,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   22,   32,    0,    0,   76,
    0,    0,   57,    0,    0,   24,   23,   25,   26,    0,
   43,   45,    0,   35,   30,    0,   42,    0,    0,   41,
    0,    0,    0,   31,    0,    0,    0,   61,   62,   49,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   48,    0,   38,   39,   37,    0,    0,   40,    0,
    0,    0,    0,   20,    0,   46,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   52,    0,   16,    0,    0,    0,
   17,    0,    0,    0,    0,    0,   13,    0,    0,    0,
    0,    0,   14,    0,    0,   15,
};
final static short yydgoto[] = {                          9,
   10,   11,  120,   13,   20,   14,   15,   16,   36,   55,
   56,   67,   17,   24,   18,   48,  121,   49,  109,  139,
   41,   60,   75,   25,   26,   42,  122,  159,   61,   62,
   63,
};
final static short yysindex[] = {                      -157,
 -182,    0,  -32,  -17,  -13,    0,    0, -228,    0, -157,
    0,    0, -182,    0,  -19,  -77,    0,    0,    0,    6,
    0, -197, -228, -193,    0,    0, -209, -154,   36,    0,
    0,    7,  -34,  -45, -144,    0,    0, -228,    0,  -57,
    0,   15,    0,   43,   45,   48,   32,   55,   70,  -39,
    0, -228,   71,    0,   63,   88,    0, -138,    0,    0,
  -15,   16,    0, -116,    0,    0, -119,    0,    0,    0,
    0,    0,    0,    0,  -45,   19,   85,   93,   30,  -44,
   31, -228,  118, -110, -228,    0,    0,  103, -150,    0,
  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0,  102,
    0,    0, -205,    0,    0, -205,    0, -175,  107,    0,
 -103,  108,   28,    0,  126,   16,   16,    0,    0,    0,
 -205,   46, -107,   47, -101, -153,  112, -100, -151,  -95,
 -150,    0,  -84,    0,    0,    0,  -44,  -44,    0,  -97,
  134, -228,  119,    0,   56,    0,    0,  137,  -87,   29,
  -91, -205,  -85,  124, -151,  -83,  142, -205,   64,  127,
  -82, -228,  129,  -78,    0,  -81,    0,  153,  -79,  135,
    0,  -73,  154,  -75,  139,  -68,    0,  -72,  143,  159,
  -69,  -63,    0,  146,  -66,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  209,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  169,    0,    0,    0,    0,
    0,  -11,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -16,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  170,  -10,   -5,    0,    0,    0,
   89,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -48,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    4,   10,  -38,  203,    0,    3,    0,    0,    0,
  -70,    0,  150,    0,    0,    0,  -93,    0,    0,    0,
  144,  -58,    0,    0,    0,    0,    0,    0,    0,   34,
   33,
};
final static int YYTABLESIZE=230;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,   84,   74,   21,   73,   97,   53,   23,   96,   12,
   29,   85,  123,   30,  125,   21,  102,  134,  115,   31,
   33,  107,   27,  136,   44,   40,   28,   91,    2,   92,
   47,   60,   58,   60,   58,   54,   59,   59,   65,   59,
   68,   34,   44,   93,   66,   35,   44,   60,   58,   38,
   38,    2,    3,   59,   86,   76,    4,   94,  158,    5,
  144,   45,   95,   39,   37,   51,   21,   43,  130,  156,
   98,  129,  155,   19,    2,   50,   99,  100,  146,  147,
  124,    2,    3,   77,   40,   78,    4,  113,   79,    5,
  142,   54,   80,   59,   59,   81,   59,   59,    1,    2,
    3,   46,    2,   88,    4,   52,    2,    5,    6,    7,
    8,   64,    2,    3,    6,    7,  162,    4,  137,  138,
    5,    6,    7,    8,  116,  117,  118,  119,   82,   87,
  132,   89,  132,   54,  132,   90,    1,    2,    3,   19,
    2,  103,    4,  104,  150,    5,    6,    7,    8,    2,
    3,  105,  106,  108,    4,    2,    3,    5,  111,  112,
    4,  114,   34,    5,  168,  126,  127,  132,  128,  131,
  133,  135,  140,  141,  143,  145,  148,  149,  152,  151,
  153,  154,  157,  160,  161,  164,  163,  166,  165,  169,
  170,  167,  171,  172,  173,  174,  175,  176,  177,  178,
  179,  180,  182,  181,  183,  184,  185,  186,    1,   18,
   19,    2,   56,   54,   55,   32,   83,   69,   70,   71,
   72,   52,    2,   22,  101,  110,    6,    7,   57,   57,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   60,    1,   62,  125,   41,   40,  125,    0,
    8,   50,  106,   10,  108,   13,   75,  125,   89,   10,
   40,   80,   40,  125,   41,   23,   40,   43,  257,   45,
   28,   43,   43,   45,   45,   33,   34,   43,   35,   45,
   38,   61,   59,   59,   35,  123,  256,   59,   59,   44,
   44,  257,  258,   59,   52,   41,  262,   42,  152,  265,
  131,  271,   47,  261,   59,   59,   64,  261,   41,   41,
   67,   44,   44,  256,  257,   40,   67,   75,  137,  138,
  256,  257,  258,   41,   82,   41,  262,   85,   41,  265,
  129,   89,   61,   91,   92,   41,   94,   95,  256,  257,
  258,  256,  257,   41,  262,  256,  257,  265,  266,  267,
  268,  256,  257,  258,  266,  267,  155,  262,  272,  273,
  265,  266,  267,  268,   91,   92,   94,   95,   59,   59,
  121,   44,  123,  131,  125,  274,  256,  257,  258,  256,
  257,  123,  262,   59,  142,  265,  266,  267,  268,  257,
  258,   59,  123,  123,  262,  257,  258,  265,   41,  270,
  262,   59,   61,  265,  162,   59,  270,  158,   61,   44,
  125,  125,   61,  274,  270,  260,  274,   44,  123,   61,
   44,  269,  274,  269,   61,   44,  270,   61,  125,   61,
  269,  274,  274,   41,  274,   61,  270,   44,  274,   61,
  269,  274,   44,   61,  274,  269,   61,  274,    0,   41,
   41,  257,  261,  125,  125,   13,  256,  275,  276,  277,
  278,  256,  257,  256,   75,   82,  266,  267,  274,  274,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","IF","THEN","ELSE","END_IF","OUT",
"FUNC","RETURN","FOR","INTEGER","FLOAT","PROC","NS","NA","CADENA","UP","DOWN",
"CTE","\"<=\"","\">=\"","\"!=\"","\"==\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloquePrograma",
"bloquePrograma : bloquePrograma sentenciaDeclarativa",
"bloquePrograma : bloquePrograma sentenciaEjecutable",
"bloquePrograma : sentenciaDeclarativa",
"bloquePrograma : sentenciaEjecutable",
"sentenciaDeclarativa : tipo listaVariables ';'",
"sentenciaDeclarativa : declaracionProcedimiento",
"sentenciaDeclarativa : error listaVariables ';'",
"listaVariables : listaVariables ',' identificador",
"listaVariables : identificador",
"listaVariables : error",
"declaracionProcedimiento : encabezadoProc bloqueProc",
"encabezadoProc : PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' error ')' NA '=' CTE ',' NS '=' CTE",
"parametrosProc : parametro",
"parametrosProc : parametro ',' parametro",
"parametrosProc : parametro ',' parametro ',' parametro",
"parametro : identificador",
"parametro : error identificador",
"bloqueProc : '{' bloque '}'",
"bloqueProc : '{' error '}'",
"bloque : bloque sentenciaDeclarativa",
"bloque : bloque sentenciaEjecutable",
"bloque : sentenciaDeclarativa",
"bloque : sentenciaEjecutable",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : OUT '(' CADENA ')' ';'",
"sentenciaEjecutable : identificador '(' parametrosProc ')' ';'",
"sentenciaEjecutable : identificador '(' ')' ';'",
"sentenciaEjecutable : IF cuerpoIf END_IF",
"sentenciaEjecutable : cicloFor",
"sentenciaEjecutable : OUT '(' error ')' ';'",
"sentenciaEjecutable : IF error END_IF",
"cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}'",
"cicloFor : FOR '(' error ')' '{' bloqueSentencia '}'",
"cicloFor : FOR '(' condicionFor ')' '{' error '}'",
"condicionFor : inicioFor ';' condiFOR ';' incDec",
"condiFOR : condicion",
"inicioFor : identificador '=' constante",
"condicion : identificador comparador asignacion",
"condicion : identificador comparador identificador",
"condicion : identificador comparador constante",
"incDec : UP constante",
"incDec : DOWN constante",
"bloqueSentencia : bloqueSentencia sentenciaEjecutable",
"bloqueSentencia : sentenciaEjecutable",
"cuerpoIf : cuerpoCompleto",
"cuerpoIf : cuerpoIncompleto",
"cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE '{' bloqueElse '}'",
"condicionIf : condicion",
"bloqueThen : bloqueSentencia",
"bloqueElse : bloqueSentencia",
"cuerpoIncompleto : '(' condicionIf ')' '{' bloqueThen '}'",
"asignacion : identificador '=' expresion ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : constante",
"factor : identificador",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"!=\"",
"comparador : \"==\"",
"comparador : '>'",
"comparador : '<'",
"tipo : FLOAT",
"tipo : INTEGER",
"identificador : ID",
"constante : CTE",
"constante : '-' CTE",
};

//#line 223 "gramatica.y"

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

void setearProc(String sval, String cantParametros, String na, String ns){
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setCantParametros(Integer.parseInt(cantParametros));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNa(Integer.parseInt(na));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNs(Integer.parseInt(ns));
	compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setTipo("NA_PROC");
	compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setTipo("NS_PROC");
}

void setearAmbitoNaNs(String na, String ns){
	compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setAmbito(compilador.Compilador.ambito,false);
	compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setAmbito(compilador.Compilador.ambito,false);
}

void setearAmbito(String sval){
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
}

void setearAmbitoyDeclarada(String sval, String tipo){
	if(tipo.equals("")){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
	}else{
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("PARAM_PROC");
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipoParametro(tipo);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setPasajeParametro("COPIA VALOR");
	}
}
//#line 500 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 14 "gramatica.y"
{mostrarMensaje("Reconoce bien el programa");
System.out.println(polaca.toString());}
break;
case 8:
//#line 26 "gramatica.y"
{yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 9:
//#line 29 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval,""); if(sePuedeUsar(val_peek(0).sval) == 2){mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");} }
break;
case 10:
//#line 30 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval,""); if(sePuedeUsar(val_peek(0).sval) == 2){mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");} }
break;
case 11:
//#line 31 "gramatica.y"
{yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 12:
//#line 34 "gramatica.y"
{ disminuirAmbito(); compilador.Compilador.na = compilador.Compilador.na + compilador.Compilador.naa; }
break;
case 13:
//#line 37 "gramatica.y"
{ setearProc(val_peek(11).sval, "1", val_peek(4).sval, val_peek(0).sval);  setearAmbito(val_peek(11).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(11).sval; setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);  if(sePuedeUsar(val_peek(11).sval) == 2){mostrarMensaje(val_peek(11).sval + " esta Redeclarada.");} setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval); }
break;
case 14:
//#line 38 "gramatica.y"
{ setearProc(val_peek(14).sval, "2", val_peek(4).sval, val_peek(0).sval); setearAmbito(val_peek(14).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(14).sval; setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval); if(sePuedeUsar(val_peek(14).sval) == 2){mostrarMensaje(val_peek(14).sval + " esta Redeclarada.");} setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval); setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval); }
break;
case 15:
//#line 39 "gramatica.y"
{ setearProc(val_peek(17).sval, "3", val_peek(4).sval, val_peek(0).sval); setearAmbito(val_peek(17).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(17).sval; setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval); if(sePuedeUsar(val_peek(17).sval) == 2){mostrarMensaje(val_peek(17).sval + " esta Redeclarada.");} setearAmbitoyDeclarada(val_peek(14).sval,val_peek(15).sval); setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval); setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval); }
break;
case 16:
//#line 40 "gramatica.y"
{ setearProc(val_peek(9).sval, "0", val_peek(4).sval, val_peek(0).sval);  setearAmbito(val_peek(9).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(9).sval; setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);  if(sePuedeUsar(val_peek(9).sval) == 2){mostrarMensaje(val_peek(9).sval + " esta Redeclarada.");} verificarNa(val_peek(4).sval,val_peek(9).sval);}
break;
case 17:
//#line 41 "gramatica.y"
{yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 21:
//#line 50 "gramatica.y"
{ setearAmbito(val_peek(-1).sval); }
break;
case 22:
//#line 51 "gramatica.y"
{yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 24:
//#line 55 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 33:
//#line 68 "gramatica.y"
{ 	if (PolacaInversa.getFlagITE()){
												polaca.completarPolaca(PolacaInversa.getRetrocesosITE());
											}
											else
												polaca.completarPolaca(PolacaInversa.getRetrocesosIT());
											}
break;
case 35:
//#line 75 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 76 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 37:
//#line 79 "gramatica.y"
{polaca.borrarVariablesControl();Par pasoEnBlanco = new Par(""); polaca.agregarPaso(pasoEnBlanco); polaca.agregarPasoIncompleto(); Par pasoBI = new Par("BI"); polaca.agregarPaso(pasoBI); polaca.completarFOR(); polaca.borrarInicioFOR(); polaca.borrarPasoIncompleto();polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());}
break;
case 38:
//#line 80 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 39:
//#line 81 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 40:
//#line 85 "gramatica.y"
{
polaca.borrarPasoPolaca();}
break;
case 41:
//#line 89 "gramatica.y"
{Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);	}
break;
case 42:
//#line 96 "gramatica.y"
{ polaca.agregarVariableControl(val_peek(2).sval); Par id = new Par(val_peek(2).sval);
polaca.agregarPaso(id);Par asig = new Par(val_peek(1).sval);
polaca.agregarPaso(asig);polaca.agregarInicioFOR();}
break;
case 43:
//#line 101 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval);Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp);  }
break;
case 44:
//#line 104 "gramatica.y"
{
			Par id1 = new Par(val_peek(2).sval); Par id2 = new Par(val_peek(0).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id1); polaca.agregarPaso(id2); polaca.agregarPaso(comp); }
break;
case 45:
//#line 107 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp); }
break;
case 46:
//#line 113 "gramatica.y"
{
	polaca.agregarVariableControl("+");
	polaca.agregarVariableControl(val_peek(0).sval);
	}
break;
case 47:
//#line 118 "gramatica.y"
{
	   polaca.agregarVariableControl("-");
		polaca.agregarVariableControl(val_peek(0).sval);
	   }
break;
case 50:
//#line 128 "gramatica.y"
{	PolacaInversa.setFlagITE(true); }
break;
case 51:
//#line 129 "gramatica.y"
{ PolacaInversa.setFlagITE(false); 
		 polaca.borrarPasoPolaca();
		 polaca.borrarPasoPolaca();
		 polaca.borrarPasoIncompleto();}
break;
case 53:
//#line 138 "gramatica.y"
{
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);
				}
break;
case 54:
//#line 147 "gramatica.y"
{Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);}
break;
case 57:
//#line 161 "gramatica.y"
{
            setearAmbito(val_peek(3).sval); if(sePuedeUsar(val_peek(3).sval) == 1){mostrarMensaje(val_peek(3).sval + " No esta declarada.");}
			Par id =  new Par(val_peek(3).sval);
			Par asig = new Par(val_peek(2).sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(asig); }
break;
case 58:
//#line 170 "gramatica.y"
{
			Par suma =  new Par("+");
			polaca.agregarPaso(suma);  }
break;
case 59:
//#line 174 "gramatica.y"
{
		    						Par resta =  new Par("-");
									polaca.agregarPaso(resta); }
break;
case 61:
//#line 181 "gramatica.y"
{
							  Par multi =  new Par("*");
							  polaca.agregarPaso(multi);		}
break;
case 62:
//#line 185 "gramatica.y"
{ 
							  Par division =  new Par("/");
							  polaca.agregarPaso(division);}
break;
case 65:
//#line 192 "gramatica.y"
{ setearAmbito(val_peek(0).sval); if(sePuedeUsar(val_peek(0).sval) == 1){mostrarMensaje(val_peek(0).sval + " No esta declarada.");}
                         Par id =  new Par(val_peek(0).sval);
					     polaca.agregarPaso(id); }
break;
case 75:
//#line 212 "gramatica.y"
{
                     setearAmbito(val_peek(0).sval);
					 Par cte =  new Par(val_peek(0).sval);
					 polaca.agregarPaso(cte);            }
break;
case 76:
//#line 216 "gramatica.y"
{  
                     setearAmbito(val_peek(0).sval);
		  			 Par cte =  new Par("-"+val_peek(1).sval);
					 polaca.agregarPaso(cte);            }
break;
//#line 868 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
