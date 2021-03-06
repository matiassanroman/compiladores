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
import accionesSemanticas.*;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;
//#line 25 "Parser.java"




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
    0,    0,    1,    1,    1,    1,    2,    2,    5,    5,
    6,    8,    8,    8,    8,    8,    8,    9,   10,   10,
   10,   10,   10,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,   13,   14,   17,   16,   19,   19,
   19,   18,   18,   18,   15,   15,   12,   12,   22,   22,
   22,   22,   27,   28,   29,   29,   29,   24,   25,   26,
   23,   23,   11,   11,   11,   30,   30,   30,   31,   31,
   31,   32,   32,   21,   21,   21,   21,   21,   21,   21,
    4,    4,    7,   20,   20,   35,   33,   34,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    1,    1,    3,    1,    3,    1,
    2,    0,   11,   13,   16,   19,   20,    3,    2,    2,
    1,    1,    1,    1,    5,    5,    4,    5,    7,    9,
    5,    2,    2,    1,    7,    5,    1,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    2,    2,   10,    8,
    8,    6,    1,    1,    3,    3,    3,    1,    1,    1,
    6,    4,    4,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    0,    2,    2,
};
final static short yydefred[] = {                         0,
    0,   83,    0,    0,    0,   82,   81,    0,    0,    0,
    5,    6,    0,    8,    0,    0,   24,   34,    0,    0,
   32,    0,    0,    0,    0,    0,    0,    3,    4,    0,
   10,    0,    0,   33,    0,   11,   86,    0,   73,   72,
    0,    0,   71,   84,   85,    0,    0,   58,   47,   48,
    0,    0,    0,    0,    0,    0,    7,    0,    0,    0,
    0,   65,    0,    0,   21,   22,    0,   87,   88,    0,
    0,    0,    0,   80,   74,   75,   76,   77,   78,   79,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,   27,    0,    0,   63,   18,   19,   20,    0,    0,
   69,   70,    0,   55,   57,    0,   53,    0,   26,   25,
   38,    0,    0,    0,   37,    0,    0,   31,    0,   28,
   46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   45,    0,    0,   54,   52,   35,    0,   39,
   41,    0,    0,    0,   36,    0,    0,    0,    0,   29,
    0,    0,    0,   44,   42,   43,    0,    0,    0,    0,
    0,   51,   50,    0,    0,    0,    0,   30,    0,    0,
    0,    0,    0,   49,   13,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   14,    0,    0,    0,    0,
    0,    0,    0,    0,   15,    0,    0,    0,    0,    0,
   16,   17,
};
final static short yydgoto[] = {                          9,
   10,   11,  121,   13,   30,   14,   15,   16,   36,   67,
   17,   21,   18,   54,  152,   55,  114,  145,  115,   40,
   81,   22,   23,   47,  123,  153,  108,  137,   48,   41,
   42,   43,   44,   45,   68,
};
final static short yysindex[] = {                      -135,
   -7,    0,   12,   16,   31,    0,    0, -188,    0,   74,
    0,    0, -188,    0,  -25,  -45,    0,    0,  -35, -188,
    0, -181, -176, -238, -188,   47,   -7,    0,    0,  -18,
    0,  -29,  -43,    0,   87,    0,    0, -182,    0,    0,
  -20,   20,    0,    0,    0,  -32,   56,    0,    0,    0,
   61,   62,   45,   66,   49,  -37,    0, -188,   68,   53,
  -41,    0,  -21,   -7,    0,    0,  -60,    0,    0,  -35,
  -35,  -35,  -35,    0,    0,    0,    0,    0,    0,    0,
  -36, -116,   55,   57,  -34,  -10, -188, -151, -188,    0,
   70,    0, -188,   75,    0,    0,    0,    0,   20,   20,
    0,    0,   59,    0,    0, -174,    0, -125,    0,    0,
    0, -174,  -32,   77,    0,   83,   -9,    0,    9,    0,
    0, -174,   13,  -89,   64,  -36, -225, -149, -177, -123,
 -188,   86,    0, -112, -174,    0,    0,    0,   59,    0,
    0,  -34,  -34,  -34,    0,  115, -188,   99,  120,    0,
   54, -174,   37,    0,    0,    0, -105,   22, -104,  112,
 -174,    0,    0,  114, -177,  -91,  141,    0,   65,  -88,
 -188,  126,  -70,    0,    0,  -33,  -79,  139,  160,  -67,
  165,  -64,  -66,  151,  -52,    0,  157,  -55,  164,  -48,
  189,  -27,  197,  -19,    0,  -17,  181,  187,  -23,  -14,
    0,    0,
};
final static short yyrindex[] = {                       130,
  249,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  130,    0,    0,    0,    0,    0,
   40,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -74,    0,    0,  130,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,   27,
    0,    0,  213,    0,    0,    0,    0,   -6,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  140,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    3,    0,    0,    0,    0,  214,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  152,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,   10,   29,  -50,    0,    0,   85,    0,    0,    0,
  -65,  263,    0,    0,  -69,    0,    0,    0,    0,  -68,
  173,    0,    0,    0,    0,  129,    0,  148,    0,  258,
   24,   28,    0,    0,    0,
};
final static int YYTABLESIZE=355;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         94,
   68,   38,   93,   88,    1,   89,  106,  180,   38,   38,
   38,   60,  105,   66,   32,  104,  111,   51,   80,   28,
   79,   70,   70,   71,   71,   58,   67,   80,   12,   79,
  142,  130,   52,  135,  129,   33,  122,   95,   29,   64,
   57,   68,  125,   68,   65,   68,  143,  144,   23,  132,
   23,   20,  131,   19,   66,   24,   66,  141,   66,   68,
  140,   72,  166,   66,   96,  165,   73,   67,    2,   67,
   25,   67,   66,  154,  155,  156,   97,   35,  147,   49,
   64,   27,    2,    3,   50,   67,   56,    4,    6,    7,
    5,   69,   26,   99,  100,   98,   82,   31,   64,  101,
  102,   83,   84,   39,   46,   85,   86,   87,   91,   53,
  107,   92,  112,  109,  171,  110,   61,   39,  116,   33,
    1,    2,    3,   68,  146,   68,    4,   12,  118,    5,
    6,    7,    8,  120,  124,  127,   66,  134,   66,   27,
    2,    3,   90,  128,  150,    4,  148,  151,    5,   67,
  133,   67,  136,  133,   39,   39,   39,   39,  157,  159,
  160,  163,   64,  164,   64,  103,   27,    2,    3,  167,
  168,  113,    4,  117,  170,    5,  161,  119,  172,  136,
  133,   23,   23,   23,  173,  175,  177,   23,  138,  174,
   23,   23,   23,   23,  181,   27,    2,    3,  178,  182,
  183,    4,  184,  187,    5,    6,    7,    8,  185,  186,
  139,  188,   62,    2,   74,  149,  189,  190,  191,   27,
    2,    2,  179,   74,  192,  193,   59,    2,    6,    7,
   37,  158,  194,   75,   76,   77,   78,   37,   37,   37,
  196,  199,   75,   76,   77,   78,  195,  200,    2,  197,
  201,  198,   12,   56,   62,  176,   68,   68,   68,  202,
   68,   68,   68,   61,   59,   68,   68,   68,   68,   66,
   66,   66,   40,   66,   66,   66,   60,   34,   66,   66,
   66,   66,   67,   67,   67,  126,   67,   67,   67,  169,
   63,   67,   67,   67,   67,   64,   64,   64,  162,   64,
   64,   64,    0,    0,   64,   64,   64,   64,    0,   27,
    2,    3,    0,    0,    0,    4,    0,    0,    5,   27,
    2,    3,    0,    0,    0,    4,    0,    0,    5,   27,
    2,    3,    0,    0,    0,    4,    0,    0,    5,    6,
    7,    8,   64,    2,    3,    0,    0,    0,    4,    0,
    0,    5,    6,    7,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   45,   44,   41,    0,   56,  123,   41,   45,   45,
   45,   41,   81,    0,   40,   81,   85,  256,   60,   10,
   62,   43,   43,   45,   45,   44,    0,   60,    0,   62,
  256,   41,  271,  123,   44,   61,  106,   59,   10,    0,
   59,   41,  112,   43,   35,   45,  272,  273,  123,   41,
  125,   40,   44,   61,   41,   40,   43,  126,   45,   59,
  126,   42,   41,   35,  125,   44,   47,   41,  257,   43,
   40,   45,   59,  142,  143,  144,   67,  123,  129,  261,
   41,  256,  257,  258,  261,   59,   40,  262,  266,  267,
  265,  274,    8,   70,   71,   67,   41,   13,   59,   72,
   73,   41,   41,   19,   20,   61,   41,   59,   41,   25,
   82,   59,  123,   59,  165,   59,   32,   33,  270,   61,
  256,  257,  258,  123,  274,  125,  262,  123,   59,  265,
  266,  267,  268,   59,  260,   59,  123,  125,  125,  256,
  257,  258,   58,   61,   59,  262,  270,  260,  265,  123,
  122,  125,  124,  125,   70,   71,   72,   73,   44,   61,
   41,  125,  123,  269,  125,   81,  256,  257,  258,  274,
   59,   87,  262,   89,   61,  265,  123,   93,  270,  151,
  152,  256,  257,  258,   44,  274,   61,  262,  125,  125,
  265,  266,  267,  268,  274,  256,  257,  258,  269,   61,
   41,  262,  270,  270,  265,  266,  267,  268,   44,  274,
  126,   61,  256,  257,  256,  131,  269,   61,  274,  256,
  257,  257,  256,  256,   61,  274,  256,  257,  266,  267,
  274,  147,   44,  275,  276,  277,  278,  274,  274,  274,
   44,   61,  275,  276,  277,  278,  274,   61,    0,  269,
  274,  269,  123,   41,  261,  171,  256,  257,  258,  274,
  260,  261,  262,  261,  125,  265,  266,  267,  268,  256,
  257,  258,   59,  260,  261,  262,  125,   15,  265,  266,
  267,  268,  256,  257,  258,  113,  260,  261,  262,  161,
   33,  265,  266,  267,  268,  256,  257,  258,  151,  260,
  261,  262,   -1,   -1,  265,  266,  267,  268,   -1,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,  266,
  267,  268,  256,  257,  258,   -1,   -1,   -1,  262,   -1,
   -1,  265,  266,  267,  268,
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
"programa : error",
"bloquePrograma : bloquePrograma sentenciaDeclarativa",
"bloquePrograma : bloquePrograma sentenciaEjecutable",
"bloquePrograma : sentenciaDeclarativa",
"bloquePrograma : sentenciaEjecutable",
"sentenciaDeclarativa : tipo listaVariables ';'",
"sentenciaDeclarativa : declaracionProcedimiento",
"listaVariables : listaVariables ',' identificador",
"listaVariables : identificador",
"declaracionProcedimiento : encabezadoProc bloqueProc",
"encabezadoProc :",
"encabezadoProc : PROC identificador '(' ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador ')' NA '=' CTE ',' NS '=' CTE",
"encabezadoProc : PROC identificador '(' tipo identificador ',' tipo identificador ',' tipo identificador error ')' NA '=' CTE ',' NS '=' CTE",
"bloqueProc : '{' bloque '}'",
"bloque : bloque sentenciaDeclarativa",
"bloque : bloque sentenciaEjecutable",
"bloque : sentenciaDeclarativa",
"bloque : sentenciaEjecutable",
"bloque : error",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : OUT '(' CADENA ')' ';'",
"sentenciaEjecutable : OUT '(' error ')' ';'",
"sentenciaEjecutable : identificador '(' ')' ';'",
"sentenciaEjecutable : identificador '(' identificador ')' ';'",
"sentenciaEjecutable : identificador '(' identificador ',' identificador ')' ';'",
"sentenciaEjecutable : identificador '(' identificador ',' identificador ',' identificador ')' ';'",
"sentenciaEjecutable : identificador '(' error ')' ';'",
"sentenciaEjecutable : IF cuerpoIf",
"sentenciaEjecutable : identificador cuerpoIf",
"sentenciaEjecutable : cicloFor",
"cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}'",
"condicionFor : inicioFor ';' condiFOR ';' incDec",
"condiFOR : condicion",
"inicioFor : identificador '=' constante",
"condicion : identificador comparador asignacion",
"condicion : identificador comparador identificador",
"condicion : identificador comparador constante",
"incDec : UP constante",
"incDec : DOWN constante",
"incDec : error constante",
"bloqueSentencia : bloqueSentencia sentenciaEjecutable",
"bloqueSentencia : sentenciaEjecutable",
"cuerpoIf : cuerpoCompleto END_IF",
"cuerpoIf : cuerpoIncompleto END_IF",
"cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE '{' bloqueElse '}'",
"cuerpoCompleto : '(' condicionIf ')' senteciaUnicaThen ELSE '{' bloqueElse '}'",
"cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE senteciaUnicaElse",
"cuerpoCompleto : '(' condicionIf ')' senteciaUnicaThen ELSE senteciaUnicaElse",
"senteciaUnicaThen : sentenciaEjecutable",
"senteciaUnicaElse : sentenciaEjecutable",
"condicionDelIf : identificador comparador asignacion",
"condicionDelIf : identificador comparador identificador",
"condicionDelIf : identificador comparador constante",
"condicionIf : condicionDelIf",
"bloqueThen : bloqueSentencia",
"bloqueElse : bloqueSentencia",
"cuerpoIncompleto : '(' condicionIf ')' '{' bloqueThen '}'",
"cuerpoIncompleto : '(' condicionIf ')' senteciaUnicaThen",
"asignacion : identificador '=' expresion ';'",
"asignacion : error '=' expresion",
"asignacion : identificador '=' error",
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
"comparador : error",
"tipo : FLOAT",
"tipo : INTEGER",
"identificador : ID",
"constante : ctePositiva",
"constante : cteNegativa",
"$$1 :",
"ctePositiva : CTE $$1",
"cteNegativa : '-' CTE",
};

