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
//#line 19 "Parser.java"

import java.io.IOException;
import java.util.ArrayList;

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
    5,    6,    8,    8,    8,   10,   10,   10,   11,   11,
    9,    9,   12,   12,   12,   12,    3,    3,    3,    3,
    3,    3,    3,    3,   15,   15,   15,   16,   18,   19,
   19,   19,   20,   20,   17,   17,   14,   14,   23,   23,
   23,   24,   24,   24,   13,   25,   25,   25,   26,   26,
   26,   27,   27,   22,   22,   22,   22,    4,    4,    7,
   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   12,   11,   12,    1,    3,    5,    2,    2,
    3,    3,    2,    2,    1,    1,    1,    5,    5,    4,
    3,    1,    5,    3,    7,    7,    7,    5,    3,    3,
    3,    3,    2,    2,    2,    1,    1,    1,   10,   10,
   10,    6,    6,    6,    4,    3,    3,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,   70,    0,    0,    0,   69,   68,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   27,   32,   11,    0,
   10,    0,    0,    0,   47,   48,    0,    0,    0,    2,
    3,    0,    0,    0,    0,   12,    8,    0,   34,    0,
    0,    0,   31,    0,    0,    0,    0,    0,    0,    0,
    6,    0,    0,    0,    0,    0,   71,    0,   63,   62,
    0,    0,   61,    0,   25,   26,    0,    9,    0,   64,
   65,   66,   67,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   20,   30,   19,    0,    0,   72,
    0,    0,   55,    0,    0,   22,   21,   23,   24,    0,
    0,   40,   42,    0,   33,   28,    0,   39,    0,    0,
    0,    0,    0,   29,    0,    0,    0,   59,   60,   46,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   45,    0,    0,   36,   37,   35,    0,    0,
   38,    0,    0,    0,   18,    0,    0,    0,   43,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   50,   51,   49,    0,   14,
    0,   15,   13,
};
final static short yydgoto[] = {                          9,
   10,   11,  120,   13,   20,   14,   15,   16,   36,   55,
   56,   67,   17,   24,   18,   48,  121,   49,   42,  141,
   60,   74,   25,   26,   61,   62,   63,
};
final static short yysindex[] = {                      -178,
 -204,    0,  -37,  -29,  -22,    0,    0, -216,    0, -178,
    0,    0, -204,    0,  -30,  -79,    0,    0,    0,  -12,
    0, -206, -165, -202,    0,    0, -222, -125,   22,    0,
    0,    1,  -39,  -45, -157,    0,    0, -216,    0,   42,
 -180,   44,    0,   76,   81,   83,   65,   88,   82,  -35,
    0, -216,   94, -216,  114,   91,    0,  -97,    0,    0,
  -21,   23,    0, -117,    0,    0, -120,    0,   55,    0,
    0,    0,    0,  -45,   60,  125,  130,   67,  -44,   70,
 -216,  -34,  -78,  158,    0,    0,    0,  143, -199,    0,
  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0, -142,
  142,    0,    0, -144,    0,    0, -142,    0,  -61,  146,
  -64,  147,  -63,    0,  165,   23,   23,    0,    0,    0,
 -108,   85, -106,  -95,   89,  -89, -139,  154, -123,  155,
 -199,  -42,    0,  -40,  -38,    0,    0,    0,  -44,  -44,
    0, -123,  180, -123,    0,  102,  103,  111,    0,    0,
  191,  -33,  193, -142, -142, -142,  -28,  178,  -27,  -83,
  -77,  -71,  182, -123,  183,    0,    0,    0, -123,    0,
 -123,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  245,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  205,    0,    0,    0,    0,
    0,  -16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -26,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  206,   13,   18,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -13,    0,  -11,  -10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    2,    4,   69,  236,    0,   12,    0,    0,  202,
  -80,    0,  179,    0,    0,    0,  -81,    0,  173,    0,
  -58,    0,    0,    0,    0,   80,   66,
};
final static int YYTABLESIZE=254;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   58,   53,   23,   12,   97,   83,  111,   96,  115,   33,
   27,   30,   21,   31,   41,  103,  132,   28,  135,   29,
  108,   91,  123,   92,   21,  124,   58,  126,   58,  136,
   34,   38,   41,   44,   41,  138,   65,   93,   66,   47,
    2,  166,   58,   35,   38,   59,   37,  167,   45,   68,
  145,   19,    2,  168,   39,   56,   52,   56,   43,   51,
   57,   50,   57,   85,   94,   87,    6,    7,   98,   95,
   99,   56,  160,  161,  162,   21,   57,    1,    2,    3,
  149,  150,   69,    4,   75,  101,    5,    6,    7,    8,
   40,    2,   41,   85,   70,   71,   72,   73,   64,    2,
    3,   54,   59,   59,    4,   59,   59,    5,    6,    7,
    8,  122,    2,    3,    2,    3,   76,    4,   54,    4,
    5,   77,    5,   78,  133,   79,  133,  133,   80,  133,
   46,    2,  139,  140,   89,    1,    2,    3,   19,    2,
   81,    4,    6,    7,    5,    6,    7,    8,    2,    3,
    2,    3,   86,    4,   88,    4,    5,   54,    5,  118,
  119,    2,    3,  133,  133,  133,    4,    2,    3,    5,
  116,  117,    4,    2,    3,    5,   90,  100,    4,    2,
    3,    5,  104,  105,    4,    2,    3,    5,  106,  107,
    4,  112,  109,    5,  125,    2,    3,  143,  113,   54,
    4,  114,   34,    5,  127,  128,  130,  129,  131,  134,
  151,    2,  153,  137,  142,  144,   52,  146,   22,  147,
   82,  148,    2,  152,  154,  155,    6,    7,   57,   57,
    6,    7,  170,  156,  157,  158,  159,  172,  164,  173,
  163,  165,  169,  171,    1,   16,   17,   53,   32,   54,
   52,   84,  102,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   40,    0,  125,   41,   41,  125,   89,   40,
   40,   10,    1,   10,   41,   74,  125,   40,  125,    8,
   79,   43,  104,   45,   13,  107,   43,  109,   45,  125,
   61,   44,   59,  256,   23,  125,   35,   59,   35,   28,
  257,  125,   59,  123,   44,   34,   59,  125,  271,   38,
  131,  256,  257,  125,  261,   43,  256,   45,  261,   59,
   43,   40,   45,   52,   42,   54,  266,  267,   67,   47,
   67,   59,  154,  155,  156,   64,   59,  256,  257,  258,
  139,  140,   41,  262,   41,   74,  265,  266,  267,  268,
  256,  257,   81,   82,  275,  276,  277,  278,  256,  257,
  258,   33,   91,   92,  262,   94,   95,  265,  266,  267,
  268,  256,  257,  258,  257,  258,   41,  262,   50,  262,
  265,   41,  265,   41,  121,   61,  123,  124,   41,  126,
  256,  257,  272,  273,   44,  256,  257,  258,  256,  257,
   59,  262,  266,  267,  265,  266,  267,  268,  257,  258,
  257,  258,   59,  262,   41,  262,  265,   89,  265,   94,
   95,  257,  258,  160,  161,  162,  262,  257,  258,  265,
   91,   92,  262,  257,  258,  265,  274,  123,  262,  257,
  258,  265,  123,   59,  262,  257,  258,  265,   59,  123,
  262,  270,  123,  265,  256,  257,  258,  129,   41,  131,
  262,   59,   61,  265,   59,  270,  270,   61,   44,  125,
  142,  257,  144,  125,   61,   61,  256,  260,  256,  260,
  256,  260,  257,   44,  123,  123,  266,  267,  274,  274,
  266,  267,  164,  123,   44,  269,   44,  169,   61,  171,
  269,  269,   61,   61,    0,   41,   41,  261,   13,  261,
  261,   50,   74,   81,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
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
"encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' tipo ',' NS '=' tipo",
"encabezadoProc : PROC identificador '(' ')' NA '=' tipo ',' NS '=' tipo",
"encabezadoProc : PROC identificador '(' error ')' NA '=' tipo ',' NS '=' tipo",
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
"cuerpoCompleto : '(' error ')' '{' bloqueSentencia '}' ELSE '{' bloqueSentencia '}'",
"cuerpoCompleto : '(' condicion ')' '{' error '}' ELSE '{' bloqueSentencia '}'",
"cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}'",
"cuerpoIncompleto : '(' error ')' '{' bloqueSentencia '}'",
"cuerpoIncompleto : '(' condicion ')' '{' error '}'",
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
"tipo : FLOAT",
"tipo : INTEGER",
"identificador : ID",
"constante : CTE",
"constante : '-' CTE",
};

