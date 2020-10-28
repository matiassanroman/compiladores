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
   18,   19,   19,   19,   20,   20,   17,   17,   14,   14,
   23,   25,   26,   27,   24,   13,   28,   28,   28,   29,
   29,   29,   30,   30,   22,   22,   22,   22,   22,   22,
    4,    4,    7,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   13,   16,   19,   11,   12,    1,    3,    5,
    2,    2,    3,    3,    2,    2,    1,    1,    1,    5,
    5,    4,    3,    1,    5,    3,    7,    7,    7,    5,
    3,    3,    3,    3,    2,    2,    2,    1,    1,    1,
   10,    1,    1,    1,    6,    4,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   73,    0,    0,    0,   72,   71,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   29,   34,   11,    0,
   10,    0,    0,    0,   49,   50,    0,    0,    0,    2,
    3,    0,    0,    0,    0,   12,    8,    0,   36,    0,
   52,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    6,    0,    0,    0,    0,    0,   74,    0,   64,   63,
    0,    0,   62,    0,   27,   28,    0,    9,   65,   66,
   67,   68,   69,   70,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   22,   32,   21,    0,    0,
   75,    0,    0,   56,    0,    0,   24,   23,   25,   26,
    0,   42,   44,    0,   35,   30,    0,   41,    0,    0,
    0,    0,    0,   31,    0,    0,    0,   60,   61,   48,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   47,    0,   38,   39,   37,    0,    0,   40,    0,
    0,    0,    0,   20,    0,   45,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   51,    0,   16,    0,    0,    0,
   17,    0,    0,    0,    0,    0,   13,    0,    0,    0,
    0,    0,   14,    0,    0,   15,
};
final static short yydgoto[] = {                          9,
   10,   11,  120,   13,   20,   14,   15,   16,   36,   55,
   56,   67,   17,   24,   18,   48,  121,   49,   41,  139,
   60,   75,   25,   26,   42,  122,  159,   61,   62,   63,
};
final static short yysindex[] = {                      -176,
 -210,    0,  -37,  -17,  -13,    0,    0, -201,    0, -176,
    0,    0, -210,    0,  -19,  -65,    0,    0,    0,    1,
    0, -193, -201, -182,    0,    0, -206, -173,   47,    0,
    0,    7,  -39,  -45, -153,    0,    0, -201,    0,  -53,
    0,   56,    0,   61,   70,   80,   62,   88,   74,  -35,
    0, -201,   76, -201,   95,   93,    0, -136,    0,    0,
  -15,   12,    0, -117,    0,    0, -115,    0,    0,    0,
    0,    0,    0,    0,  -45,   21,   87,   89,   26,  -44,
   35, -201,  119, -104, -201,    0,    0,    0,  108, -203,
    0,  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0,
  107,    0,    0, -138,    0,    0, -138,    0, -140,  110,
 -100,  111,   29,    0,  127,   12,   12,    0,    0,    0,
 -138,   48, -103,   49, -101, -179,  114,  -98, -166,  -93,
 -203,    0,  -82,    0,    0,    0,  -44,  -44,    0,  -95,
  136, -201,  120,    0,   59,    0,    0,  139,  -85,   33,
  -89, -138,  -83,  126, -166,  -81,  144, -138,   65,  130,
  -80, -201,  131,  -76,    0,  -79,    0,  155,  -77,  137,
    0,  -71,  156,  -73,  141,  -66,    0,  -69,  143,  162,
  -67,  -61,    0,  148,  -64,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  211,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  172,    0,    0,    0,    0,
    0,  -11,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -16,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  173,  -10,   -7,    0,    0,    0,
   90,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   91,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    4,    5,  -21,  207,    0,    3,    0,    0,    0,
  -70,    0,  151,    0,    0,    0,  -90,    0,  152,    0,
  -62,    0,    0,    0,    0,    0,    0,    0,   14,   36,
};
final static int YYTABLESIZE=234;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,   53,   23,   21,   12,   84,   74,   97,   73,   98,
   29,   54,  103,   30,   31,   21,  123,  108,  125,  115,
   33,  134,   27,  136,   43,   40,   28,   92,   85,   93,
   47,   59,   57,   59,   57,   58,   59,   58,   65,   66,
   68,   34,   43,   94,   38,   19,    2,   59,   57,   44,
   38,   58,   52,   95,   86,    2,   88,   35,   96,   37,
  144,  158,    6,    7,   45,   51,   21,   39,   54,  130,
   99,  100,  129,  156,  146,  147,  155,  101,   43,    1,
    2,    3,   46,    2,   40,    4,   50,  113,    5,    6,
    7,    8,  137,  138,   59,   59,   76,   59,   59,    6,
    7,   77,   64,    2,    3,  116,  117,  142,    4,   54,
   78,    5,    6,    7,    8,  124,    2,    3,    2,    3,
   79,    4,   80,    4,    5,  132,    5,  132,   81,  132,
  118,  119,   82,  162,   87,   89,   90,   91,   19,    2,
    1,    2,    3,  104,  150,  105,    4,  106,  107,    5,
    6,    7,    8,    2,    3,    2,    3,  109,    4,  111,
    4,    5,  132,    5,  168,  112,  114,   34,  126,  127,
  131,  128,  133,  135,  140,  141,  143,  145,  148,  149,
  151,  152,  153,  154,  157,  160,  161,  164,  163,  165,
  166,  169,  170,  167,  171,  172,  173,  174,  175,  176,
  177,  178,  179,  181,  180,  182,  183,  184,  185,  186,
    1,    2,   18,   19,   53,   54,   52,   55,   22,   32,
   83,   69,   70,   71,   72,  102,    6,    7,   57,   57,
    6,    7,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   40,    1,    0,   41,   60,  125,   62,  125,
    8,   33,   75,   10,   10,   13,  107,   80,  109,   90,
   40,  125,   40,  125,   41,   23,   40,   43,   50,   45,
   28,   43,   43,   45,   45,   43,   34,   45,   35,   35,
   38,   61,   59,   59,   44,  256,  257,   59,   59,  256,
   44,   59,  256,   42,   52,  257,   54,  123,   47,   59,
  131,  152,  266,  267,  271,   59,   64,  261,   90,   41,
   67,   67,   44,   41,  137,  138,   44,   75,  261,  256,
  257,  258,  256,  257,   82,  262,   40,   85,  265,  266,
  267,  268,  272,  273,   92,   93,   41,   95,   96,  266,
  267,   41,  256,  257,  258,   92,   93,  129,  262,  131,
   41,  265,  266,  267,  268,  256,  257,  258,  257,  258,
   41,  262,   61,  262,  265,  121,  265,  123,   41,  125,
   95,   96,   59,  155,   59,   41,   44,  274,  256,  257,
  256,  257,  258,  123,  142,   59,  262,   59,  123,  265,
  266,  267,  268,  257,  258,  257,  258,  123,  262,   41,
  262,  265,  158,  265,  162,  270,   59,   61,   59,  270,
   44,   61,  125,  125,   61,  274,  270,  260,  274,   44,
   61,  123,   44,  269,  274,  269,   61,   44,  270,  125,
   61,   61,  269,  274,  274,   41,  274,   61,  270,   44,
  274,   61,  269,   61,  274,   44,  274,  269,   61,  274,
    0,  257,   41,   41,  125,  125,  256,  261,  256,   13,
  256,  275,  276,  277,  278,   75,  266,  267,  274,  274,
  266,  267,   -1,   82,
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
"parametro : tipo identificador",
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
"condicionFor : inicioFor ';' condicion ';' incDec",
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

