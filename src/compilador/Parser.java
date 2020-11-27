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
   22,   22,   27,   28,   24,   25,   26,   23,   23,   11,
   11,   11,   29,   29,   29,   30,   30,   30,   31,   31,
   21,   21,   21,   21,   21,   21,   21,    4,    4,    7,
   20,   20,   34,   32,   33,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    1,    1,    3,    1,    3,    1,
    2,    0,   11,   13,   16,   19,   20,    3,    2,    2,
    1,    1,    1,    1,    5,    5,    4,    5,    7,    9,
    5,    2,    2,    1,    7,    5,    1,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    2,    2,   10,    8,
    8,    6,    1,    1,    1,    1,    1,    6,    4,    4,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    0,    2,    2,
};
final static short yydefred[] = {                         0,
    0,   80,    0,    0,    0,   79,   78,    0,    0,    0,
    5,    6,    0,    8,    0,    0,   24,   34,    0,    0,
   32,    0,    0,    0,    0,    0,    0,    3,    4,    0,
   10,    0,    0,   33,    0,   11,   83,    0,   70,   69,
    0,    0,   68,   81,   82,    0,   55,    0,   47,   48,
    0,    0,    0,    0,    0,    0,    7,    0,    0,    0,
    0,   62,    0,    0,   21,   22,    0,   84,   85,    0,
    0,    0,    0,   77,   71,   72,   73,   74,   75,   76,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,   27,    0,    0,   60,   18,   19,   20,    0,    0,
   66,   67,    0,   39,   41,    0,   53,    0,   26,   25,
   38,    0,    0,   37,    0,    0,   31,    0,   28,   46,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   45,    0,    0,   54,   52,   35,    0,    0,    0,   36,
    0,    0,    0,    0,   29,    0,    0,    0,   44,   42,
   43,    0,    0,    0,    0,    0,   51,   50,    0,    0,
    0,    0,   30,    0,    0,    0,    0,    0,   49,   13,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   14,    0,    0,    0,    0,    0,    0,    0,    0,   15,
    0,    0,    0,    0,    0,   16,   17,
};
final static short yydgoto[] = {                          9,
   10,   11,  120,   13,   30,   14,   15,   16,   36,   67,
   17,   21,   18,   54,  147,   55,  113,  140,   47,   40,
   81,   22,   23,   48,  122,  148,  108,  135,   41,   42,
   43,   44,   45,   68,
};
final static short yysindex[] = {                      -145,
  -44,    0,  -16,   23,   31,    0,    0, -208,    0,   88,
    0,    0, -208,    0,  -27,  -31,    0,    0,  -35, -208,
    0, -194, -181, -240, -208,   42,  -44,    0,    0,  -18,
    0,  -29,  -43,    0,  101,    0,    0, -185,    0,    0,
   11,   -4,    0,    0,    0,  -32,    0,   49,    0,    0,
   50,   60,   32,   62,   45,  -37,    0, -208,   65,   56,
  -41,    0,  -20,  -44,    0,    0,  -56,    0,    0,  -35,
  -35,  -35,  -35,    0,    0,    0,    0,    0,    0,    0,
  -36,   -9,   57,   59,  -34,  -14, -208, -143, -208,    0,
   66,    0, -208,   70,    0,    0,    0,    0,   -4,   -4,
    0,    0,   69,    0,    0, -160,    0, -129,    0,    0,
    0, -160,   73,    0,   74,    7,    0,    9,    0,    0,
 -160,   13,   54,   78, -236, -138, -201, -130, -208,   82,
    0, -118, -160,    0,    0,    0,  -34,  -34,  -34,    0,
   99, -208,   83,  108,    0,   68, -160,   26,    0,    0,
    0, -116,   17, -120,   96, -160,    0,    0,   97, -201,
 -113,  115,    0,   35, -108, -208,  100, -102,    0,    0,
  -33, -105,  109,  130,  -98,  137, -100,  -88,  122,  -84,
    0,  125,  -82,  132,  -80,  151,  -69,  152,  -72,    0,
  -62,  147,  155,  -55,  -49,    0,    0,
};
final static short yyrindex[] = {                        95,
  226,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   95,    0,    0,    0,    0,    0,
   40,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -78,    0,    0,   95,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,   27,
    0,    0,  -26,    0,    0,    0,    0,  -28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  107,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -19,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  126,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -3,   52,  -50,    0,    0,   75,    0,    0,    0,
  169,  237,    0,    0,  -77,    0,    0,    0,  167,  -63,
    0,    0,    0,    0,    0,  104,    0,  118,  222,    8,
   12,    0,    0,    0,
};
final static int YYTABLESIZE=369;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         94,
   65,   38,   93,   88,    1,   89,   28,  175,   38,   38,
   38,   60,   32,   63,   40,   51,   19,  105,   80,  137,
   79,  111,   70,   20,   71,   58,   64,   80,  121,   79,
   52,   65,   40,   33,  124,  138,  139,   72,   95,   61,
   57,   65,   73,   65,   23,   65,   23,  128,    2,  130,
  127,   12,  129,   70,   63,   71,   63,  161,   63,   65,
  160,   29,   24,   97,    6,    7,   49,   64,   96,   64,
   25,   64,   63,  149,  150,  151,  142,   99,  100,   50,
   61,   56,   26,  101,  102,   64,   66,   31,   69,   82,
   83,   35,   85,   39,   46,   27,    2,    3,   61,   53,
   84,    4,   86,   87,    5,   91,   61,   39,  112,  166,
    1,    2,    3,  106,   92,  109,    4,  110,   98,    5,
    6,    7,    8,   65,  117,   65,  115,   12,  119,   33,
  123,  125,   90,  107,  126,  141,   63,  132,   63,  143,
  145,  146,  152,  154,   39,   39,   39,   39,  155,   64,
  158,   64,  159,  162,  163,  103,  167,  165,  168,  169,
  172,   46,   61,  116,   61,  170,  173,  118,  176,  177,
  178,  179,  131,  181,  134,  131,  133,   23,   23,   23,
  180,  182,  183,   23,  184,  185,   23,   23,   23,   23,
  156,  186,  187,  188,  189,  191,  192,  134,  131,   27,
    2,    3,  136,  144,  190,    4,  193,  194,    5,    6,
    7,    8,   62,    2,   74,  195,  153,   12,  196,   27,
    2,    2,  174,   74,  197,    2,   59,    2,    6,    7,
   37,   56,   59,   75,   76,   77,   78,   37,   37,   37,
  171,   58,   75,   76,   77,   78,   27,    2,    3,  104,
   57,   34,    4,  114,   63,    5,   65,   65,   65,  164,
   65,   65,   65,  157,    0,   65,   65,   65,   65,   63,
   63,   63,    0,   63,   63,   63,    0,    0,   63,   63,
   63,   63,   64,   64,   64,    0,   64,   64,   64,    0,
    0,   64,   64,   64,   64,   61,   61,   61,    0,   61,
   61,   61,    0,    0,   61,   61,   61,   61,    0,   27,
    2,    3,    0,    0,    0,    4,    0,    0,    5,    0,
    0,    0,    0,   27,    2,    3,    0,    0,    0,    4,
    0,    0,    5,   27,    2,    3,    0,    0,    0,    4,
    0,    0,    5,   27,    2,    3,    0,    0,    0,    4,
    0,    0,    5,    6,    7,    8,   64,    2,    3,    0,
    0,    0,    4,    0,    0,    5,    6,    7,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   45,   44,   41,    0,   56,   10,   41,   45,   45,
   45,   41,   40,    0,   41,  256,   61,   81,   60,  256,
   62,   85,   43,   40,   45,   44,    0,   60,  106,   62,
  271,   35,   59,   61,  112,  272,  273,   42,   59,    0,
   59,   41,   47,   43,  123,   45,  125,   41,  257,   41,
   44,    0,   44,   43,   41,   45,   43,   41,   45,   59,
   44,   10,   40,   67,  266,  267,  261,   41,  125,   43,
   40,   45,   59,  137,  138,  139,  127,   70,   71,  261,
   41,   40,    8,   72,   73,   59,   35,   13,  274,   41,
   41,  123,   61,   19,   20,  256,  257,  258,   59,   25,
   41,  262,   41,   59,  265,   41,   32,   33,  123,  160,
  256,  257,  258,  123,   59,   59,  262,   59,   67,  265,
  266,  267,  268,  123,   59,  125,  270,  123,   59,   61,
  260,   59,   58,   82,   61,  274,  123,  125,  125,  270,
   59,  260,   44,   61,   70,   71,   72,   73,   41,  123,
  125,  125,  269,  274,   59,   81,  270,   61,   44,  125,
   61,   87,  123,   89,  125,  274,  269,   93,  274,   61,
   41,  270,  121,  274,  123,  124,  123,  256,  257,  258,
   44,  270,   61,  262,  269,   61,  265,  266,  267,  268,
  123,  274,   61,  274,   44,   44,  269,  146,  147,  256,
  257,  258,  125,  129,  274,  262,  269,   61,  265,  266,
  267,  268,  256,  257,  256,   61,  142,  123,  274,  256,
  257,  257,  256,  256,  274,    0,  256,  257,  266,  267,
  274,  125,  261,  275,  276,  277,  278,  274,  274,  274,
  166,  261,  275,  276,  277,  278,  256,  257,  258,   81,
  125,   15,  262,   87,   33,  265,  256,  257,  258,  156,
  260,  261,  262,  146,   -1,  265,  266,  267,  268,  256,
  257,  258,   -1,  260,  261,  262,   -1,   -1,  265,  266,
  267,  268,  256,  257,  258,   -1,  260,  261,  262,   -1,
   -1,  265,  266,  267,  268,  256,  257,  258,   -1,  260,
  261,  262,   -1,   -1,  265,  266,  267,  268,   -1,  256,
  257,  258,   -1,   -1,   -1,  262,   -1,   -1,  265,   -1,
   -1,   -1,   -1,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,  256,  257,  258,   -1,   -1,   -1,  262,
   -1,   -1,  265,  266,  267,  268,  256,  257,  258,   -1,
   -1,   -1,  262,   -1,   -1,  265,  266,  267,  268,
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
"condicionIf : condicion",
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

