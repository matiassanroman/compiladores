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

//#line 19 "Parser.java"




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
   23,   24,   13,   25,   25,   25,   26,   26,   26,   27,
   27,   22,   22,   22,   22,   22,   22,    4,    4,    7,
   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   13,   16,   19,   11,   12,    1,    3,    5,
    2,    2,    3,    3,    2,    2,    1,    1,    1,    5,
    5,    4,    3,    1,    5,    3,    7,    7,    7,    5,
    3,    3,    3,    3,    2,    2,    2,    1,    1,    1,
   10,    6,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,   70,    0,    0,    0,   69,   68,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   29,   34,   11,    0,
   10,    0,    0,    0,   49,   50,    0,    0,    0,    2,
    3,    0,    0,    0,    0,   12,    8,    0,   36,    0,
    0,   33,    0,    0,    0,    0,    0,    0,    0,    6,
    0,    0,    0,    0,    0,   71,    0,   61,   60,    0,
    0,   59,    0,   27,   28,    0,    9,   62,   63,   64,
   65,   66,   67,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   22,   32,   21,    0,    0,   72,
    0,    0,   53,    0,    0,   24,   23,   25,   26,    0,
   42,   44,    0,   35,   30,    0,   41,    0,    0,    0,
    0,    0,   31,    0,    0,    0,   57,   58,   48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   47,   38,   39,   37,    0,    0,   40,    0,    0,    0,
    0,   20,    0,   45,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   51,    0,   16,    0,    0,    0,   17,    0,    0,
    0,    0,    0,   13,    0,    0,    0,    0,    0,   14,
    0,    0,   15,
};
final static short yydgoto[] = {                          9,
   10,   11,  119,   13,   20,   14,   15,   16,   36,   54,
   55,   66,   17,   24,   18,   47,  120,   48,   41,  137,
   59,   74,   25,   26,   60,   61,   62,
};
final static short yysindex[] = {                      -177,
 -193,    0,  -37,  -15,   15,    0,    0, -235,    0, -177,
    0,    0, -193,    0,  -19,  -75,    0,    0,    0,   13,
    0, -231, -235, -202,    0,    0, -209, -152,   25,    0,
    0,   14,  -39,  -45, -155,    0,    0, -235,    0,  -53,
   28,    0,   35,   37,   59,   48,   76,   65,  -35,    0,
 -235,   68, -235,   90,   89,    0, -136,    0,    0,  -10,
  -18,    0, -102,    0,    0, -117,    0,    0,    0,    0,
    0,    0,    0,  -45,   19,   85,   97,   36,  -44,   40,
 -235,  117, -100, -235,    0,    0,    0,  109, -174,    0,
  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0,  112,
    0,    0, -128,    0,    0, -128,    0, -142,  118,  -92,
  120,   42,    0,  135,  -18,  -18,    0,    0,    0, -105,
  -93,   57,  -91, -154,  123,  -88, -145,  -85, -174,  -73,
    0,    0,    0,    0,  -44,  -44,    0,  -86,  145, -235,
  129,    0,   69,    0,    0,  147,  -76,   55,  -80, -128,
  -74,  136, -145,  -72,  152,  -82,  138,  -71, -235,  139,
  -68,    0,  -70,    0,  161,  -69,  146,    0,  -64,  164,
  -65,  149,  -58,    0,  -60,  154,  169,  -56,  -49,    0,
  155,  -48,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  233,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  193,    0,    0,    0,    0,    0,
   -7,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -14,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  194,    1,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -25,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    4,    5,  -21,  224,    0,    3,    0,    0,    0,
  -79,    0,  165,    0,    0,    0,  -89,    0,  157,    0,
  -61,    0,    0,    0,    0,   44,   52,
};
final static int YYTABLESIZE=239;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   57,   52,   23,   21,   12,   83,   73,   97,   72,  114,
   29,   53,  102,   30,   31,   21,  121,  107,  123,  130,
   33,    2,   96,   94,   27,   40,   43,   84,   95,   39,
   46,  132,   91,  134,   92,   56,   58,   56,   64,   65,
   67,   34,  162,   54,   43,   54,   43,   35,   93,  142,
   55,   56,   55,   85,   28,   87,   38,   38,   42,   54,
  156,   44,   19,    2,   49,   21,   55,   53,   75,   98,
   99,   37,   50,  144,  145,   76,  100,   77,    1,    2,
    3,   51,  128,   40,    4,  127,  112,    5,    6,    7,
    8,    6,    7,   58,   58,  154,   58,   58,  153,   78,
   63,    2,    3,   45,    2,  140,    4,   53,   79,    5,
    6,    7,    8,  122,    2,    3,   80,  135,  136,    4,
    6,    7,    5,   81,  131,  131,   86,  131,    2,    3,
   88,  159,   89,    4,  115,  116,    5,   90,    1,    2,
    3,  103,  148,  104,    4,  117,  118,    5,    6,    7,
    8,    2,    3,   19,    2,  105,    4,  110,  106,    5,
  131,  165,  108,    2,    3,    2,    3,  113,    4,  111,
    4,    5,   34,    5,    2,    3,  124,  125,  129,    4,
  126,  133,    5,  138,  141,  139,  143,  146,  147,  149,
  151,  150,  152,  155,  157,  161,  158,  160,  163,  166,
  167,  169,  164,  168,  170,  172,  171,  173,  174,  175,
  176,    2,  179,  177,  178,  182,   51,  180,   22,  181,
   82,   68,   69,   70,   71,  183,    6,    7,   56,   56,
    6,    7,    1,   18,   19,   52,   32,  109,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   40,    1,    0,   41,   60,  125,   62,   89,
    8,   33,   74,   10,   10,   13,  106,   79,  108,  125,
   40,  257,  125,   42,   40,   23,   41,   49,   47,  261,
   28,  125,   43,  125,   45,   43,   34,   45,   35,   35,
   38,   61,  125,   43,   59,   45,  256,  123,   59,  129,
   43,   59,   45,   51,   40,   53,   44,   44,  261,   59,
  150,  271,  256,  257,   40,   63,   59,   89,   41,   66,
   66,   59,   59,  135,  136,   41,   74,   41,  256,  257,
  258,  256,   41,   81,  262,   44,   84,  265,  266,  267,
  268,  266,  267,   91,   92,   41,   94,   95,   44,   41,
  256,  257,  258,  256,  257,  127,  262,  129,   61,  265,
  266,  267,  268,  256,  257,  258,   41,  272,  273,  262,
  266,  267,  265,   59,  120,  121,   59,  123,  257,  258,
   41,  153,   44,  262,   91,   92,  265,  274,  256,  257,
  258,  123,  140,   59,  262,   94,   95,  265,  266,  267,
  268,  257,  258,  256,  257,   59,  262,   41,  123,  265,
  156,  159,  123,  257,  258,  257,  258,   59,  262,  270,
  262,  265,   61,  265,  257,  258,   59,  270,   44,  262,
   61,  125,  265,   61,  270,  274,  260,  274,   44,   61,
   44,  123,  269,  274,  269,   44,   61,  270,   61,   61,
  269,   41,  274,  274,  274,  270,   61,   44,  274,   61,
  269,  257,   44,  274,   61,   61,  256,  274,  256,  269,
  256,  275,  276,  277,  278,  274,  266,  267,  274,  274,
  266,  267,    0,   41,   41,  261,   13,   81,   74,
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
"cuerpoCompleto : '(' condicion ')' '{' bloqueSentencia '}' ELSE '{' bloqueSentencia '}'",
"cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}'",
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

