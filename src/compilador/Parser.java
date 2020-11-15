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
//#line 22 "Parser.java"




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
    6,    8,    8,    8,    8,    8,    8,   10,   10,   10,
    9,   11,   11,   11,   11,   11,    3,    3,    3,    3,
    3,    3,    3,    3,   14,   15,   18,   17,   20,   20,
   20,   19,   19,   19,   16,   16,   13,   13,   23,   23,
   23,   23,   25,   26,   27,   24,   24,   12,   12,   28,
   28,   28,   29,   29,   29,   30,   30,   22,   22,   22,
   22,   22,   22,   22,    4,    4,    7,   21,   21,   33,
   31,   34,   32,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    1,    1,    3,    1,    3,    1,
    2,    0,   11,   13,   16,   19,   20,    1,    3,    5,
    3,    2,    2,    1,    1,    1,    1,    5,    5,    5,
    4,    5,    2,    1,    7,    5,    1,    3,    3,    3,
    3,    2,    2,    2,    2,    1,    2,    2,   10,    8,
    8,    6,    1,    1,    1,    6,    4,    4,    3,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    0,
    2,    0,    3,
};
final static short yydefred[] = {                         0,
    0,   77,    0,    0,    0,   76,   75,    0,    0,    0,
    5,    6,    0,    8,    0,    0,   27,   34,    0,    0,
   33,    0,    0,    0,    0,    0,    0,    3,    4,    0,
   10,    0,    0,    0,   11,   80,    0,   67,   66,    0,
    0,   65,   78,   79,    0,   53,    0,   47,   48,    0,
    0,    0,    0,    0,    0,    7,    0,    0,    0,    0,
    0,    0,    0,   24,   25,    0,   81,   82,    0,    0,
    0,    0,   74,   68,   69,   70,   71,   72,   73,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    9,    0,
   31,    0,    0,   58,   21,   22,   23,   83,    0,    0,
   63,   64,    0,   39,   41,    0,    0,   29,   28,   38,
    0,    0,   37,    0,    0,   32,    0,   30,   46,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   45,    0,
    0,   52,   35,    0,    0,    0,   36,    0,    0,    0,
   20,    0,    0,    0,   44,   42,   43,    0,    0,    0,
    0,   51,   50,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   49,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   14,    0,    0,    0,    0,    0,
    0,    0,    0,   15,    0,    0,    0,    0,    0,   16,
   17,
};
final static short yydgoto[] = {                          9,
   10,   11,  119,   13,   30,   14,   15,   16,   35,   61,
   66,   17,   21,   18,   53,  143,   54,  112,  137,   46,
   39,   80,   22,   23,   47,  121,  144,   40,   41,   42,
   43,   44,   67,   98,
};
final static short yysindex[] = {                      -135,
  -36,    0,   -1,    8,   12,    0,    0, -244,    0,   53,
    0,    0, -244,    0,  -28,  -69,    0,    0,  -43, -244,
    0, -205, -192, -241, -244,   36,  -36,    0,    0,  -27,
    0,  -32,  -43,   66,    0,    0, -189,    0,    0,  -22,
   16,    0,    0,    0,  -56,    0,   50,    0,    0,   51,
   52,   33,   59,   42,  -33,    0, -244,   62,   46,   67,
   72,  -23,  -36,    0,    0,  -96,    0,    0,  -43,  -43,
  -43,  -43,    0,    0,    0,    0,    0,    0,    0,  -45,
  -52,   55,   56,  -42,   -7, -244, -153, -244,    0,   60,
    0, -244,   61,    0,    0,    0,    0,    0,   16,   16,
    0,    0,   57,    0,    0,   79, -131,    0,    0,    0,
   79,   75,    0,   64,   20,    0,   91,    0,    0,   79,
   13,  -21, -109, -238, -138, -200, -129, -244,    0, -118,
   79,    0,    0,  -42,  -42,  -42,    0,   99, -244,   83,
    0,  -11,   79,   26,    0,    0,    0, -124,   21, -120,
   79,    0,    0,   97, -200, -111,  120,   48, -107, -244,
  107,  -95,    0,    0,  -30,  -99,  115,  136,  -91,  139,
  -90,  -85,  126,  -81,    0,  132,  -79,  137,  -75,  157,
  -72,  159,  -62,    0,  -61,  148,  154,  -51,  -47,    0,
    0,
};
final static short yyrindex[] = {                        95,
  228,    0,    0,    0,    0,    0,    0,    0,    0,    5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   95,    0,    0,    0,    0,    0,   40,
    1,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  189,
    0,    0,  -76,    0,    0,   95,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   14,   27,
    0,    0,  -31,    0,    0,    0,  -19,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  197,    0,    0,  114,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -18,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  123,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,   43,   74,  -48,    0,    0,   18,    0,    0,    0,
    0,  160,    0,    0,    0,  -87,    0,    0,    0,  163,
  -39,    0,    0,    0,    0,    0,  101,  217,   10,   11,
    0,    0,    0,    0,
};
final static int YYTABLESIZE=344;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   62,   37,   37,   79,    1,   78,   88,   87,   59,   40,
  169,   32,    2,   60,   50,  133,   57,  134,  120,   69,
   69,   70,   70,  123,   19,   26,   61,   40,   95,   51,
   31,   56,   33,  135,  136,   94,   38,   45,   20,   59,
  105,   62,   52,   62,  110,   62,   26,   24,   26,   60,
   38,   25,   28,   34,   60,   48,   60,   71,   60,   62,
  127,  156,   72,  126,  155,    6,    7,   61,   49,   61,
  106,   61,   60,   12,   89,   55,   64,  139,   99,  100,
   59,  101,  102,   29,   68,   61,   38,   38,   38,   38,
   81,   82,   83,   84,  145,  146,  147,  103,   59,   85,
   86,  131,   90,   45,   91,  115,  160,   65,   96,  117,
   92,  151,   93,  108,  109,  111,  114,   33,  116,  118,
    1,    2,    3,   62,  125,   62,    4,   12,  122,    5,
    6,    7,    8,  124,  128,  138,   60,  130,   60,   97,
  140,  142,  148,  150,  154,  141,   27,    2,    3,   61,
  153,   61,    4,  157,  107,    5,  149,  159,  161,   27,
    2,    3,   59,  162,   59,    4,  164,  166,    5,    6,
    7,    8,  163,  167,  170,  171,  172,  165,  173,   26,
   26,   26,  174,  175,  176,   26,  177,  178,   26,   26,
   26,   26,  179,  129,  180,  132,  129,  181,  182,   73,
  183,  184,  185,   27,    2,    3,  186,  187,  188,    4,
   27,    2,    5,    2,  189,  152,  129,   12,   74,   75,
   76,   77,  190,   58,    2,  168,  191,    2,   36,   18,
   36,   36,    6,    7,   27,    2,    3,   19,   54,  104,
    4,   57,   56,    5,   27,    2,    3,   55,  113,   62,
    4,  158,    0,    5,    0,    0,   62,   62,   62,    0,
   62,   62,   62,    0,    0,   62,   62,   62,   62,   60,
   60,   60,    0,   60,   60,   60,    0,    0,   60,   60,
   60,   60,   61,   61,   61,    0,   61,   61,   61,    0,
    0,   61,   61,   61,   61,   59,   59,   59,    0,   59,
   59,   59,    0,    0,   59,   59,   59,   59,   27,    2,
    3,    0,    0,    0,    4,    0,    0,    5,    6,    7,
    8,   63,    2,    3,    0,    0,    0,    4,    0,    0,
    5,    6,    7,    8,   27,    2,    3,    0,    0,    0,
    4,    0,    0,    5,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
    0,   45,   45,   60,    0,   62,   55,   41,   41,   41,
   41,   40,  257,    0,  256,  125,   44,  256,  106,   43,
   43,   45,   45,  111,   61,    8,    0,   59,  125,  271,
   13,   59,   61,  272,  273,   59,   19,   20,   40,    0,
   80,   41,   25,   43,   84,   45,  123,   40,  125,   32,
   33,   40,   10,  123,   41,  261,   43,   42,   45,   59,
   41,   41,   47,   44,   44,  266,  267,   41,  261,   43,
  123,   45,   59,    0,   57,   40,   34,  126,   69,   70,
   41,   71,   72,   10,  274,   59,   69,   70,   71,   72,
   41,   41,   41,   61,  134,  135,  136,   80,   59,   41,
   59,  123,   41,   86,   59,   88,  155,   34,   66,   92,
   44,  123,   41,   59,   59,  123,  270,   61,   59,   59,
  256,  257,  258,  123,   61,  125,  262,  123,  260,  265,
  266,  267,  268,   59,   44,  274,  123,  125,  125,   66,
  270,  260,   44,   61,  269,  128,  256,  257,  258,  123,
  125,  125,  262,  274,   81,  265,  139,   61,  270,  256,
  257,  258,  123,   44,  125,  262,  274,   61,  265,  266,
  267,  268,  125,  269,  274,   61,   41,  160,  270,  256,
  257,  258,   44,  274,  270,  262,   61,  269,  265,  266,
  267,  268,   61,  120,  274,  122,  123,   61,  274,  256,
   44,  274,   44,  256,  257,  258,  269,  269,   61,  262,
  256,  257,  265,  257,   61,  142,  143,  123,  275,  276,
  277,  278,  274,  256,  257,  256,  274,    0,  274,   41,
  274,  274,  266,  267,  256,  257,  258,   41,  125,   80,
  262,  261,  261,  265,  256,  257,  258,  125,   86,   33,
  262,  151,   -1,  265,   -1,   -1,  256,  257,  258,   -1,
  260,  261,  262,   -1,   -1,  265,  266,  267,  268,  256,
  257,  258,   -1,  260,  261,  262,   -1,   -1,  265,  266,
  267,  268,  256,  257,  258,   -1,  260,  261,  262,   -1,
   -1,  265,  266,  267,  268,  256,  257,  258,   -1,  260,
  261,  262,   -1,   -1,  265,  266,  267,  268,  256,  257,
  258,   -1,   -1,   -1,  262,   -1,   -1,  265,  266,  267,
  268,  256,  257,  258,   -1,   -1,   -1,  262,   -1,   -1,
  265,  266,  267,  268,  256,  257,  258,   -1,   -1,   -1,
  262,   -1,   -1,  265,
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
"nombres : identificador",
"nombres : identificador ',' identificador",
"nombres : identificador ',' identificador ',' identificador",
"bloqueProc : '{' bloque '}'",
"bloque : bloque sentenciaDeclarativa",
"bloque : bloque sentenciaEjecutable",
"bloque : sentenciaDeclarativa",
"bloque : sentenciaEjecutable",
"bloque : error",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : OUT '(' CADENA ')' ';'",
"sentenciaEjecutable : OUT '(' error ')' ';'",
"sentenciaEjecutable : identificador '(' nombres ')' ';'",
"sentenciaEjecutable : identificador '(' ')' ';'",
"sentenciaEjecutable : identificador '(' error ')' ';'",
"sentenciaEjecutable : IF cuerpoIf",
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
"cuerpoCompleto : '(' condicionIf ')' sentenciaEjecutable ELSE '{' bloqueElse '}'",
"cuerpoCompleto : '(' condicionIf ')' '{' bloqueThen '}' ELSE sentenciaEjecutable",
"cuerpoCompleto : '(' condicionIf ')' sentenciaEjecutable ELSE sentenciaEjecutable",
"condicionIf : condicion",
"bloqueThen : bloqueSentencia",
"bloqueElse : bloqueSentencia",
"cuerpoIncompleto : '(' condicionIf ')' '{' bloqueThen '}'",
"cuerpoIncompleto : '(' condicionIf ')' sentenciaEjecutable",
"asignacion : identificador '=' expresion ';'",
"asignacion : error '=' expresion",
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
"$$2 :",
"cteNegativa : '-' CTE $$2",
};