//#line 1150 "gramatica.y"

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////// DEFINICIONES PROPIAS///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Compilador c;
ArrayList<String> errores = new ArrayList<String>();
Token t;
int lineaActual;
ArrayList<String> reconocidos = new ArrayList<String>();
static PolacaInversa polaca = new PolacaInversa();

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
	if (error.equals("syntax error"))
		this.errores.add(error + " en linea " + this.lineaActual);
	else
		errores.add(error);
}

public ArrayList<String> getErrores(){
  return this.errores;
}

public ArrayList<String> getReconocidos(){
  return this.reconocidos;
}

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}

void comprobarRango(String sval, boolean negativo){
	double flotante;
	int entero;

	//ES NEGATIVO???
	if(negativo) {	
		//ES FLOAT Y NEGATIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			String aux = "-" + sval;
			if ( AS10_Verificar_Rango_Float.estaEnRango(aux) ) {			
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				String auxx = AS10_Verificar_Rango_Float.normalizar(flotante);
				
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setValor("-"+auxx);
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setTipo("float");
				compilador.Compilador.tablaSimbolo.get(auxx).get(compilador.Compilador.tablaSimbolo.get(auxx).size()-1).setUso("CTE");
				
				//compilador.Compilador.tablaSimbolo.get(auxx).remove(compilador.Compilador.tablaSimbolo.get(auxx).size()-1);
				/*
				Simbolo s = new Simbolo(AS10_Verificar_Rango_Float.normalizar(flotante));
				s.setValor("-"+s.getValor());
				s.setTipo("float");
				s.setUso("CTE");
				*/
				//compilador.Compilador.tablaSimbolo.put(s.getValor(),s);
				//compilador.Compilador.tablaSimbolo.get(auxx).add(s);
				//mostrarMensaje("CTE FLOAT negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				//mostrarMensaje("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
		//ES ENTERO Y NEGATIVO
		else{
			String aux = "-" + sval;
			if ( AS9_Verificar_Rango_Constante.estaEnRango(aux) ) {			
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setValor("-"+sval);
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("int");
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setUso("CTE");
				
				//compilador.Compilador.tablaSimbolo.put(s.getValor(),s);
				//compilador.Compilador.tablaSimbolo.get(sval).add(s);
				//mostrarMensaje("CTE ENTERA negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				//mostrarMensaje("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
	//ES POSITIVO	
	}else {
		// ES FLOAT Y POSTIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			if ( AS10_Verificar_Rango_Float.estaEnRango(sval) ){
				//mostrarMensaje("CTE FLOAT postiva esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				//mostrarMensaje("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
		// ES ENTERA Y POSITIVA
		else{
			if ( AS9_Verificar_Rango_Constante.estaEnRango(sval) ){
				//mostrarMensaje("CTE ENTERA postiva esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				//mostrarMensaje("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
				yyerror("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos. Error en linea: " + compilador.Compilador.nroLinea);
			}
		}
		
	}
}

void disminuirAmbito(){
	String [] arreglo = compilador.Compilador.ambito.split("\\@"); 
	String aux = ""; 
	for(int i=0; i<arreglo.length-1; i++){
		if(i == 0)
			aux = arreglo[i];
		else
			aux = aux + "@" + arreglo[i]; 
	} 
	compilador.Compilador.ambito = aux;
}

void setearVerificacionNa(String sval, String proc){
	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') 
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}
	
	compilador.Compilador.anidamientos.add(Integer.parseInt(sval));
	//int tamano = compilador.Compilador.anidamientos.size();
	/*
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++)
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " tiene problemas con los NA de: " + compilador.Compilador.anidamientos.get(i).getValor() + " Error en linea: " + compilador.Compilador.nroLinea);
			}
	*/

	/*
	System.out.println("INICIO");
	for(int i=0; i<compilador.Compilador.anidamientos.size(); i++) {
		System.out.println("NOMBRE: " + compilador.Compilador.anidamientosNombre.get(i));
		System.out.println("NA: " + compilador.Compilador.anidamientos.get(i));
	}
	System.out.println("FIN");
	
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++){
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " Error en linea: " + compilador.Compilador.nroLinea);
				break;
			}
		}
	*/
		
}

void verificacionNa(){

	int tamano = compilador.Compilador.anidamientos.size();
	if(tamano > 1)
		if(compilador.Compilador.anidamientos.get(tamano-2) <= compilador.Compilador.anidamientos.get(tamano-1)){
			String msj1 = "Conflicto en los niveles de anidamientos de los procedimientos." + ".\n";
			String msj2 = "Descripcion: el proc con el nombre: " + compilador.Compilador.anidamientosNombre.get(tamano-2) + " de la linea: " + compilador.Compilador.anidamientosLinea.get(tamano-2) + " tiene un NA: " + compilador.Compilador.anidamientos.get(tamano-2) + " y es menor o igual que el proc con el nombre: " + compilador.Compilador.anidamientosNombre.get(tamano-1) + " que esta en la linea: " + compilador.Compilador.anidamientosLinea.get(tamano-1) + " y tiene un Na: " + compilador.Compilador.anidamientos.get(tamano-1) + ".";
			yyerror(msj1 + msj2);
		}
}

boolean nameManglingNs(String sval) {
	
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	//System.out.println("USOOO:" + ambitoId);
	//Recorro la lista con todos los id con ese nombre
	//for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
	for(int i=compilador.Compilador.tablaSimbolo.get(sval).size()-1; i>=0; i--) {
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main") && (compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())) {
			//System.out.println("ACAAAAAAAAAAAAAAAAAAA: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				String [] arreglo = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().split("\\@");
				String idProc = arreglo[arreglo.length-1];
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(idProc).size(); j++){
						//System.out.println("entro");
						//System.out.println("ID DENTRO DE PROC NO DECLARADOS: " + compilador.Compilador.tablaSimbolo.get(sval).get(j).getValor());
						//Compruebo que el ambito del id del Proc este contenido
						String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
						String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(idProc).get(j).ambitoSinNombre();
						//System.out.println("ambitoSinVar: " + compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito());
						//System.out.println("ambitoSinProc: " + compilador.Compilador.tablaSimbolo.get(idProc).get(j).getAmbito());
						//System.out.println("NSSSSS: " + compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs());
						if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
							//Compruebo que el NS sea >= que la cantidad de anidamientos
							String [] id = ambitoSinNombreVar.split("\\@"); 
							String [] proc = ambitoSinNombreProc.split("\\@"); 
							//System.out.println("TAMANO: " + (id.length - proc.length));
							if(compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs() >= ((id.length - proc.length)-1)) {
								return true;
							}		
						}
				}
				return false;
			}
		}
	}
	return false;
}