//#line 141 "gramatica.y"

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


//#line 399 "Parser.java"
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
//#line 12 "gramatica.y"
{mostrarMensaje("Reconoce bien el programa");
	System.out.println(polaca.toString());
}
break;
case 6:
//#line 21 "gramatica.y"
{mostrarMensaje("Reconocio declaracion de una o mas variables en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 8:
//#line 23 "gramatica.y"
{yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 9:
//#line 26 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); }
break;
case 10:
//#line 27 "gramatica.y"
{ setearAmbitoyDeclarada(val_peek(0).sval); }
break;
case 11:
//#line 28 "gramatica.y"
{yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 12:
//#line 31 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea); disminuirAmbito();}
break;
case 13:
//#line 34 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(11).sval); setearAmbito(val_peek(11).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(11).sval; setearAmbito(val_peek(8).sval); }
break;
case 14:
//#line 35 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(14).sval); setearAmbito(val_peek(14).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(14).sval; setearAmbito(val_peek(11).sval); setearAmbito(val_peek(8).sval); }
break;
case 15:
//#line 36 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(17).sval); setearAmbito(val_peek(17).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(17).sval; setearAmbito(val_peek(14).sval); setearAmbito(val_peek(11).sval); setearAmbito(val_peek(8).sval); }
break;
case 16:
//#line 37 "gramatica.y"
{mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea); setearProc(val_peek(9).sval); setearAmbito(val_peek(9).sval); compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(9).sval; }
break;
case 17:
//#line 38 "gramatica.y"
{yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 21:
//#line 46 "gramatica.y"
{mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);  setearAmbito(val_peek(0).sval); }
break;
case 22:
//#line 47 "gramatica.y"
{yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 23:
//#line 50 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 24:
//#line 51 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 30:
//#line 61 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 31:
//#line 62 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 32:
//#line 63 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 34:
//#line 65 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 35:
//#line 66 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 67 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 38:
//#line 71 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 39:
//#line 72 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 40:
//#line 75 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 45:
//#line 86 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 46:
//#line 87 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 51:
//#line 98 "gramatica.y"
{mostrarMensaje("Reconocio IF con cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 52:
//#line 101 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);
	
}
break;
case 53:
//#line 104 "gramatica.y"

{mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);
	String ladoIzq  = val_peek(3).sval;
	String operador = val_peek(2).sval;  // bien
	String ladoDer  = val_peek(1).sval;
	Par par =  new Par(ladoIzq);
	Par par2 = new Par(ladoDer);
	Par par3 = new Par(operador);
	polaca.agregarPaso(par);
	polaca.agregarPaso(par2);
	polaca.agregarPaso(par3);
}
break;
case 54:
//#line 107 "gramatica.y"
{mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 55:
//#line 108 "gramatica.y"
{mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 57:
//#line 112 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 58:
//#line 113 "gramatica.y"
{mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 59:
//#line 114 "gramatica.y"
//{ if(!estaDeclarada(val_peek(0).sval)){mostrarMensaje(val_peek(0).sval + " no esta declarada.");} }
break;
case 62:
//#line 121 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 63:
//#line 122 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 64:
//#line 123 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 65:
//#line 124 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 66:
//#line 125 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 67:
//#line 126 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 68:
//#line 129 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 69:
//#line 130 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 70:
//#line 133 "gramatica.y"
{mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea); }
break;
case 71:
//#line 136 "gramatica.y"
{mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea); setearAmbito(val_peek(0).sval); }
break;
case 72:
//#line 137 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea); setearAmbito(val_peek(1).sval); }
break;
//#line 732 "Parser.java"
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
// TODO Auto-generated catch block
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