//#line 194 "gramatica.y"

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
//#line 481 "Parser.java"
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
{mostrarMensaje("Reconoce bien el programa");}
break;
case 6:
//#line 23 "gramatica.y"
{mostrarMensaje("Reconocio declaracion de una o mas variables en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 8:
//#line 25 "gramatica.y"
{yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 9:
//#line 28 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); if(sePuedeUsar(val_peek(0).sval) == 2){mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");} }
break;
case 10:
//#line 29 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); if(sePuedeUsar(val_peek(0).sval) == 2){mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");} }
break;
case 11:
//#line 30 "gramatica.y"
{yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 12:
//#line 33 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea); disminuirAmbito(); compilador.Compilador.na = compilador.Compilador.na + compilador.Compilador.naa;  }
break;
case 13:
//#line 36 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(11).sval); setearAmbito(val_peek(11).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(11).sval; if(sePuedeUsar(val_peek(11).sval) == 2){mostrarMensaje(val_peek(11).sval + " esta Redeclarada.");} setearAmbito(val_peek(4).sval);  setearAmbito(val_peek(0).sval); setearAmbitoyDeclarada(val_peek(8).sval); }
break;
case 14:
//#line 37 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(14).sval); setearAmbito(val_peek(14).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(14).sval; if(sePuedeUsar(val_peek(14).sval) == 2){mostrarMensaje(val_peek(14).sval + " esta Redeclarada.");} setearAmbito(val_peek(4).sval); setearAmbito(val_peek(0).sval); setearAmbitoyDeclarada(val_peek(11).sval); setearAmbitoyDeclarada(val_peek(8).sval); }
break;
case 15:
//#line 38 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(17).sval); setearAmbito(val_peek(17).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(17).sval; if(sePuedeUsar(val_peek(17).sval) == 2){mostrarMensaje(val_peek(17).sval + " esta Redeclarada.");} setearAmbito(val_peek(4).sval); setearAmbito(val_peek(0).sval); setearAmbitoyDeclarada(val_peek(14).sval); setearAmbitoyDeclarada(val_peek(11).sval); setearAmbitoyDeclarada(val_peek(8).sval); }
break;
case 16:
//#line 39 "gramatica.y"
{mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(9).sval); setearAmbito(val_peek(9).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(9).sval; if(sePuedeUsar(val_peek(9).sval) == 2){mostrarMensaje(val_peek(9).sval + " esta Redeclarada.");} setearAmbito(val_peek(4).sval);  setearAmbito(val_peek(0).sval); verificarNa(val_peek(4).sval,val_peek(9).sval); }
break;
case 17:
//#line 40 "gramatica.y"
{yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 21:
//#line 48 "gramatica.y"
{mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);  setearAmbito(val_peek(0).sval); }
break;
case 22:
//#line 49 "gramatica.y"
{yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 23:
//#line 52 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 24:
//#line 53 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 30:
//#line 63 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 31:
//#line 64 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 32:
//#line 65 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 34:
//#line 67 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 35:
//#line 68 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 69 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 38:
//#line 73 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 39:
//#line 74 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 40:
//#line 77 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 42:
//#line 83 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval);Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp);  }
break;
case 43:
//#line 86 "gramatica.y"
{
			Par id1 = new Par(val_peek(2).sval); Par id2 = new Par(val_peek(0).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id1); polaca.agregarPaso(id2); polaca.agregarPaso(comp); }
break;
case 44:
//#line 89 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp); }
break;
case 45:
//#line 94 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 46:
//#line 95 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 51:
//#line 106 "gramatica.y"
{mostrarMensaje("Reconocio IF con ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 52:
//#line 109 "gramatica.y"
{
			/* if algo then*/
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);
			/* else*/
			/*	polaca.completarPolaca(PolacaInversa.getRetrocesosIT()); */
				}
break;
case 53:
//#line 121 "gramatica.y"
{
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBI = new Par("BI"); 
				polaca.agregarPaso(pasoBI);
				}
break;
case 54:
//#line 130 "gramatica.y"
{polaca.completarPolaca(PolacaInversa.getRetrocesosITE());}
break;
case 55:
//#line 133 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 56:
//#line 135 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);
            setearAmbito(val_peek(3).sval); if(sePuedeUsar(val_peek(3).sval) == 1){mostrarMensaje(val_peek(3).sval + " No esta declarada.");}
			Par id =  new Par(val_peek(3).sval);
			Par asig = new Par(val_peek(2).sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(asig); }
break;
case 57:
//#line 143 "gramatica.y"
{mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea); 
			Par suma =  new Par("+");
			polaca.agregarPaso(suma);  }
break;
case 58:
//#line 146 "gramatica.y"
{mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);
		    						Par resta =  new Par("-");
									polaca.agregarPaso(resta); }
break;
case 60:
//#line 152 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea); 
							  Par multi =  new Par("*");
							  polaca.agregarPaso(multi);		}
break;
case 61:
//#line 155 "gramatica.y"
{mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea); 
							  Par division =  new Par("/");
							  polaca.agregarPaso(division);}
break;
case 64:
//#line 162 "gramatica.y"
{ setearAmbito(val_peek(0).sval); if(sePuedeUsar(val_peek(0).sval) == 1){mostrarMensaje(val_peek(0).sval + " No esta declarada.");}
                         Par id =  new Par(val_peek(0).sval);
					     polaca.agregarPaso(id); }
break;
case 65:
//#line 167 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 66:
//#line 168 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 67:
//#line 169 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 68:
//#line 170 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 69:
//#line 171 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 70:
//#line 172 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 71:
//#line 175 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 72:
//#line 176 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 73:
//#line 179 "gramatica.y"
{mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);
					            }
break;
case 74:
//#line 183 "gramatica.y"
{mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);
                     setearAmbito(val_peek(0).sval);
					 Par cte =  new Par(val_peek(0).sval);
					 polaca.agregarPaso(cte);            }
break;
case 75:
//#line 187 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);  
                     setearAmbito(val_peek(0).sval);
		  			 Par cte =  new Par("-"+val_peek(1).sval);
					 polaca.agregarPaso(cte);            }
break;
//#line 881 "Parser.java"
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
//Definiciones propias

Compilador c;
ArrayList<String> errores = new ArrayList<String>();
Token t;
int lineaActual;
ArrayList<String> reconocidos = new ArrayList<String>();
PolacaInversa polaca = new PolacaInversa();


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
//
e.printStackTrace();
}
return 0;
}

public void yyerror(String error){
this.errores.add(error + " en linea " + this.lineaActual) ;
}

public ArrayList<String> getErrores(){
return this.errores;
}

public ArrayList<String> getReconocidos(){
return this.reconocidos;
}

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