int sePuedeUsar(String sval){
	//0 se puede usar
	//1 no esta al alcance.
	//2 esta redeclarada
	
	//Tomo el ambito de la id (asignacion)
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//Es una variable?
		if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var")){
			//No esta declarada?
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Veo si es un id que esta dentro del Proc para evaluar el NS
				if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(sval + "@Main")){
					if(nameManglingNs(sval))
						return 0;
				}
				//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
					//Compruebo que el id no sea proc y que el ambito sea Main
					if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main")) {
						if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())
							return 0;
					}
				}
				//No existe una id declarada al alcance.
				return 1;	
			}
			//Si esta declarada ver que no este Redeclarada.
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
		}
		else{
			//Veo si el id de Proc no esta declarado para buscar si existe en el ambito
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Recorro la lista de id de proc
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
					//Busco que esos id esten declarados
					if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada() && compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc")){
						String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
						String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
						//Pregunto si tienen el mismo ambito
						if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
							return 0;
						}
						//No se admite recursion
						String [] recurAux = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre().split("\\@");
						if(sval.equals(recurAux[recurAux.length-1])){
							//mostrarMensaje("No se permite recursion.");
							//yyerror("El Proc: " + sval + " intenta hacer recursion y no esta permitido. Error en linea: " + compilador.Compilador.nroLinea);
						}
						//Esta al alcance?
						if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
							return 0;							
						}
					}
				}
				return 1;
			}	
			else {
				//Si esta declarada ver que no este Redeclarada
				if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
						return 0;
				}else{
					for(int j=0; j<compilador.Compilador.tablaSimbolo.get(sval).size()-1; j++){
						if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(compilador.Compilador.tablaSimbolo.get(sval).get(j).getAmbito())){
							return 2;
						}
					}
				}
				return 0;	
			}			
		}
	}
	//Si no esta en la tabla de simbolos no existe ninguna declaracion.
	return 1;

}