//#line 825 "gramatica.y"

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

void verificarNa(String sval, String proc){
	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') 
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}
	
	compilador.Compilador.anidamientos.add(Integer.parseInt(sval));
	int tamano = compilador.Compilador.anidamientos.size();
	/*
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++)
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " tiene problemas con los NA de: " + compilador.Compilador.anidamientos.get(i).getValor() + " Error en linea: " + compilador.Compilador.nroLinea);
			}
	*/
	if(tamano > 1)
		for(int i=0; i<tamano-1; i++){
			if( compilador.Compilador.anidamientos.get(tamano-1) >= compilador.Compilador.anidamientos.get(i)){
				//mostrarMensaje("Error en los niveles de anidamientos en el proc: " + proc);
				yyerror("Error en los niveles de anidamientos en el proc: " + proc + " Error en linea: " + compilador.Compilador.nroLinea);
				break;
			}
		}
		
}

boolean nameManglingNs(String sval) {
	
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Recorro la lista con todos los id con ese nombre
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + "@Main") && (compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada())) {
			//System.out.println("ACAAAAAAAAAAAAAAAAAAA: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getValor());
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				String [] arreglo = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().split("\\@");
				String idProc = arreglo[arreglo.length-1];
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(idProc).size(); j++){
					//System.out.println("ID DENTRO DE PROC NO DECLARADOS: " + compilador.Compilador.tablaSimbolo.get(sval).get(j).getValor());
					//Compruebo que el ambito del id del Proc este contenido
					String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
					String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(idProc).get(j).ambitoSinNombre();
					//System.out.println("ambitoSinVar: " + ambitoSinNombreVar);
					//System.out.println("ambitoSinProc: " + ambitoSinNombreProc);
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
					if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada()){
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
							yyerror("El Proc: " + sval + " intenta hacer recursion y no esta permitido. Error en linea: " + compilador.Compilador.nroLinea);
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
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
	}	
	else if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var") && !(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada())){
		compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
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



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////FIN DEFINICIONES PROPIAS////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//#line 912 "Parser.java"
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
	polaca.mostrarParametrosFormales();
	mostrarMensaje("Reconoce bien el programa");
	/*System.out.println(polaca.toString());*/
}
break;
case 2:
//#line 25 "gramatica.y"
{
	yyerror("Programa invalido, error en linea: " + compilador.Compilador.nroLinea);
}
break;
case 3:
//#line 31 "gramatica.y"
{
}
break;
case 4:
//#line 34 "gramatica.y"
{
}
break;
case 5:
//#line 37 "gramatica.y"
{
}
break;
case 6:
//#line 40 "gramatica.y"
{
}
break;
case 7:
//#line 45 "gramatica.y"
{
	/*mostrarMensaje("Declaracion de una o mas variables en linea nro: " + compilador.Compilador.nroLinea);*/
	setearTipoParam(val_peek(2).sval);
}
break;
case 8:
//#line 50 "gramatica.y"
{
	Par retorno = new Par("RET");
	polaca.agregarPaso(retorno);
}
break;
case 9:
//#line 57 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		/*mostrarMensaje($3.sval + " esta Redeclarada.");*/
		yyerror(val_peek(0).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 10:
//#line 65 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		/*mostrarMensaje($1.sval + " esta Redeclarada.");*/
		yyerror(val_peek(0).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
}
break;
case 11:
//#line 75 "gramatica.y"
{
	/*mostrarMensaje("Procedimiento completo, en linea nro: " + compilador.Compilador.nroLinea);*/
	disminuirAmbito();
	if(!(compilador.Compilador.anidamientos.size() == 0))
		compilador.Compilador.anidamientos.remove(compilador.Compilador.anidamientos.size()-1);
}
break;
case 13:
//#line 84 "gramatica.y"
{
	/*mostrarMensaje("Procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);*/
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
		verificarNa(val_peek(4).sval,val_peek(9).sval);
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 14:
//#line 107 "gramatica.y"
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
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());
		
		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(12).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  val_peek(11).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(11).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(11).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		verificarNa(val_peek(4).sval,val_peek(11).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 15:
//#line 138 "gramatica.y"
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
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());

		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(15).sval+" "+compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" +  val_peek(14).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(14).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(14).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		verificarNa(val_peek(4).sval,val_peek(14).sval);
		setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 16:
//#line 172 "gramatica.y"
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
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(14).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(11).sval).size()-1).getAmbito());
		polaca.agregarParametro(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());

		/*Par proc = new Par($1.sval+" "+$2.sval);*/
		Par proc =  new Par(val_peek(18).sval +" "+compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(17).sval).size()-1).getAmbito());
		polaca.agregarPaso(proc);
		compilador.Compilador.ambito = compilador.Compilador.ambito + "@" + val_peek(17).sval;
		setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
		if(sePuedeUsar(val_peek(17).sval) == 2){
			/*mostrarMensaje($2.sval + " esta Redeclarada.");*/
			yyerror(val_peek(17).sval + " esta Redeclarada. Error en linea: " + compilador.Compilador.nroLinea);
		}
		verificarNa(val_peek(4).sval,val_peek(17).sval);
		setearAmbitoyDeclarada(val_peek(14).sval,val_peek(15).sval);
		setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
		setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
	}
	else{
		/*mostrarMensaje("NA o NS no es una CTE ENTERA");*/
		yyerror("NA o NS no es una CTE ENTERA");
	}
}
break;
case 17:
//#line 209 "gramatica.y"
{
	yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);
}
break;
case 18:
//#line 215 "gramatica.y"
{
	PolacaInversa.bajarNivelProc();
	polaca.borrarProcYParametros();
	/*int posProc = polaca.inicioProc();*/
}
break;
case 19:
//#line 223 "gramatica.y"
{
}
break;
case 20:
//#line 226 "gramatica.y"
{
}
break;
case 21:
//#line 229 "gramatica.y"
{
}
break;
case 22:
//#line 232 "gramatica.y"
{
}
break;
case 23:
//#line 235 "gramatica.y"
{
	yyerror("Error: no puede haber un seccion vacia, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 24:
//#line 241 "gramatica.y"
{
}
break;
case 25:
//#line 244 "gramatica.y"
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
//#line 253 "gramatica.y"
{
	yyerror("Error: Formato de cadena incorrecto, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 27:
//#line 257 "gramatica.y"
{
	/*Par nomProc = new Par($1.sval); */
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);

	/*mostrarMensaje("Llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);*/

	compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).setCantParametros(0);
	setearAmbito(val_peek(3).sval);

	/*Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado*/
	int aux = sePuedeUsar(val_peek(3).sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			/*mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");*/
			yyerror("Procedimiento: " + val_peek(3).sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			mostrarMensaje("Procedimiento " + val_peek(3).sval + " esta Redeclarado.");
			yyerror("Procedimiento " + val_peek(3).sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam(val_peek(3).sval)){
			mostrarMensaje("Llamador del procedimiento: " + val_peek(3).sval + " no coincide con la cantidad de parametros de su definicion.");
			yyerror("Llamador del procedimiento: " + val_peek(3).sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
}
break;
case 28:
//#line 290 "gramatica.y"
{
	ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(val_peek(2).sval));
	polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(val_peek(4).sval));
	
	/*Par nomProc = new Par($1.sval); */
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	/*mostrarMensaje("Llamada a procedimiento con 1 parametro en linea nro: " + compilador.Compilador.nroLinea);*/

	compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(4).sval).size()-1).setCantParametros(1);
	setearAmbito(val_peek(4).sval);
	
	/*Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado*/
	int aux = sePuedeUsar(val_peek(4).sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			/*mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");*/
			yyerror("Procedimiento: " + val_peek(4).sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			/*mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");*/
			yyerror("Procedimiento " + val_peek(4).sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam(val_peek(4).sval)){
			/*mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");*/
			yyerror("Llamador del procedimiento: " + val_peek(4).sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	
	/*Compruebo que el parametro real se pueda usar*/
	int aux2 = sePuedeUsar(val_peek(2).sval);
	if(aux2 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(4).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}

}
break;
case 29:
//#line 333 "gramatica.y"
{
	ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(val_peek(4).sval, val_peek(2).sval));
	polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(val_peek(6).sval));	

	/*Par nomProc = new Par($1.sval); */
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	/*mostrarMensaje("Llamada a procedimiento con 2 parametros en linea nro: " + compilador.Compilador.nroLinea);*/

	compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(6).sval).size()-1).setCantParametros(2);
	setearAmbito(val_peek(6).sval);
	
	/*Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado*/
	int aux = sePuedeUsar(val_peek(6).sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			/*mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");*/
			yyerror("Procedimiento: " + val_peek(6).sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			/*mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");*/
			yyerror("Procedimiento " + val_peek(6).sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam(val_peek(6).sval)){
			/*mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");*/
			yyerror("Llamador del procedimiento: " + val_peek(6).sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}

	/*Compruebo que el parametro real se pueda usar*/
	int aux2 = sePuedeUsar(val_peek(4).sval);
	int aux3 = sePuedeUsar(val_peek(2).sval);
	if(aux2 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(6).sval + " tiene el parametro real " + val_peek(4).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}
	if(aux3 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(6).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}

}
break;
case 30:
//#line 381 "gramatica.y"
{
	ArrayList<String> parametrosInvocados = new ArrayList<String>(Arrays.asList(val_peek(6).sval, val_peek(4).sval, val_peek(2).sval));
	polaca.asignarParametros(parametrosInvocados, polaca.inicioProc(val_peek(8).sval));
	
	/*Par nomProc = new Par($1.sval); */
	Par nomProc =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).getAmbito());
	Par call = new Par("CALL");
	polaca.agregarPaso(nomProc);
	polaca.agregarPaso(call);
	/*mostrarMensaje("Llamada a procedimiento con 3 parametros en linea nro: " + compilador.Compilador.nroLinea);*/

	compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(8).sval).size()-1).setCantParametros(3);
	setearAmbito(val_peek(8).sval);

	/*Compruebo que el nombre del llamador este al alcance y coincida con el numero de parametros del llamado*/
	int aux = sePuedeUsar(val_peek(8).sval);
	if(aux == 1 || aux == 2){
		if(aux == 1){
			/*mostrarMensaje("Procedimiento: " + $1.sval + " No esta declarado.");*/
			yyerror("Procedimiento: " + val_peek(8).sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
		else{
			/*mostrarMensaje("Procedimiento " + $1.sval + " esta Redeclarado.");*/
			yyerror("Procedimiento " + val_peek(8).sval + " esta Redeclarado. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}
	else{
		if(!verificarCantParam(val_peek(8).sval)){
			/*mostrarMensaje("Llamador del procedimiento: " + $1.sval + " no coincide con la cantidad de parametros de su definicion.");*/
			yyerror("Llamador del procedimiento: " + val_peek(8).sval + " no coincide con la cantidad de parametros de su definicion. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}

	/*Compruebo que el parametro real se pueda usar*/
	int aux2 = sePuedeUsar(val_peek(6).sval);
	int aux3 = sePuedeUsar(val_peek(4).sval);
	int aux4 = sePuedeUsar(val_peek(2).sval);
	if(aux2 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $3.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(6).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}
	if(aux3 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $5.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(4).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}
	if(aux4 == 1){
		/*mostrarMensaje("Procedimiento: " + $1.sval + " tiene el parametro real " + $7.sval +  " No declarado.");*/
		yyerror("Procedimiento: " + val_peek(8).sval + " tiene el parametro real " + val_peek(2).sval +  " No declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}

}
break;
case 31:
//#line 434 "gramatica.y"
{
	yyerror("Error: Cantidad no permitida de parametros, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 32:
//#line 438 "gramatica.y"
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
//#line 448 "gramatica.y"
{
	yyerror("Error: las palabras reservadas van en mayuscula, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 34:
//#line 453 "gramatica.y"
{
	/*mostrarMensaje("Ciclo FOR en linea nro: " + compilador.Compilador.nroLinea);*/
}
break;
case 35:
//#line 459 "gramatica.y"
{
	polaca.borrarVariablesControl();
	Par pasoEnBlanco = new Par("");
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI");
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
	polaca.completarFOR();
	polaca.borrarInicioFOR();
	polaca.borrarPasoIncompleto();
	polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());
}
break;
case 36:
//#line 475 "gramatica.y"
{
	polaca.borrarPasoPolaca();
}
break;
case 37:
//#line 481 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 38:
//#line 491 "gramatica.y"
{
	if(!verficarCTEEnteras(val_peek(0).sval))
		yyerror("CTE de: " + val_peek(2).sval + " debe ser entero. Error en linea: " + compilador.Compilador.nroLinea);

	polaca.agregarVariableControl(val_peek(2).sval);
	/*Par id = new Par($1.sval);*/
	Par id =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).size()-1).getAmbito());
	polaca.agregarPaso(id);
	Par asig = new Par(val_peek(1).sval);
	polaca.agregarPaso(asig);
	polaca.agregarInicioFOR();
	polaca.agregarLabel();
}
break;
case 39:
//#line 507 "gramatica.y"
{
	/*Par id = new Par($1.sval);*/
	Par id =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).size()-1).getAmbito());
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
break;
case 40:
//#line 515 "gramatica.y"
{
	if(sePuedeUsar(val_peek(0).sval) == 0 ) {
		boolean aux = false;
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).size(); i++){
			/*Compruebo que el id no sea proc y que el ambito sea Main*/
			if(!compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(i).getTipo().equals("Proc") && compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(i).isDeclarada()) {
				String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).size()-1).getAmbito();
				String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(i).getAmbito();
				if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
					if(!compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(i).getTipoParametro().equals("INTEGER"))
						aux = true;
						break;
					}
			}
		}
		if(aux) {
			yyerror("Variable de comparacion: " + val_peek(0).sval + " No es de tipo INTEGER. Error en linea: " + compilador.Compilador.nroLinea);
		}
	}else {
		yyerror("Variable de comparacion: " + val_peek(0).sval + " No esta declarado. Error en linea: " + compilador.Compilador.nroLinea);
	}

	/*Par id1 = new Par($1.sval);*/
	/*Par id2 = new Par($3.sval);*/
	Par id1 =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).size()-1).getAmbito());
	Par id2 =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).size()-1).getAmbito());
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id1);
	polaca.agregarPaso(id2);
	polaca.agregarPaso(comp);
}
break;
case 41:
//#line 547 "gramatica.y"
{
	if(!verficarCTEEnteras(val_peek(0).sval))
		yyerror("CTE de la comparacion debe ser entero. Error en linea: " + compilador.Compilador.nroLinea);

	/*Par id = new Par($1.sval);*/
	Par id =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(2).sval).size()-1).getAmbito());
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
break;
case 42:
//#line 560 "gramatica.y"
{	
	if(!verficarCTEEnteras(val_peek(0).sval))
		yyerror("CTE del UP debe ser entero. Error en linea: " + compilador.Compilador.nroLinea);
	polaca.agregarVariableControl("+");
	polaca.agregarVariableControl(val_peek(0).sval);
}
break;
case 43:
//#line 567 "gramatica.y"
{
	if(!verficarCTEEnteras(val_peek(0).sval))
		yyerror("CTE del DOWN debe ser entero. Error en linea: " + compilador.Compilador.nroLinea);
	polaca.agregarVariableControl("-");
	polaca.agregarVariableControl(val_peek(0).sval);
}
break;
case 44:
//#line 574 "gramatica.y"
{
	yyerror("Error: incremento/decremento mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 45:
//#line 580 "gramatica.y"
{
}
break;
case 46:
//#line 583 "gramatica.y"
{
}
break;
case 47:
//#line 588 "gramatica.y"
{
	PolacaInversa.setFlagITE(true);
}
break;
case 48:
//#line 592 "gramatica.y"
{
	PolacaInversa.setFlagITE(false); 
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoIncompleto();
}
break;
case 49:
//#line 602 "gramatica.y"
{
}
break;
case 50:
//#line 605 "gramatica.y"
{
}
break;
case 51:
//#line 608 "gramatica.y"
{
}
break;
case 52:
//#line 611 "gramatica.y"
{
}
break;
case 53:
//#line 617 "gramatica.y"
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
//#line 628 "gramatica.y"
{
}
break;
case 55:
//#line 633 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 56:
//#line 643 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
	polaca.agregarLabel();
}
break;
case 57:
//#line 654 "gramatica.y"
{
}
break;
case 58:
//#line 659 "gramatica.y"
{
}
break;
case 59:
//#line 662 "gramatica.y"
{
}
break;
case 60:
//#line 667 "gramatica.y"
{
	setearAmbito(val_peek(3).sval);
	if(sePuedeUsar(val_peek(3).sval) == 1){
		/*mostrarMensaje($1.sval + " No esta declarada.");*/
		yyerror(val_peek(3).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
	/*Par id =  new Par($1.sval);*/
	Par id =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(3).sval).size()-1).getAmbito());
	Par asig = new Par(val_peek(2).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(asig);
}
break;
case 61:
//#line 680 "gramatica.y"
{
	yyerror("Error: identificador mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 62:
//#line 684 "gramatica.y"
{
	yyerror("Error: asignacion mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 63:
//#line 690 "gramatica.y"
{
	Par suma =  new Par("+");
	polaca.agregarPaso(suma);
}
break;
case 64:
//#line 695 "gramatica.y"
{
	Par resta =  new Par("-");
	polaca.agregarPaso(resta);
}
break;
case 65:
//#line 700 "gramatica.y"
{
}
break;
case 66:
//#line 705 "gramatica.y"
{
	Par multi =  new Par("*");
	polaca.agregarPaso(multi);
}
break;
case 67:
//#line 710 "gramatica.y"
{ 
	Par division =  new Par("/");
	polaca.agregarPaso(division);
}
break;
case 68:
//#line 715 "gramatica.y"
{
}
break;
case 69:
//#line 720 "gramatica.y"
{
}
break;
case 70:
//#line 723 "gramatica.y"
{ 
	setearAmbito(val_peek(0).sval);
	if(sePuedeUsar(val_peek(0).sval) == 1){
		/*mostrarMensaje($1.sval + " No esta declarada.");*/
		yyerror(val_peek(0).sval + " No esta declarada. Error en linea: " + compilador.Compilador.nroLinea);
	}
    /*Par id =  new Par($1.sval);*/
	Par id =  new Par(compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).get(compilador.Compilador.tablaSimbolo.get(val_peek(0).sval).size()-1).getAmbito());
	polaca.agregarPaso(id);
	
}
break;
case 71:
//#line 737 "gramatica.y"
{
}
break;
case 72:
//#line 740 "gramatica.y"
{
}
break;
case 73:
//#line 743 "gramatica.y"
{
}
break;
case 74:
//#line 746 "gramatica.y"
{
}
break;
case 75:
//#line 749 "gramatica.y"
{
}
break;
case 76:
//#line 752 "gramatica.y"
{
}
break;
case 77:
//#line 755 "gramatica.y"
{
	yyerror("Error: comparador no permitido, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 78:
//#line 761 "gramatica.y"
{
}
break;
case 79:
//#line 764 "gramatica.y"
{
}
break;
case 80:
//#line 769 "gramatica.y"
{
}
break;
case 81:
//#line 774 "gramatica.y"
{
}
break;
case 82:
//#line 777 "gramatica.y"
{
}
break;
case 83:
//#line 781 "gramatica.y"
{
	setearAmbito(val_peek(0).sval);
	comprobarRango(val_peek(0).sval,false);
	String valor = val_peek(0).sval;
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	Par cte =  new Par(valor);
	polaca.agregarPaso(cte);
}
break;
case 84:
//#line 797 "gramatica.y"
{
	/*yyerror("Error: constante positiva mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);*/
}
break;
case 85:
//#line 803 "gramatica.y"
{  
	setearAmbito(val_peek(0).sval);
	comprobarRango(val_peek(0).sval,true);
	String valor = val_peek(0).sval;
	if (valor.contains("_i"))
		valor = valor.replace("_i", "");
	else 
		if (valor.contains("f")) {
			valor = valor.replace('f', 'E');
			valor = AS10_Verificar_Rango_Float.normalizar( Double.parseDouble(valor));
			valor = valor.replace('f', 'E');
		}
	Par cte =  new Par('-'+valor);
	polaca.agregarPaso(cte);
}
break;
//#line 1961 "Parser.java"
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