//#line 143 "gramatica.y"

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



//#line 369 "Parser.java"
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
{mostrarMensaje("Reconoce bien el programa");}
break;
case 6:
//#line 21 "gramatica.y"
{mostrarMensaje("Reconocio declaracion de una o mas variables en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 8:
//#line 23 "gramatica.y"
{yyerror("Error, tipo invalido en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 11:
//#line 28 "gramatica.y"
{yyerror("Error en la o las varibles, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 12:
//#line 31 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 13:
//#line 34 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 14:
//#line 35 "gramatica.y"
{mostrarMensaje("Reconocio PROC sin parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 15:
//#line 36 "gramatica.y"
{yyerror("Error en los parametros de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 19:
//#line 44 "gramatica.y"
{mostrarMensaje("Reconocio parametro en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 20:
//#line 45 "gramatica.y"
{yyerror("Error, tipo invalido en el parametro, en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 21:
//#line 48 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 22:
//#line 49 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 28:
//#line 61 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 29:
//#line 62 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 30:
//#line 63 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 32:
//#line 65 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 33:
//#line 66 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 34:
//#line 67 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 71 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 37:
//#line 72 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 38:
//#line 75 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 43:
//#line 86 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 44:
//#line 87 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 49:
//#line 98 "gramatica.y"
{mostrarMensaje("Reconocio IF con cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 50:
//#line 99 "gramatica.y"
{yyerror("Error en condicion de IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 51:
//#line 100 "gramatica.y"
{yyerror("Error en bloque de sentecias de IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 52:
//#line 103 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 53:
//#line 104 "gramatica.y"
{yyerror("Error en bloque de sentecias de IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 54:
//#line 105 "gramatica.y"
{yyerror("Error en el bloque de sentencias del ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 55:
//#line 108 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 56:
//#line 111 "gramatica.y"
{mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 57:
//#line 112 "gramatica.y"
{mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 59:
//#line 116 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 60:
//#line 117 "gramatica.y"
{mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 64:
//#line 125 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 65:
//#line 126 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 66:
//#line 127 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 67:
//#line 128 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 68:
//#line 131 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 69:
//#line 132 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 70:
//#line 135 "gramatica.y"
{mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 71:
//#line 138 "gramatica.y"
{mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 72:
//#line 139 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);}
break;
//#line 690 "Parser.java"
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
//Definiciones propias

Compilador c;
ArrayList<String> errores = new ArrayList<String>();
Token t;
int lineaActual;
ArrayList<String> reconocidos = new ArrayList<String>();

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
  /*
  if(i == 0) {
    Token token = new Token(258,0);
    token.setLexema("asd");
    token.setLinea(1);
    i=i+1;
    this.lineaActual = token.getLinea();
    yylval = new ParserVal(t);
    yylval.sval = token.getLexema();
    System.out.println("entra id");
    return 258;
  }
  else if(i == 1) {
    Token token = new Token(61,0);
    token.setLinea(1);
    i=i+1;
    this.lineaActual = token.getLinea();
    yylval = new ParserVal(t);
    yylval.sval = token.getLexema();
    System.out.println("entra =");
    return 61;
  }
  else if(i == 2) {
    Token token = new Token(258,0);
    token.setLexema("dsa");
    token.setLinea(1);
    i=i+1;
    this.lineaActual = token.getLinea();
    yylval = new ParserVal(t);
    yylval.sval = token.getLexema();
    System.out.println("entra id");
    return 258;
  }
  else if(i == 3) {
    Token token = new Token(59,0);
    token.setLinea(1);
    i=i+1;
    this.lineaActual = token.getLinea();
    yylval = new ParserVal(t);
    yylval.sval = token.getLexema();
    System.out.println("entra ;");
    return 59;
  }
  return 0;*/
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