void setearTipoParam(String sval){

	//Se setean todos las variables que son declaradas con su tipo
	Set<String> keys = c.getTablaSimbolo().keySet();
	Iterator<String> itr = keys.iterator();
	String str;
	
	while (itr.hasNext()) { 
		str = itr.next();
		Simbolo s = compilador.Compilador.tablaSimbolo.get(str).get(compilador.Compilador.tablaSimbolo.get(str).size()-1);

		if(s.getTipo().equals("Var") && s.isDeclarada() && s.getTipoParametro() == null)
			compilador.Compilador.tablaSimbolo.get(str).get(compilador.Compilador.tablaSimbolo.get(str).size()-1).setTipoParametro(sval);
	}

}
/*
boolean verificarParamFormales(String sval, String proc){
	//Verifico que el parametro real que venga este al alcance, declarado y que sea del mismo tipo que el de la definicion (parametro formal)
	if(sePuedeUsar(sval) == 0) {
		String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(proc).get(compilador.Compilador.tablaSimbolo.get(proc).size()-1).ambitoSinNombre();
		//Busco el proc declarado
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(proc).size(); i++) {
			if(compilador.Compilador.tablaSimbolo.get(proc).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(proc).get(i).isDeclarada()) {
				String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
				//Pregunto si tienen el mismo ambito
				if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
					return true;
				}
				//Esta al alcance?
				else if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
					return true;							
				}
			}		
		}
	}
	else
		return false;
}
*/


boolean verificarCantParam(String sval){

	int cantLlamdor = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getCantParametros();
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Busco que esos id esten declarados y sea Proc
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada()){
			String ambitoSinNombreLlamador = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
			String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(sval).get(i).ambitoSinNombre();
			//Pregunto si tienen el mismo ambito
			if(ambitoSinNombreLlamador.equals(ambitoSinNombreLlamado)) {
				if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getCantParametros() == cantLlamdor)
					return true;
			}
			//Esta al alcance?
			if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
				if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getCantParametros() == cantLlamdor)
					return true;							
			}
		}
	}
	return false;
}


boolean verficarCTEEnteras(String cte){
	
	if(cte.charAt(0) >= '0' && cte.charAt(0) <= '9') 
		if(cte.contains("_") && cte.contains("i")){
			return true;
		}

	return false;

}

boolean verficarNANSEnteras(String na, String ns){
	
	if(na.charAt(0) >= '0' && na.charAt(0) <= '9' && ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i") && ns.contains("_") && ns.contains("i")){
			return true;
		}

	return false;

}

void setearProc(String sval, String cantParametros, String na, String ns){

	if(na.charAt(0) >= '0' && na.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i")){
			na = na.toString().substring(0, na.length()-2); 
		}
	
	if(ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(ns.contains("_") && ns.contains("i")){
			ns = ns.toString().substring(0, ns.length()-2); 
		}

	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setCantParametros(Integer.parseInt(cantParametros));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNa(Integer.parseInt(na));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNs(Integer.parseInt(ns));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);

	if (na.equals(ns)) {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setTipo("NA_PROC");
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setTipo("NS_PROC");
	}
	else {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-1).setTipo("NA_PROC");
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setTipo("NS_PROC");
	}

}

void setearAmbitoNaNs(String na, String ns){

	if(na.charAt(0) >= '0' && na.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i")){
			na = na.toString().substring(0, na.length()-2); 
		}
	
	if(ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(ns.contains("_") && ns.contains("i")){
			ns = ns.toString().substring(0, ns.length()-2); 
		}

	if (na.equals(ns)) {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-2).setAmbito(compilador.Compilador.ambito,false);
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setAmbito(compilador.Compilador.ambito,false);
	}
	else {
		compilador.Compilador.tablaSimbolo.get(na).get(compilador.Compilador.tablaSimbolo.get(na).size()-1).setAmbito(compilador.Compilador.ambito,false);
		compilador.Compilador.tablaSimbolo.get(ns).get(compilador.Compilador.tablaSimbolo.get(ns).size()-1).setAmbito(compilador.Compilador.ambito,false);
	}

}

void setearAmbito(String sval){
	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') {
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}
		else if(sval.contains("f") || sval.contains(".")){
			Double flotante = Double.parseDouble(sval.replace('f', 'E'));
			if (sval.contains("f")) {
				if(String.valueOf(flotante).contains("E"))
					sval = String.valueOf(flotante).replace('E', 'f');
			}
			if (sval.contains("."))
				sval = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Proc") && !(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada())){
		/*
		for(int i=compilador.Compilador.tablaSimbolo.get(sval).size()-1; i>=0; i--){
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada()){
				compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
				break;
			}
		}*/
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito();
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var") && !(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada())){
		//compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito();
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getUso().equals("CADENA")){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
	}	

}

void setearAmbitoyDeclarada(String sval, String tipo){
	if(tipo.equals("")){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
	}
	else{
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false); 
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setDeclarada(true);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("PARAM_PROC");
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipoParametro(tipo);
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setPasajeParametro("COPIA VALOR");
	}
}

String getAmbitoVerdaderoVerdadero(String sval) {
	
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Recorro la lista con todos los id con ese nombre
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main") && (compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())) {
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				String [] arreglo = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().split("\\@");
				String idProc = arreglo[arreglo.length-1];
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(idProc).size(); j++){
					//Compruebo que el ambito del id del Proc este contenido
					String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
					String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(idProc).get(j).ambitoSinNombre();
					if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
						//Compruebo que el NS sea >= que la cantidad de anidamientos
						String [] id = ambitoSinNombreVar.split("\\@"); 
						String [] proc = ambitoSinNombreProc.split("\\@"); 
						if(compilador.Compilador.tablaSimbolo.get(idProc).get(j).getNs() >= ((id.length - proc.length)-1)) {
							return compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito();
						}
							
					}
				}
				return "";
			}
		}
	}
	return "";
}

String getAmbitoVerdadero(String sval){
	
	//Tomo el ambito de la id (asignacion)
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//No esta declarada?
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
			//Veo si es un id que esta dentro del Proc para evaluar el NS
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito().equals(sval + "@Main")){
				if(!getAmbitoVerdaderoVerdadero(sval).equals(""))
					return getAmbitoVerdaderoVerdadero(sval);
			}
			//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
			for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
				//Compruebo que el id no sea proc y que el ambito sea Main
				if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main")) {
					if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())
						return compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito();
				}
			}	
		}
	}
	return "";
}

String getAmbitoProc(String ambitoGeneral) {
	String [] arreglo = ambitoGeneral.split("\\@");
	String aux = arreglo[arreglo.length-1];
	//System.out.println("AUX: " + aux);
	for(int i=0; i<arreglo.length-1; i++)
		aux = aux + "@" + arreglo[i];
	//System.out.println("AUX 2: " + aux);
	return aux;	
}

int getNsProc(String ambitoProc, String sval) {
	String [] arreglo = ambitoProc.split("\\@");
	String aux = sval;
	for(int j=0; j<arreglo.length-1; j++)
		aux = aux + "@" + arreglo[j];
	
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(aux))
			return compilador.Compilador.tablaSimbolo.get(sval).get(i).getNs();
	}
	return 0;
}

int getNivelVar(String sval) {
	String [] arreglo = sval.split("\\@");
	return arreglo.length-1;
}

boolean dadoProcVerDeclaracionVar(String ambitoProc, String sval) {
	String [] ambitoProcedimiento = ambitoProc.split("\\@");
	String ambitoVar = sval;
	if(ambitoProcedimiento.length > 1) {
		for(int i=1; i<ambitoProcedimiento.length; i++) {
			ambitoVar =  ambitoVar + "@" + ambitoProcedimiento[i];
		}
		ambitoVar = ambitoVar + "@" + ambitoProcedimiento[0];
	}
	else {
		ambitoVar = ambitoVar + "@" + ambitoProc;
	}
	
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(ambitoVar))
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada() && (compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Var") || compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("PARAM_PROC")))
				return true;				
	}
	return false;				
}

