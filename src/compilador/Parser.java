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
   23,   25,   26,   27,   28,   24,   29,   30,   13,   31,
   31,   31,   32,   32,   32,   33,   33,   22,   22,   22,
   22,   22,   22,    4,    4,    7,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   13,   16,   19,   11,   12,    1,    3,    5,
    2,    2,    3,    3,    2,    2,    1,    1,    1,    5,
    5,    4,    3,    1,    5,    3,    7,    7,    7,    5,
    3,    3,    3,    3,    2,    2,    2,    1,    1,    1,
    5,    6,    1,    1,    1,    1,    6,    1,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   76,    0,    0,    0,   75,   74,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   29,   34,   11,    0,
   10,    0,    0,    0,   49,   50,    0,   56,    0,    0,
    0,    2,    3,    0,    0,    0,    0,   12,    8,    0,
   36,    0,   54,    0,   33,    0,    0,    0,    0,    0,
    0,    0,    0,    6,    0,    0,    0,    0,    0,   77,
    0,   67,   66,    0,    0,   65,    0,   27,   28,    0,
    9,   68,   69,   70,   71,   72,   73,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   22,
   32,   21,    0,    0,   78,    0,    0,   59,    0,    0,
   24,   23,   25,   26,    0,   42,   44,    0,   48,    0,
    0,   35,   30,    0,   41,    0,    0,    0,    0,    0,
   31,    0,    0,    0,   63,   64,    0,    0,   58,    0,
    0,   47,   51,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   52,   57,   38,   39,   37,    0,    0,   40,
    0,    0,    0,    0,   20,   45,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   16,    0,    0,    0,   17,    0,    0,    0,
    0,    0,   13,    0,    0,    0,    0,    0,   14,    0,
    0,   15,
};
final static short yydgoto[] = {                          9,
   10,   11,  109,   13,   20,   14,   15,   16,   38,   58,
   59,   70,   17,   24,   18,   51,  110,   52,   43,  150,
   63,   78,   25,   26,   27,  111,   44,  130,   28,  131,
   64,   65,   66,
};
final static short yysindex[] = {                      -149,
 -225,    0,  -36,    1,    9,    0,    0, -234,    0, -149,
    0,    0, -225,    0,  -14,  -68,    0,    0,    0,   -2,
    0, -192, -234, -186,    0,    0, -168,    0, -205, -160,
   49,    0,    0,   14,  -38,  -44, -133,    0,    0, -234,
    0,  -52,    0,   61,    0,    7,   65,   80,   87,   70,
   95,   83,  -34,    0, -234,   93, -234,  112,  110,    0,
 -115,    0,    0,   -7,  -17,    0, -116,    0,    0, -119,
    0,    0,    0,    0,    0,    0,    0,  -44,   43, -171,
  108,  111,   51,  -43,   52, -234,  131,  -94, -234,    0,
    0,    0,  118, -189,    0,  -44,  -44,    0,  -44,  -44,
    0,    0,    0,    0,  117,    0,    0, -171,    0, -171,
   54,    0,    0, -171,    0, -177,  121,  -89,  122,   19,
    0,  138,  -17,  -17,    0,    0,  -40, -171,    0,   59,
   60,    0,    0, -107,   62,  -97, -174,  125,  -86, -156,
  -81, -189,    0,    0,    0,    0,    0,  -43,  -43,    0,
  -84,  147, -234,  132,    0,    0,    0,  148,  -75,   24,
  -79,  -73,  136, -156,  -72,  155,  139,  -71, -234,  140,
  -67,  -70,    0,  164,  -66,  145,    0,  -63,  165,  -64,
  150,  -57,    0,  -60,  154,  172,  -55,  -48,    0,  156,
  -47,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  234,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  198,    0,
    0,    0,    0,    0,   -6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -30,    0,    0,    0,    0,  115,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  200,    5,   11,    0,    0,    0,  119,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    6,   35,  -20,  229,    0,    4,    0,    0,    0,
  -80,    0,  167,    0,    0,    0,   48,    0,  -62,    0,
  -65,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,   27,
};
final static int YYTABLESIZE=245;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
   61,   61,   56,   23,   21,  102,   88,   77,  101,   76,
   43,   31,  107,  122,   57,   32,   21,  145,  115,   77,
   36,   76,    2,  117,   99,   35,   42,  147,   43,  100,
   19,    2,   89,   50,   12,   96,   62,   97,   62,   62,
   29,   40,   68,   71,   33,  129,   36,   60,   30,   60,
   47,   98,   62,   61,   37,   61,   39,   40,   90,  141,
   92,  155,  140,   60,  165,   48,   55,  164,   41,   61,
   21,   69,   54,   57,   45,  103,    6,    7,  135,    2,
    3,  105,  156,  157,    4,    2,    3,    5,   53,   42,
    4,   46,  120,    5,   43,   49,    2,  148,  149,   62,
   62,   79,   62,   62,  104,   81,    1,    2,    3,    6,
    7,  127,    4,  123,  124,    5,    6,    7,    8,  153,
   82,   57,   67,    2,    3,  125,  126,   83,    4,   80,
   84,    5,    6,    7,    8,   85,    1,    2,    3,   19,
    2,   86,    4,  169,  132,    5,    6,    7,    8,    2,
    3,   91,   93,   94,    4,  128,  160,    5,   95,    2,
    3,  134,  132,  136,    4,  108,  112,    5,  132,  113,
  132,  118,  174,  114,  116,  119,  121,   36,  133,  137,
  138,  142,  139,  143,  144,  151,  146,  152,  154,  158,
  159,  162,  161,  163,  166,  167,  168,  170,  171,  172,
  175,  176,  173,  177,  178,  180,  181,  179,  182,  183,
  184,  185,    2,  186,  187,  188,  191,   55,  189,   22,
  190,   87,   72,   73,   74,   75,  192,    6,    7,   60,
   60,    6,    7,    1,   72,   73,   74,   75,   18,   53,
   19,   34,    0,   55,  106,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,   45,   41,   40,    1,  125,   41,   60,  125,   62,
   41,    8,   78,   94,   35,   10,   13,  125,   84,   60,
   61,   62,  257,   86,   42,   40,   23,  125,   59,   47,
  256,  257,   53,   30,    0,   43,   43,   45,   45,   36,
   40,   44,   37,   40,   10,  108,   61,   43,   40,   45,
  256,   59,   59,   43,  123,   45,   59,   44,   55,   41,
   57,  142,   44,   59,   41,  271,  256,   44,  261,   59,
   67,   37,   59,   94,  261,   70,  266,  267,  256,  257,
  258,   78,  148,  149,  262,  257,  258,  265,   40,   86,
  262,  260,   89,  265,  125,  256,  257,  272,  273,   96,
   97,   41,   99,  100,   70,   41,  256,  257,  258,  266,
  267,  108,  262,   96,   97,  265,  266,  267,  268,  140,
   41,  142,  256,  257,  258,   99,  100,   41,  262,  123,
   61,  265,  266,  267,  268,   41,  256,  257,  258,  256,
  257,   59,  262,  164,  110,  265,  266,  267,  268,  257,
  258,   59,   41,   44,  262,  108,  153,  265,  274,  257,
  258,  114,  128,  116,  262,  123,   59,  265,  134,   59,
  136,   41,  169,  123,  123,  270,   59,   61,  125,   59,
  270,   44,   61,  125,  125,   61,  125,  274,  270,  274,
   44,   44,   61,  269,  274,  269,   61,  270,   44,   61,
   61,  269,  274,  274,   41,   61,  270,  274,   44,  274,
   61,  269,  257,  274,   61,   44,   61,  256,  274,  256,
  269,  256,  275,  276,  277,  278,  274,  266,  267,  274,
  274,  266,  267,    0,  275,  276,  277,  278,   41,  125,
   41,   13,   -1,  125,   78,
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
"cuerpoCompleto : encabezadoCOMP ELSE '{' bloqueElse '}'",
"encabezadoCOMP : '(' condicionIf ')' '{' bloqueThen '}'",
"bloqueElse : bloqueSentencia",
"condicionIf : condicion",
"bloqueThen : bloqueSentencia",
"cuerpoIncompleto : encabezadoINC",
"encabezadoINC : '(' condicionIf ')' '{' bloqueThenINC '}'",
"bloqueThenINC : condicion",
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