//#line 507 "gramatica.y"

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////// DEFINICIONES PROPIAS///////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    // Auto-generated catch block
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
	@SuppressWarnings("unused")
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
				mostrarMensaje("CTE FLOAT negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				mostrarMensaje("CTE FLOAT negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
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
				mostrarMensaje("CTE ENTERA negativa esta dentro del rango");
			}
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				mostrarMensaje("CTE ENTERA negativa esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
	//ES POSITIVO	
	}else {
		// ES FLOAT Y POSTIVO???
		if (sval.contains("f") || sval.contains(".")){
			flotante = Double.parseDouble(sval.replace('f', 'E'));
			if ( AS10_Verificar_Rango_Float.estaEnRango(sval) )
			mostrarMensaje("CTE FLOAT postiva esta dentro del rango");
			else {
				//compilador.Compilador.tablaSimbolo.remove(AS10_Verificar_Rango_Float.normalizar(flotante));
				compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).remove(compilador.Compilador.tablaSimbolo.get(AS10_Verificar_Rango_Float.normalizar(flotante)).size()-1);
				mostrarMensaje("CTE FLOAT positiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
		// ES ENTERA Y POSITIVA
		else{
			if ( AS9_Verificar_Rango_Constante.estaEnRango(sval) )
			mostrarMensaje("CTE ENTERA postiva esta dentro del rango");
			else {
				//compilador.Compilador.tablaSimbolo.remove(sval);
				sval = sval.toString().substring(0, sval.length()-2);
				compilador.Compilador.tablaSimbolo.get(sval).remove(compilador.Compilador.tablaSimbolo.get(sval).size()-1);
				mostrarMensaje("CTE ENTERA postiva esta fuera del rango por lo tanto no aparece en la tabla de simbolos.");
			}
		}
		
	}
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

	if(sval.charAt(0) >= '0' && sval.charAt(0) <= '9') 
		if(sval.contains("_") && sval.contains("i")){
			sval = sval.toString().substring(0, sval.length()-2); 
		}

	if(compilador.Compilador.primero){
		compilador.Compilador.na = Integer.parseInt(sval); 
		compilador.Compilador.primero = false; 
		compilador.Compilador.naa = Integer.parseInt(sval);
	}
	else{
		if(Integer.parseInt(sval) == 0)
			compilador.Compilador.na = compilador.Compilador.na - 1; 
		else
			compilador.Compilador.na = compilador.Compilador.na - Integer.parseInt(sval); 

		if(compilador.Compilador.na < 0){
			//Error 1: la suma de los na actual supera al na de algun proc que lo engloba.  
			mostrarMensaje("La suma de los na actual supera al na del proc: " + proc + ".");
		} 
		if(compilador.Compilador.naa < Integer.parseInt(sval)){
			//Error 2: na de proc x es mayor que el na del proc que lo contiene.
			mostrarMensaje("Na de proc: " + proc + " es mayor que el Na del proc que lo contiene.");
		} 
		//compilador.Compilador.naa = Integer.parseInt(sval); 
	}
	compilador.Compilador.naa = Integer.parseInt(sval);
}

boolean nameManglingNs(String sval) {
	
	int cantidadAnidamientos = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).cantidadAnidamientos();
	String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
	
	//Recorro la lista con todos los id con ese nombre
	for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
		//Veo que el id no sea Proc y no sea una variable declarada en el main (sino que este adentro de un Proc)
		if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc") && !compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(sval + ":Main")) {
			//Compruebo que el ambito de id no declarado este contenido en la lista de id declarados
			if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
				char idProc = compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().charAt(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().length()-1);
				//Recorro lista de id de Proc
				for(int j=0; j<compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).size(); j++){
					//Compruebo que el ambito del id del Proc este contenido
					String ambitoSinNombreVar = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).ambitoSinNombre();
					String ambitoSinNombreProc = compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).get(j).ambitoSinNombre();
					if(ambitoSinNombreVar.indexOf(ambitoSinNombreProc) != -1){
						//Compruebo que el NS sea >= que la cantidad de anidamientos
						if(compilador.Compilador.tablaSimbolo.get(String.valueOf(idProc)).get(j).getNs() >= cantidadAnidamientos)
							return true;
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
	//Esta en la tabla de simbolos?
	if(compilador.Compilador.tablaSimbolo.containsKey(sval)) {
		//Es una variable?
		if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getTipo().equals("Var")){
			//No esta declarada?
			if(!compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).isDeclarada()){
				//Veo si es un id que esta dentro del Proc para evaluar el NS
				if(compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).cantidadAnidamientos() > 0){
					if(nameManglingNs(sval))
						return 0;
					//Puede que se de el caso que Los Proc no quieren que sea vea y va a ir al Main a buscar
					//else
					//	return 1;
				}
				//Tomo el ambito de la id no declarada y busco si hay una declarada al alcance.
				String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
				if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
					return 1;
				}
				else{
					//System.out.println("Tamño: " + compilador.Compilador.tablaSimbolo.get(sval).size());
					for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size()-1; i++){
						if(!compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Proc")) {
							//System.out.println("Tabla: " + compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito());
							if(ambitoId.indexOf(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito()) != -1){
								return 0;
							}
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
		}
		else{
			//Tomo el ambito de la id de proc y veo que no este en el mismo ambito.
			String ambitoId = compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).getAmbito();
			if(compilador.Compilador.tablaSimbolo.get(sval).size() == 1){
				return 0;
			}
			else{
				//System.out.println("Tamanio: " + compilador.Compilador.tablaSimbolo.get(sval).size());
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

	if(na.charAt(0) >= '0' && na.charAt(0) <= '9') 
		if(na.contains("_") && na.contains("i")){
			na = na.toString().substring(0, na.length()-2); 
		}
	
	if(ns.charAt(0) >= '0' && ns.charAt(0) <= '9') 
		if(ns.contains("_") && ns.contains("i")){
			ns = ns.toString().substring(0, ns.length()-2); 
		}

	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setTipo("Proc");
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setCantParametros(Integer.parseInt(cantParametros));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNa(Integer.parseInt(na));
	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setNs(Integer.parseInt(ns));
	
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
		else{
			Double flotante = Double.parseDouble(sval.replace('f', 'E'));
			if (sval.contains("f")) {
				if(String.valueOf(flotante).contains("E"))
					sval = String.valueOf(flotante).replace('E', 'f');
			}
			if (sval.contains("."))
				sval = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
		}
	}		

	compilador.Compilador.tablaSimbolo.get(sval).get(compilador.Compilador.tablaSimbolo.get(sval).size()-1).setAmbito(sval, false);
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
//#line 782 "Parser.java"
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
//#line 16 "gramatica.y"
{
	polaca.mostrarProcs();
	mostrarMensaje("Reconoce bien el programa");
	System.out.println(polaca.toString());
}
break;
case 2:
//#line 21 "gramatica.y"
{
	yyerror("Programa invalido, error en linea: " + compilador.Compilador.nroLinea);
}
break;
case 3:
//#line 27 "gramatica.y"
{
}
break;
case 4:
//#line 30 "gramatica.y"
{
}
break;
case 5:
//#line 33 "gramatica.y"
{
}
break;
case 6:
//#line 36 "gramatica.y"
{
}
break;
case 7:
//#line 41 "gramatica.y"
{
	mostrarMensaje("Declaracion de una o mas variables en linea nro: " + compilador.Compilador.nroLinea);
}
break;
case 8:
//#line 45 "gramatica.y"
{
	Par retorno = new Par("RET");
	polaca.agregarPaso(retorno);
}
break;
case 9:
//#line 50 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");
	}
}
break;
case 10:
//#line 57 "gramatica.y"
{
	setearAmbitoyDeclarada(val_peek(0).sval,"");
	if(sePuedeUsar(val_peek(0).sval) == 2){
		mostrarMensaje(val_peek(0).sval + " esta Redeclarada.");
	}
}
break;
case 11:
//#line 66 "gramatica.y"
{
	mostrarMensaje("Procedimiento completo, en linea nro: " + compilador.Compilador.nroLinea);
	disminuirAmbito();
	compilador.Compilador.na = compilador.Compilador.na + compilador.Compilador.naa;
}
break;
case 13:
//#line 74 "gramatica.y"
{
	Par proc = new Par(val_peek(9).sval);
	polaca.agregarProcedimiento(proc);
	mostrarMensaje("Procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc(val_peek(9).sval, "0", val_peek(4).sval, val_peek(0).sval);
	setearAmbito(val_peek(9).sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" + val_peek(9).sval;
	setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
	if(sePuedeUsar(val_peek(9).sval) == 2){
		mostrarMensaje(val_peek(9).sval + " esta Redeclarada.");
	}
	verificarNa(val_peek(4).sval,val_peek(9).sval);
}
break;
case 14:
//#line 87 "gramatica.y"
{
	Par proc = new Par(val_peek(11).sval);
	polaca.agregarProcedimiento(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc(val_peek(11).sval, "1", val_peek(4).sval, val_peek(0).sval);
	setearAmbito(val_peek(11).sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(11).sval;
	setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
	if(sePuedeUsar(val_peek(11).sval) == 2){
		mostrarMensaje(val_peek(11).sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
}
break;
case 15:
//#line 99 "gramatica.y"
{
	Par proc = new Par(val_peek(14).sval);
	polaca.agregarProcedimiento(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc(val_peek(14).sval, "2", val_peek(4).sval, val_peek(0).sval);
	setearAmbito(val_peek(14).sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" +  val_peek(14).sval;
	setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
	if(sePuedeUsar(val_peek(14).sval) == 2){
		mostrarMensaje(val_peek(14).sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
	setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
}
break;
case 16:
//#line 112 "gramatica.y"
{
	Par proc = new Par(val_peek(17).sval);
	polaca.agregarProcedimiento(proc);
	mostrarMensaje("Procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);
	setearProc(val_peek(17).sval, "3", val_peek(4).sval, val_peek(0).sval);
	setearAmbito(val_peek(17).sval);
	compilador.Compilador.ambito = compilador.Compilador.ambito + ":" + val_peek(17).sval;
	setearAmbitoNaNs(val_peek(4).sval,val_peek(0).sval);
	if(sePuedeUsar(val_peek(17).sval) == 2){
		mostrarMensaje(val_peek(17).sval + " esta Redeclarada.");
	}
	setearAmbitoyDeclarada(val_peek(14).sval,val_peek(15).sval);
	setearAmbitoyDeclarada(val_peek(11).sval,val_peek(12).sval);
	setearAmbitoyDeclarada(val_peek(8).sval,val_peek(9).sval);
}
break;
case 17:
//#line 126 "gramatica.y"
{
	yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);
}
break;
case 18:
//#line 132 "gramatica.y"
{
}
break;
case 19:
//#line 135 "gramatica.y"
{
}
break;
case 20:
//#line 138 "gramatica.y"
{
}
break;
case 21:
//#line 143 "gramatica.y"
{
}
break;
case 22:
//#line 148 "gramatica.y"
{
}
break;
case 23:
//#line 151 "gramatica.y"
{
}
break;
case 24:
//#line 154 "gramatica.y"
{
}
break;
case 25:
//#line 157 "gramatica.y"
{
}
break;
case 26:
//#line 160 "gramatica.y"
{
	yyerror("Error: no puede haber un seccion vacia, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 27:
//#line 166 "gramatica.y"
{
}
break;
case 28:
//#line 169 "gramatica.y"
{
	mostrarMensaje("Sentencia OUT, en linea " + compilador.Compilador.nroLinea);
	Par out = new Par(val_peek(4).sval);
	Par cadena = new Par(val_peek(2).sval);
	polaca.agregarPaso(cadena);
	polaca.agregarPaso(out);
}
break;
case 29:
//#line 177 "gramatica.y"
{
	yyerror("Error: Formato de cadena incorrecto, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 30:
//#line 181 "gramatica.y"
{
	String nombreProc = val_peek(4).sval;
	System.out.println(sePuedeUsar(nombreProc));
	if (sePuedeUsar(nombreProc)==0) {
		String pos = polaca.buscarInicioProc(nombreProc);
		Par iniProc = new Par(pos);
		polaca.agregarPaso(iniProc);
	}	
	mostrarMensaje("Llamada a procedimiento con parametros en linea nro: " + compilador.Compilador.nroLinea);
}
break;
case 31:
//#line 185 "gramatica.y"
{
	String nombreProc = val_peek(3).sval;
	if (sePuedeUsar(nombreProc)==0) {
		String pos = polaca.buscarInicioProc(nombreProc);
		Par iniProc = new Par(pos); 
		polaca.agregarPaso(iniProc);
	}	
	mostrarMensaje("Llamada a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);
}
break;
case 32:
//#line 189 "gramatica.y"
{
	yyerror("Error: Cantidad no permitida de parametros, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 33:
//#line 193 "gramatica.y"
{
	if (PolacaInversa.getFlagITE()){
		polaca.completarPolaca(PolacaInversa.getRetrocesosITE());
	}
	else
		polaca.completarPolaca(PolacaInversa.getRetrocesosIT());
}
break;
case 34:
//#line 201 "gramatica.y"
{
	mostrarMensaje("Ciclo FOR en linea nro: " + compilador.Compilador.nroLinea);
}
break;
case 35:
//#line 207 "gramatica.y"
{
	polaca.borrarVariablesControl();
	Par pasoEnBlanco = new Par("");
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI");
	polaca.agregarPaso(pasoBI);
	polaca.completarFOR();
	polaca.borrarInicioFOR();
	polaca.borrarPasoIncompleto();
	polaca.completarPolaca(PolacaInversa.getRetrocesosFOR());
}
break;
case 36:
//#line 222 "gramatica.y"
{
	polaca.borrarPasoPolaca();
}
break;
case 37:
//#line 228 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 38:
//#line 238 "gramatica.y"
{
	polaca.agregarVariableControl(val_peek(2).sval);
	Par id = new Par(val_peek(2).sval);
	polaca.agregarPaso(id);
	Par asig = new Par(val_peek(1).sval);
	polaca.agregarPaso(asig);
	polaca.agregarInicioFOR();
}
break;
case 39:
//#line 249 "gramatica.y"
{
	Par id = new Par(val_peek(2).sval);
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
break;
case 40:
//#line 256 "gramatica.y"
{
	Par id1 = new Par(val_peek(2).sval);
	Par id2 = new Par(val_peek(0).sval);
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id1);
	polaca.agregarPaso(id2);
	polaca.agregarPaso(comp);
}
break;
case 41:
//#line 265 "gramatica.y"
{
	Par id = new Par(val_peek(2).sval);
	Par comp = new Par(val_peek(1).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(comp);
}
break;
case 42:
//#line 274 "gramatica.y"
{
	polaca.agregarVariableControl("+");
	polaca.agregarVariableControl(val_peek(0).sval);
}
break;
case 43:
//#line 279 "gramatica.y"
{
	polaca.agregarVariableControl("-");
	polaca.agregarVariableControl(val_peek(0).sval);
}
break;
case 44:
//#line 284 "gramatica.y"
{
	yyerror("Error: incremento/decremento mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 45:
//#line 290 "gramatica.y"
{
}
break;
case 46:
//#line 293 "gramatica.y"
{
}
break;
case 47:
//#line 298 "gramatica.y"
{
	PolacaInversa.setFlagITE(true);
}
break;
case 48:
//#line 302 "gramatica.y"
{
	PolacaInversa.setFlagITE(false); 
	polaca.borrarPasoPolaca();
	polaca.borrarPasoPolaca();
	polaca.borrarPasoIncompleto();
}
break;
case 49:
//#line 311 "gramatica.y"
{
}
break;
case 50:
//#line 314 "gramatica.y"
{
}
break;
case 51:
//#line 317 "gramatica.y"
{
}
break;
case 52:
//#line 320 "gramatica.y"
{
}
break;
case 53:
//#line 325 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBF = new Par("BF"); 
	polaca.agregarPaso(pasoBF);
}
break;
case 54:
//#line 335 "gramatica.y"
{
	Par pasoEnBlanco = new Par(""); 
	polaca.agregarPaso(pasoEnBlanco);
	polaca.agregarPasoIncompleto();
	Par pasoBI = new Par("BI"); 
	polaca.agregarPaso(pasoBI);
}
break;
case 55:
//#line 345 "gramatica.y"
{
}
break;
case 56:
//#line 350 "gramatica.y"
{
}
break;
case 57:
//#line 353 "gramatica.y"
{
}
break;
case 58:
//#line 358 "gramatica.y"
{
	setearAmbito(val_peek(3).sval);
	if(sePuedeUsar(val_peek(3).sval) == 1){
		mostrarMensaje(val_peek(3).sval + " No esta declarada.");
	}
	Par id =  new Par(val_peek(3).sval);
	Par asig = new Par(val_peek(2).sval);
	polaca.agregarPaso(id);
	polaca.agregarPaso(asig);
}
break;
case 59:
//#line 369 "gramatica.y"
{
	yyerror("Error: identificador mal escrito, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 60:
//#line 375 "gramatica.y"
{
	Par suma =  new Par("+");
	polaca.agregarPaso(suma);
}
break;
case 61:
//#line 380 "gramatica.y"
{
	Par resta =  new Par("-");
	polaca.agregarPaso(resta);
}
break;
case 62:
//#line 385 "gramatica.y"
{
}
break;
case 63:
//#line 390 "gramatica.y"
{
	Par multi =  new Par("*");
	polaca.agregarPaso(multi);
}
break;
case 64:
//#line 395 "gramatica.y"
{ 
	Par division =  new Par("/");
	polaca.agregarPaso(division);
}
break;
case 65:
//#line 400 "gramatica.y"
{
}
break;
case 66:
//#line 405 "gramatica.y"
{
}
break;
case 67:
//#line 408 "gramatica.y"
{ 
	setearAmbito(val_peek(0).sval);
	if(sePuedeUsar(val_peek(0).sval) == 1)
		{mostrarMensaje(val_peek(0).sval + " No esta declarada.");
	}
    Par id =  new Par(val_peek(0).sval);
	polaca.agregarPaso(id);
}
break;
case 68:
//#line 419 "gramatica.y"
{
}
break;
case 69:
//#line 422 "gramatica.y"
{
}
break;
case 70:
//#line 425 "gramatica.y"
{
}
break;
case 71:
//#line 428 "gramatica.y"
{
}
break;
case 72:
//#line 431 "gramatica.y"
{
}
break;
case 73:
//#line 434 "gramatica.y"
{
}
break;
case 74:
//#line 437 "gramatica.y"
{
	yyerror("Error: comparador no permitido, en linea nro: "+ compilador.Compilador.nroLinea);
}
break;
case 75:
//#line 443 "gramatica.y"
{
}
break;
case 76:
//#line 446 "gramatica.y"
{
}
break;
case 77:
//#line 451 "gramatica.y"
{
}
break;
case 78:
//#line 456 "gramatica.y"
{
}
break;
case 79:
//#line 459 "gramatica.y"
{
}
break;
case 80:
//#line 463 "gramatica.y"
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
case 81:
//#line 479 "gramatica.y"
{
	/*yyerror("Error: constante positiva mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);*/
}
break;
case 82:
//#line 485 "gramatica.y"
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
	Par cte = new Par('-'+valor);
	polaca.agregarPaso(cte);
}
break;
case 83:
//#line 501 "gramatica.y"
{
	/*yyerror("Error: constante negativa mal escrita, en linea nro: "+ compilador.Compilador.nroLinea);	*/
}
break;
//#line 1520 "Parser.java"
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