String construirAmbito (String[] array) {
	String delimiter = "@";
    return String.join(delimiter, array);
}

String[] construirAmbitoMenosUltimo (String[] array) {
	String aux = "";
	for(int j=0; j<array.length-1; j++)
		if(j==0)
			aux = array[j];
		else
			aux = aux + "@" + array[j];
	return aux.split("\\@");
	
}

String construirAmbitoMenosUltimoString (String array2) {
	String [] array = array2.split("\\@");
	String aux = "";
	for(int j=0; j<array.length-1; j++)
		if(j==0)
			aux = array[j];
		else
			aux = aux + "@" + array[j];
	String delimiter = "@";
    return String.join(delimiter, aux);
	
}

boolean verificarAnidamientos(String ambitoProc, String ambitoVar, int ns) {	
	String proc = "";
	String [] aux = ambitoProc.split("\\@");
	for(int i=1; i<aux.length; i++)
		if(i==1)
			proc = aux[i];
		else
			proc = proc + "@" + aux[i];
	
	String var = "";
	String [] aux2 = ambitoVar.split("\\@");
	for(int i=1; i<aux2.length; i++)
		if(i==1)
			var = aux2[i];
		else
			var = var + "@" + aux2[i];
	
	//En proc tengo f2@main@f1 => main@f1@f2
	//Nivel son las veces que me voy metiendo hasta encontrar donde esta declarado
	//var es el ambito de la variable ax1@main@f1@f2 => main@f1@f2
	proc = proc + "@" + aux[0];
	int nivel = 0;
	
	while(var.length() > 0) {
		if(var.equals(proc)) {
			if(ns >= nivel) {
				return true;
			}
			else {
				return false;
			}
		}
		var = construirAmbitoMenosUltimoString(var);
		nivel++;
	}
	
	return false;
}

public String ambitoSinNombre(String sval) {
	String [] arreglo = sval.split("\\@");
	String auxSinNombre = "";
	boolean primero = true;
	for(int z=1; z<arreglo.length; z++) {
		if(primero) {
			primero = false;
			auxSinNombre = arreglo[z];
		}
		else
			auxSinNombre = auxSinNombre + "@" + arreglo[z];
	}
	return auxSinNombre;
}

/*
boolean verificarCantParam(String alcanceProc, int cantParam){

	//sval llamado con su ambito => b@Main@a@b
	String [] nombreProc = alcanceProc.split("\\@");

	for(int i=compilador.Compilador.tablaSimbolo.get(nombreProc[0]).size()-1; i>=0; i--){
		if((compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).isDeclarada()) && (compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getTipo().equals("Proc")))
			if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito().equals(alcanceProc))
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return true;
	}
	return false;
}
*/

String comprobarAlcanceProc(String sval, int cantParam){

	//sval llamador con su ambito => b@Main@a@b
	String [] nombreProc = sval.split("\\@");
	String ambitoSinNombreLlamador = ambitoSinNombre(sval);
	for(int i=compilador.Compilador.tablaSimbolo.get(nombreProc[0]).size()-1; i>=0; i--){
		//Si es un Proc declarado
		if((compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).isDeclarada()) && (compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getTipo().equals("Proc"))){
			//Caso Hermano: llamador esta en el mismo ambito que el llamado.
			if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito().equals(sval)){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return sval;
			}
			//Caso Recursivo: llamador esta dentro del llamado.
			else if(sval.equals(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito()+nombreProc[0])){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito();
			}
			String ambitoSinNombreLlamado = compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).ambitoSinNombre();
			//Esta al alcance?
			if(ambitoSinNombreLlamador.indexOf(ambitoSinNombreLlamado) != -1){
				if(compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getCantParametros() == cantParam)
					return compilador.Compilador.tablaSimbolo.get(nombreProc[0]).get(i).getAmbito();							
			}
		}
	}
	return "";	
}

String comprobarAlcance(String sval) {
	String ambito = "Main" + compilador.Compilador.ambito;
	String [] ambitoAux = ambito.split("\\@");
	String ambitoUsoVar = sval + "@" + ambito;
	String [] ambitoArr = ambito.split("\\@");
	boolean primero = true;
	for(int i = ambitoArr.length-1; i>=0; i--) {
		if(!ambitoArr[i].equals("Main")) {
			if(primero) {
				primero = false;
				if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
					//System.out.println("SALIDA 1: " + sval + "@" + construirAmbito(ambitoAux));
					return sval + "@" + construirAmbito(ambitoAux);
				}
					
			}
			else {
				if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
					if(verificarAnidamientos(getAmbitoProc(construirAmbito(ambitoAux)), ambitoUsoVar,getNsProc(construirAmbito(ambitoAux),ambitoArr[i]))) {
						//System.out.println("SALIDA 2: " + sval + "@" + construirAmbito(ambitoAux));
						return sval + "@" + construirAmbito(ambitoAux);
					}
				}
			}
		}
		else {
			if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
				//System.out.println("SALIDA 3: " + sval + "@" + construirAmbito(ambitoAux));
				return sval + "@" + construirAmbito(ambitoAux);
			}
		}
		
		ambitoAux = construirAmbitoMenosUltimo(ambitoAux);
	}
	return "";		
}

boolean verficarIDEnteras(String id){
	
	String [] var = id.split("\\@");
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(var[0]).size(); i++){
		if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getAmbito().equals(id))
			if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getTipoParametro() != null)
				if(compilador.Compilador.tablaSimbolo.get(var[0]).get(i).getTipoParametro().equals("INTEGER"))
					return true;
	}

	return false;

}

boolean erroresFor(){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains("for"))
			return true;

	return false;
}

void erroresForAsignacion(int linea){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains(String.valueOf(linea)))
			this.errores.set(i,"El id de la comparacion del for: " + this.errores.get(i));
}