//#line 211 "gramatica.y"

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}

void disminuirAmbito(){
	String [] arreglo = compilador.Compilador.ambito.split("\\:"); 
	String aux = ""; 
	for(int i=0; i<arreglo.length-1; i++){
		aux = aux + arreglo[i]; 
	} 
	compilador.Compilador.ambito = aux;
}

boolean estaDeclarada(String sval){
	compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var"))
			return compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada();
		return true;
	}
	return false;
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


//#line 414 "Parser.java"
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
case 6:
//#line 24 "gramatica.y"
{mostrarMensaje("Reconocio declaracion de una o mas variables en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 8:
//#line 26 "gramatica.y"
{yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 9:
//#line 29 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); }
break;
case 10:
//#line 30 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); }
break;
case 11:
//#line 31 "gramatica.y"
{yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 12:
//#line 34 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea); /*disminuirAmbito();*/}
break;
case 13:
//#line 37 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); 
					setearProc(val_peek(11).sval); 
					setearAmbito(val_peek(11).sval); 
					compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(11).sval; setearAmbito(val_peek(8).sval);     }
break;
case 14:
//#line 41 "gramatica.y"
{ mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); 
			   		setearProc(val_peek(14).sval); 
			   		setearAmbito(val_peek(14).sval); 
			   		compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(14).sval; setearAmbito(val_peek(11).sval); 
			   		setearAmbito(val_peek(8).sval);   }
break;
case 15:
//#line 46 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); 
			   		setearProc(val_peek(17).sval); 
			   		setearAmbito(val_peek(17).sval); 
			   		compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(17).sval; setearAmbito(val_peek(14).sval); 
			   		setearAmbito(val_peek(11).sval); setearAmbito(val_peek(8).sval);   }
break;
case 16:
//#line 51 "gramatica.y"
{mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea); 
			   		setearProc(val_peek(9).sval); 
			   		setearAmbito(val_peek(9).sval); 
			   		compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(9).sval; }