void erroresIfAsignacion(int linea){
	for(int i=0; i<this.errores.size(); i++)
		if(this.errores.get(i).contains(String.valueOf(linea)))
			this.errores.set(i,"El id de la comparacion del if: " + this.errores.get(i));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////FIN DEFINICIONES PROPIAS////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//#line 1256 "Parser.java"
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
//#line 19 "gramatica.y"
{
	mostrarMensaje("Reconoce bien el programa");
}
break;
case 2:
//#line 23 "gramatica.y"
{
	yyerror("Programa invalido, error en linea: " + compilador.Compilador.nroLinea);
}
break;
case 3:
//#line 29 "gramatica.y"
{
}
break;
case 4:
//#line 32 "gramatica.y"
{
}
break;
case 5:
//#line 35 "gramatica.y"
{
}
break;
case 6:
//#line 38 "gramatica.y"
{
}
break;
case 7:
//#line 43 "gramatica.y"
{
	setearTipoParam(val_peek(2).sval);
}
break;
case 8:
//#line 47 "gramatica.y"
{
	Par retorno = new Par("RET");
	polaca.agregarPaso(retorno);
}
break;
case 9:
//#line 54 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		yyerror(val_peek(0).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 10:
//#line 61 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		yyerror(val_peek(0).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 11:
//#line 70 "gramatica.y"
{
	disminuirAmbito();
	if(!(compilador.Compilador.anidamientos.size() == 0)){
		verificacionNa();
		compilador.Compilador.anidamientos.remove(compilador.Compilador.anidamientos.size()-1);
		compilador.Compilador.anidamientosNombre.remove(compilador.Compilador.anidamientosNombre.size()-1);
		compilador.Compilador.anidamientosLinea.remove(compilador.Compilador.anidamientosLinea.size()-1);
	}
}
break;
case 13:
//#line 82 "gramatica.y"
{
	if(verficarNANSEnteras(val_peek(4).sval, val_peek(0).sval)){
		setearProc(val_peek(9).sval, "0", val_peek(4).sval, val_peek(0).sval);
		/*setearAmbito($2.sval);*/
		PolacaInversa.subirNivelProc();
		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(10).sval+" "+ compilador.Compilador.tablaSimbolo.get(val_peek(9).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(9).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" + val_peek(9).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(9).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(9).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add(val_peek(9).sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa(val_peek(4).sval,val_peek(9).sval);
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 14:
//#line 106 "gramatica.y"
{
	
	/*mostrarMensaje("Procedimiento con 1 parametro en linea nro: "+compilador.Compilador.nroLinea);*/
	if(verficarNANSEnteras(val_peek(4).sval, val_peek(0).sval)){
		setearProc(val_peek(11).sval, "1", val_peek(4).sval, val_peek(0).sval);
		/*setearAmbito($2.sval);*/
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		/*polaca.agregarParametro($1.sval+" "+$2.sval);*/
		/*polaca.agregarParametro($5.sval);*/
		polaca.agregarParametro(val_peek(12).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
				
		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(12).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  val_peek(11).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(11).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(11).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add(val_peek(11).sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa(val_peek(4).sval,val_peek(11).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 15:
//#line 139 "gramatica.y"
{

	/*mostrarMensaje("Procedimiento con 2 parametros en linea nro: "+compilador.Compilador.nroLinea);*/
	if(verficarNANSEnteras(val_peek(4).sval, val_peek(0).sval)){
		setearProc(val_peek(14).sval, "2", val_peek(4).sval, val_peek(0).sval);
		/*setearAmbito($2.sval);*/
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		/*polaca.agregarParametro($1.sval+" "+$2.sval);*/
		/*polaca.agregarParametro($5.sval);*/
		/*polaca.agregarParametro($8.sval);*/
		polaca.agregarParametro(val_peek(15).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).size()-1).getAmbito());
		
		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(15).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  val_peek(14).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(14).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(14).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add(val_peek(14).sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa(val_peek(4).sval,val_peek(14).sval);
		setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 16:
//#line 175 "gramatica.y"
{
	
	/*mostrarMensaje("Procedimiento con 3 parametros en linea nro: "+compilador.Compilador.nroLinea);*/
	if(verficarNANSEnteras(val_peek(4).sval, val_peek(0).sval)){
		setearProc(val_peek(17).sval, "3", val_peek(4).sval, val_peek(0).sval);
		/*setearAmbito($2.sval);*/
		PolacaInversa.subirNivelProc();
		polaca.agregarParametro(Integer.toString(PolacaInversa.nivelProc));
		/*polaca.agregarParametro($1.sval+" "+$2.sval);*/
		/*polaca.agregarParametro($5.sval);*/
		/*polaca.agregarParametro($8.sval);*/
		/*polaca.agregarParametro($11.sval);*/
		polaca.agregarParametro(val_peek(18).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).size()-1).getAmbito());
		
		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(18).sval +" "+compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" + val_peek(17).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(17).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(17).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		compilador.Compilador.anidamientosNombre.add(val_peek(17).sval);
		compilador.Compilador.anidamientosLinea.add(compilador.Compilador.nroLinea);
		setearVerificacionNa(val_peek(4).sval,val_peek(17).sval);
		setearAmbitoyDeclarada(val_peek(14).sval,val_peek(15).sval);
		setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 17:
//#line 214 "gramatica.y"
{
	yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);
}
break;
case 18:
//#line 220 "gramatica.y"
{
	PolacaInversa.bajarNivelProc();
	/*polaca.borrarProcYParametros();*/
	/*int posProc = polaca.inicioProc();*/
}
break;
case 19:
//#line 228 "gramatica.y"
{
}
break;
case 20:
//#line 231 "gramatica.y"
{
}
break;
case 21:
//#line 234 "gramatica.y"
{
}
break;
case 22:
//#line 237 "gramatica.y"
{
}
break;
case 23:
//#line 240 "gramatica.y"
{
	yyerror("Error: no puede haber un seccion vacia, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 24:
//#line 246 "gramatica.y"
{
}
break;
case 25:
//#line 249 "gramatica.y"
{
	/*mostrarMensaje("Sentencia OUT, en linea " + compilador.Compilador.nroLinea);*/
	setearAmbito(val_peek(2).sval);
	Par out = new Par(val_peek(4).sval);
	Par cadena = new Par(val_peek(2).sval);
	polaca.agregarPaso(cadena);
	polaca.agregarPaso(out);
}
break;
case 26:
//#line 258 "gramatica.y"
{
	yyerror("Error: Formato de cadena incorrecto, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 27:
//#line 262 "gramatica.y"
{
	compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).setCantParametros(0);
	setearAmbito(val_peek(3).sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).getAmbito(),0);

	/*Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.*/
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + val_peek(3).sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		/*Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());*/
		Par nomProc =  new Par(alcanceProc);
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(0);
	setearAmbito($1.sval);

	//Par nomProc = new Par($1.sval); 
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);

	//Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado
	int aux = sePuedeUsar($1.sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam($1.sval)){
			mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
			yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	*/

}
break;
case 28:
//#line 313 "gramatica.y"
{
	compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).setCantParametros(1);
	setearAmbito(val_peek(4).sval);
	setearAmbito(val_peek(2).sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).getAmbito(),1);
	String aux2 = comprobarAlcance(val_peek(2).sval); 
	
	/*Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.*/
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + val_peek(4).sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	/*Compruebo que los parametros formales esten al alcance.*/
	else if(aux2.equals("")){
		yyerror("Procedimiento: " + val_peek(4).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2));
		/*polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito(),1), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());*/
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(alcanceProc), compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).getAmbito());

		/*Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());*/
		Par nomProc =  new Par(alcanceProc);
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(1);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("")){
		//mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado.");
		yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/
}
break;
case 29:
//#line 383 "gramatica.y"
{

	compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).setCantParametros(2);
	setearAmbito(val_peek(6).sval);
	setearAmbito(val_peek(4).sval);
	setearAmbito(val_peek(2).sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).getAmbito(),2);
	String aux2 = comprobarAlcance(val_peek(4).sval); 
	String aux3 = comprobarAlcance(val_peek(2).sval);

	/*Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.*/
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + val_peek(6).sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	/*Compruebo que los parametros formales esten al alcance.*/
	else if(aux2.equals("") || aux3.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + val_peek(6).sval + " tiene el parametro real " + val_peek(4).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux3.equals(""))
			yyerror("Procedimiento: " + val_peek(6).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(alcanceProc), compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).getAmbito());

		/*Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());*/
		Par nomProc =  new Par(alcanceProc);
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(2);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/

}
break;
case 30:
//#line 464 "gramatica.y"
{
	compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).setCantParametros(3);
	setearAmbito(val_peek(8).sval);
	setearAmbito(val_peek(6).sval);
	setearAmbito(val_peek(4).sval);
	setearAmbito(val_peek(2).sval);
	String alcanceProc = comprobarAlcanceProc(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito(),3);
	String aux2 = comprobarAlcance(val_peek(6).sval); 
	String aux3 = comprobarAlcance(val_peek(4).sval);
	String aux4 = comprobarAlcance(val_peek(2).sval);

	/*Compruebo que el nombre del llamador este al alcance y la cantidad de parametros sea la correcta.*/
	if(alcanceProc == ""){
		yyerror("Procedimiento: " + val_peek(8).sval + " No esta declarado o no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	/*Compruebo que los parametros formales esten al alcance.*/
	else if(aux2.equals("") || aux3.equals("") || aux4.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(6).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux3.equals(""))
			yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(4).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		if(aux4.equals(""))
			yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3,aux4));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(alcanceProc), compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());

		/*Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());*/
		Par nomProc =  new Par(alcanceProc);
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}

	/*
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).setCantParametros(3);
	setearAmbito($1.sval);
	setearAmbito($3.sval);
	setearAmbito($5.sval);
	setearAmbito($7.sval);
	int aux = sePuedeUsar($1.sval);
	String aux2 = comprobarAlcance($3.sval); 
	String aux3 = comprobarAlcance($5.sval);
	String aux4 = comprobarAlcance($7.sval);

	//Compruebo que el nombre del llamador este al alcance.
	if(aux == 1 || aux == 2){
		if(aux == 1){
			//mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");
			yyerror("Procedimiento: " + $1.sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			//mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");
			yyerror("Procedimiento " + $1.sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	//Compruebo que el numero de parametros del llamado coincida con el llamador.
	else if(!verificarCantParam($1.sval)){
		//mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");
		yyerror("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	//Compruebo que los parametros formales esten al alcance.
	else if(aux2.equals("") || aux3.equals("") || aux4.equals("")){
		if(aux2.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else if(aux3.equals(""))
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("Procedimiento: " + $1.sval + " tiene el parametro real " + $7.sval +  " No declarado o es el Id de una funcion. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(aux2,aux3,aux4));
		polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito()), compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());

		//Par nomProc = new Par($1.sval); 
		Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par call = new Par("CALL");
		polaca.agregarPaso(nomProc);
		polaca.agregarPaso(call);
	}
	*/

}
break;
case 31:
//#line 552 "gramatica.y"
{
	yyerror("Error: Cantidad no permitida de parametros, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 32:
//#line 556 "gramatica.y"
{
	if (PolacaInversa.getFlagITE()){
		polaca.completarPolaca(PolacaInversa.getRetrocesosITE());
	}
	else
		polaca.completarPolaca(PolacaInversa.getRetrocesosIT());
	polaca.agregarLabel();
}
break;
case 33:
//#line 566 "gramatica.y"
{
	yyerror("Error: las palabras reservadas van en mayuscula, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 34:
//#line 571 "gramatica.y"
{
	/*mostrarMensaje("Ciclo FOR en linea nro: " + compilador.Compilador.nroLinea);*/
}
break;
case 35:
//#line 577 "gramatica.y"
{
	if(!erroresFor()){
		polaca.borrarVariablesControl();
		Par pasoEnBlanco = new Par("");
		polaca.agregarPaso(pasoEnBlanco);
		polaca.agregarPasoIncompleto();
		Par pasoBI = new Par("BI");
		polaca.agregarPaso(pasoBI);
		/*polaca.agregarLabel();*/
		polaca.completarFOR();
		polaca.borrarInicioFOR();
		polaca.borrarPasoIncompleto();
		polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());
		polaca.agregarLabel();
	}
}
break;
case 36:
//#line 596 "gramatica.y"
{
	if(!erroresFor()){
		polaca.borrarPasoPolaca();
	}
}
break;
case 37:
//#line 604 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 38:
//#line 614 "gramatica.y"
{		
	setearAmbito(val_peek(2).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 
	if(!aux.equals("")){
		if(verficarIDEnteras(aux) && verficarCTEEnteras(val_peek(0).sval)){
			polaca.agregarVariableControl(aux);
			Par id = new Par(aux);
			polaca.agregarPaso(id);
			Par asig = new Par(val_peek(1).sval);
			polaca.agregarPaso(asig);
			polaca.agregarInicioFOR();
			polaca.agregarLabel();
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarCTEEnteras(val_peek(0).sval)){
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("La CTE de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else 
				yyerror("El identificador de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror("El identificador del inicio del for: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 39:
//#line 645 "gramatica.y"
{	
	erroresForAsignacion(compilador.Compilador.nroLinea);
	setearAmbito(val_peek(2).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 
	if(!aux.equals("")){
		if(verficarIDEnteras(aux)){
			Par id = new Par(aux);
			Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(comp);
		}
		else{
			yyerror("El identificador de la comparacion del for: " +val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}	
	}
	else{
		yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 40:
//#line 665 "gramatica.y"
{
	
	setearAmbito(val_peek(2).sval);
	setearAmbito(val_peek(0).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 
	String aux2 = comprobarAlcance(val_peek(0).sval); 

	if(!aux.equals("") && !aux2.equals("")){
		if(verficarIDEnteras(aux) && verficarIDEnteras(aux2)){
			Par id1 =  new Par(aux);
			Par id2 =  new Par(aux2);
			Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id1);
			polaca.agregarPaso(id2);
			polaca.agregarPaso(comp);
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarIDEnteras(aux2)){
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("El identificador de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else 
				yyerror("El identificador de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(aux.equals("") && aux2.equals("")){
			yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
			yyerror("El identificador de la comparacion del for: " + val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else if(aux.equals(""))
			yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("El identificador de la comparacion del for: " + val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 41:
//#line 704 "gramatica.y"
{
	setearAmbito(val_peek(2).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 

	if(!aux.equals("")){
		if(verficarIDEnteras(aux) && verficarCTEEnteras(val_peek(0).sval)){
			Par id =  new Par(aux);
			Par comp = new Par(val_peek(1).sval);
			polaca.agregarPaso(id);
			polaca.agregarPaso(comp);
		}
		else{
			if(!verficarIDEnteras(aux) && !verficarCTEEnteras(val_peek(0).sval)){
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
				yyerror("La CTE de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			}
			else if(!verficarIDEnteras(aux))
				yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
			else
				yyerror("La CTE de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror("El identificador de la comparacion del for: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		if(!verficarCTEEnteras(val_peek(0).sval))
			yyerror("La CTE de la comparacion del for: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 42:
//#line 735 "gramatica.y"
{	
	if(verficarCTEEnteras(val_peek(0).sval)){
		polaca.agregarVariableControl("+");
		polaca.agregarVariableControl(val_peek(0).sval);
	}
	else
		yyerror("CTE del UP del for No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
}
break;
case 43:
//#line 744 "gramatica.y"
{
	if(verficarCTEEnteras(val_peek(0).sval)){
		polaca.agregarVariableControl("-");
		polaca.agregarVariableControl(val_peek(0).sval);
	}
	else
		yyerror("CTE del DOWN del for No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
}
break;
case 44:
//#line 753 "gramatica.y"
{
	yyerror("Error: incremento/decremento mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 45:
//#line 759 "gramatica.y"
{
}
break;
case 46:
//#line 762 "gramatica.y"
{
}
break;
case 47:
//#line 767 "gramatica.y"
{
	PolacaInversa.setFlagITE(true);
}
break;
case 48:
//#line 771 "gramatica.y"
{
	PolacaInversa.setFlagITE(false); 
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoIncompleto();
}
break;
case 49:
//#line 781 "gramatica.y"
{
}
break;
case 50:
//#line 784 "gramatica.y"
{
}
break;
case 51:
//#line 787 "gramatica.y"
{
}
break;
case 52:
//#line 790 "gramatica.y"
{
}
break;
case 53:
//#line 796 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
}
break;
case 54:
//#line 807 "gramatica.y"
{
}
break;
case 55:
//#line 816 "gramatica.y"
{	
	erroresIfAsignacion(compilador.Compilador.nroLinea);
	setearAmbito(val_peek(2).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		Par comp = new Par(val_peek(1).sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(comp);	
	}
	else{
		yyerror("El identificador de la comparacion del if: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 56:
//#line 831 "gramatica.y"
{	
	setearAmbito(val_peek(2).sval);
	setearAmbito(val_peek(0).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 
	String aux2 = comprobarAlcance(val_peek(0).sval); 

	if(!aux.equals("") && !aux2.equals("")){
		Par id1 =  new Par(aux);
		Par id2 =  new Par(aux2);
		Par comp = new Par(val_peek(1).sval);
		polaca.agregarPaso(id1);
		polaca.agregarPaso(id2);
		polaca.agregarPaso(comp);
	}
	else{
		if(aux.equals("") && aux2.equals("")){
			yyerror("El identificador de la comparacion del if: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
			yyerror("El identificador de la comparacion del if: " + val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else if(aux.equals(""))
			yyerror("El identificador de la comparacion del if: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
		else
			yyerror("El identificador de la comparacion del if: " + val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 57:
//#line 857 "gramatica.y"
{
	setearAmbito(val_peek(2).sval);
	String aux = comprobarAlcance(val_peek(2).sval); 

	if(!aux.equals("")){
		Par id =  new Par(aux);
		Par comp = new Par(val_peek(1).sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(comp);
	}
	else{
		yyerror("El identificador de la comparacion del if: " + val_peek(2).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 58:
//#line 877 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 59:
//#line 887 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
}
break;
case 60:
//#line 898 "gramatica.y"
{
}
break;
case 61:
//#line 903 "gramatica.y"
{
}
break;
case 62:
//#line 906 "gramatica.y"
{
}
break;
case 63:
//#line 911 "gramatica.y"
{
	setearAmbito(val_peek(3).sval);
	String aux = comprobarAlcance(val_peek(3).sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		Par asig = new Par(val_peek(2).sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(asig);	

		String [] aux2 = aux.split("\\@");
		for(int j=0; j<compilador.Compilador.tablaSimbolo.get(aux2[0]).size(); j++){
			if(compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).getAmbito().equals(aux)) 
				if(compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).isDeclarada())
					compilador.Compilador.tablaSimbolo.get(aux2[0]).get(j).setLineaConv(compilador.Compilador.nroLinea);
		}
	}
	else{
		yyerror(val_peek(3).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}

	/*
	if(sePuedeUsar($1.sval) == 1){
		//mostrarMensaje($1.sval + " No esta declarada.");
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		//Par id =  new Par($1.sval);
		//Par id =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		//Par id =  new Par(getAmbitoVerdadero($1.sval));
		sePuedeUsar2($1.sval);
		Par id = new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		Par asig = new Par($2.sval);
		polaca.agregarPaso(id);
		polaca.agregarPaso(asig);
		compilador.Compilador.nroLineaConversion.add(String.valueOf(compilador.Compilador.nroLinea));
	}
	*/
}
break;
case 64:
//#line 950 "gramatica.y"
{
	yyerror("Error: identificador mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 65:
//#line 954 "gramatica.y"
{
	yyerror("Error: asignacion mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 66:
//#line 960 "gramatica.y"
{
	Par suma =  new Par("+");
	polaca.agregarPaso(suma);
}
break;
case 67:
//#line 965 "gramatica.y"
{
	Par resta =  new Par("-");
	polaca.agregarPaso(resta);
}
break;
case 68:
//#line 970 "gramatica.y"
{
}
break;
case 69:
//#line 975 "gramatica.y"
{
	Par multi =  new Par("*");
	polaca.agregarPaso(multi);
}
break;
case 70:
//#line 980 "gramatica.y"
{ 
	Par division =  new Par("/");
	polaca.agregarPaso(division);
}
break;
case 71:
//#line 985 "gramatica.y"
{
}
break;
case 72:
//#line 990 "gramatica.y"
{
}
break;
case 73:
//#line 993 "gramatica.y"
{ 
	setearAmbito(val_peek(0).sval);
	String aux = comprobarAlcance(val_peek(0).sval); 
	if(!aux.equals("")){
		Par id = new Par(aux);
		polaca.agregarPaso(id);
	}
	else{
		yyerror(val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}

	/*
	if(sePuedeUsar($1.sval) == 1){
		//mostrarMensaje($1.sval + " No esta declarada.");
		yyerror($1.sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
	else{
		//Par id =  new Par($1.sval);
		//Par id =  new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		//Par id =  new Par(getAmbitoVerdadero($1.sval));
		sePuedeUsar2($1.sval);
		Par id = new Par(compilador.Compilador.tablaSimbolo.get($1.sval).get(compilador.Compilador.tablaSimbolo.get($1.sval).size()-1).getAmbito());
		polaca.agregarPaso(id);
	}
	*/
	
}
break;
case 74:
//#line 1023 "gramatica.y"
{
}
break;
case 75:
//#line 1026 "gramatica.y"
{
}
break;
case 76:
//#line 1029 "gramatica.y"
{
}
break;
case 77:
//#line 1032 "gramatica.y"
{
}
break;
case 78:
//#line 1035 "gramatica.y"
{
}
break;
case 79:
//#line 1038 "gramatica.y"
{
}
break;
case 80:
//#line 1041 "gramatica.y"
{
	yyerror("Error: comparador no permitido, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 81:
//#line 1047 "gramatica.y"
{
}
break;
case 82:
//#line 1050 "gramatica.y"
{
}
break;
case 83:
//#line 1055 "gramatica.y"
{
}
break;
case 84:
//#line 1060 "gramatica.y"
{
}
break;
case 85:
//#line 1063 "gramatica.y"
{
}
break;
case 86:
//#line 1067 "gramatica.y"
{
	setearAmbito(val_peek(0).sval);
	comprobarRango(val_peek(0).sval,false);
	String valor = val_peek(0).sval;
	double flotante;
	String result;
	if (valor.contains("_i"))
		result = valor.replace("_i", "");
	else {
		if (valor.contains("f")) {
			flotante = Double.parseDouble(valor.replace('f', 'E'));
			if(String.valueOf(flotante).contains("E"))
				result = String.valueOf(flotante).replace('f', 'E');
			else
				result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		else {
			flotante = Double.parseDouble(valor);
			result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}

	Par cte =  new Par(result);
	polaca.agregarPaso(cte);
	
	/*
	if (valor.contains("f")) {
		valor = valor.replace('f', 'E');
		valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
		valor = valor.replace('f', 'E');
	}
	*/
}
break;
case 87:
//#line 1101 "gramatica.y"
{
	/*yyerror("Error: constante positiva mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);*/
}
break;
case 88:
//#line 1107 "gramatica.y"
{  
	setearAmbito(val_peek(0).sval);
	comprobarRango(val_peek(0).sval,true);
	String valor = val_peek(0).sval;
	double flotante;
	String result;
	if (valor.contains("_i"))
		result = valor.replace("_i", "");
	else {
		if (valor.contains("f")) {
			flotante = Double.parseDouble(valor.replace('f', 'E'));
			if(String.valueOf(flotante).contains("E"))
				result = String.valueOf(flotante).replace('f', 'E');
			else
				result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
		else {
			flotante = Double.parseDouble(valor);
			result = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}

	Par cte =  new Par('-'+result);
	polaca.agregarPaso(cte);

	/*
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	*/
}
break;
//#line 2627 "Parser.java"
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