break;
case 17:
//#line 55 "gramatica.y"
{yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 21:
//#line 63 "gramatica.y"
{mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);  setearAmbito(val_peek(0).sval); }
break;
case 22:
//#line 64 "gramatica.y"
{yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 23:
//#line 67 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 24:
//#line 68 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 30:
//#line 78 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 31:
//#line 79 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 32:
//#line 80 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 34:
//#line 82 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 35:
//#line 83 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 84 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 38:
//#line 88 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 39:
//#line 89 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 40:
//#line 92 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 42:
//#line 98 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval);Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp);  }
break;
case 43:
//#line 101 "gramatica.y"
{
			Par id1 = new Par(val_peek(2).sval); Par id2 = new Par(val_peek(0).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id1); polaca.agregarPaso(id2); polaca.agregarPaso(comp); }
break;
case 44:
//#line 104 "gramatica.y"
{
			Par id = new Par(val_peek(2).sval); Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id); polaca.agregarPaso(comp); }
break;
case 45:
//#line 109 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 46:
//#line 110 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 51:
//#line 121 "gramatica.y"
{mostrarMensaje("Reconocio IF con ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 53:
//#line 127 "gramatica.y"
{polaca.completarPolaca(PolacaInversa.getRetrocesosITE());}
break;
case 54:
//#line 130 "gramatica.y"
{
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBF = new Par("BF"); 
				polaca.agregarPaso(pasoBF);
				}
break;
case 55:
//#line 139 "gramatica.y"
{
				Par pasoEnBlanco = new Par(""); 
				polaca.agregarPaso(pasoEnBlanco);
				polaca.agregarPasoIncompleto();
				Par pasoBI = new Par("BI"); 
				polaca.agregarPaso(pasoBI);
				}
break;
case 56:
//#line 148 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 58:
//#line 154 "gramatica.y"
{polaca.completarPolaca(PolacaInversa.getRetrocesosIT()); }
break;
case 59:
//#line 156 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);
			Par id =  new Par(val_peek(3).sval);
			Par asig = new Par(val_peek(2).sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(asig); }
break;
case 60:
//#line 163 "gramatica.y"
{mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea); 
			Par suma =  new Par("+");
			polaca.agregarPaso(suma);  }
break;
case 61:
//#line 166 "gramatica.y"
{mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);
		    						Par resta =  new Par("-");
									polaca.agregarPaso(resta); }
break;
case 63:
//#line 172 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea); 
							  Par multi =  new Par("*");
							  polaca.agregarPaso(multi);		}
break;
case 64:
//#line 175 "gramatica.y"
{mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea); 
							  Par division =  new Par("/");
							  polaca.agregarPaso(division);}
break;
case 67:
//#line 182 "gramatica.y"
{Par id =  new Par(val_peek(0).sval);
					     polaca.agregarPaso(id); }
break;
case 68:
//#line 186 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 69:
//#line 187 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 70:
//#line 188 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 71:
//#line 189 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 72:
//#line 190 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 73:
//#line 191 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 74:
//#line 194 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 75:
//#line 195 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 76:
//#line 198 "gramatica.y"
{mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);
					            }
break;
case 77:
//#line 202 "gramatica.y"
{mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);
					 Par cte =  new Par(val_peek(0).sval);
					 polaca.agregarPaso(cte);            }
break;
case 78:
//#line 205 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);  
		  			 Par cte =  new Par("-"+val_peek(1).sval);
					 polaca.agregarPaso(cte);            }
break;
//#line 826 "Parser.java"
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
