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
    6,    8,    8,    8,   10,   10,   10,   11,    9,    9,
   12,   12,   12,   12,    3,    3,    3,    3,    3,    3,
    3,   15,   16,   18,   19,   19,   19,   20,   20,   17,
   17,   14,   14,   23,   24,   13,   25,   25,   25,   26,
   26,   26,   27,   27,   22,   22,   22,   22,    4,    4,
    7,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    2,   12,   11,   12,    1,    3,    5,    2,    3,    3,
    2,    2,    1,    1,    1,    5,    5,    3,    1,    5,
    3,    7,    5,    3,    3,    3,    3,    2,    2,    2,
    1,    1,    1,   10,    6,    4,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   61,    0,    0,    0,   60,   59,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   25,   29,    0,   10,
    0,    0,    0,   42,   43,    0,    0,    0,    2,    3,
    0,    0,    0,    0,   11,    8,    0,   31,    0,    0,
   28,    0,    0,    0,    0,    0,    0,    6,    0,    0,
    0,   62,    0,   54,   53,    0,    0,   52,    0,   23,
   24,    0,    9,   55,   56,   57,   58,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   18,    0,    0,
   63,    0,    0,   46,    0,    0,   20,   19,   21,   22,
    0,   35,   37,    0,   30,   26,   34,    0,    0,    0,
    0,    0,   27,    0,    0,    0,   50,   51,   41,    0,
    0,    0,    0,    0,    0,    0,    0,   40,   32,    0,
    0,   33,    0,    0,    0,   17,    0,   38,   39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   44,    0,   13,    0,   14,   12,
};
final static short yydgoto[] = {                          9,
   10,   11,  109,   13,   19,   14,   15,   16,   35,   50,
   51,   62,   17,   23,   18,   45,  110,   46,   40,  122,
   55,   68,   24,   25,   56,   57,   58,
};
final static short yysindex[] = {                      -185,
 -240,    0,  -37,  -10,   16,    0,    0, -240,    0, -185,
    0,    0, -240,    0,  -20,  -76,    0,    0,  -19,    0,
 -196, -240, -193,    0,    0, -207, -240,   29,    0,    0,
    7, -213,  -45, -155,    0,    0, -240,    0, -154,   35,
    0,   37,   43,   25,   49,   40,  -39,    0, -240,   65,
   64,    0, -165,    0,    0,  -22,   10,    0, -119,    0,
    0, -121,    0,    0,    0,    0,    0,  -45,   -5,   61,
   66,  -44,    3, -240,   86, -142,   88,    0,   71, -213,
    0,  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0,
   70,    0,    0, -170,    0,    0,    0, -170,   73, -137,
   78, -136,    0,   96,   10,   10,    0,    0,    0, -109,
 -107, -214,   82, -213,   91, -213, -106,    0,    0,  -44,
  -44,    0, -213,  113, -213,    0,   38,    0,    0,  118,
 -104,  119, -170, -103,  107, -100,  -98,  109, -213,  110,
    0, -213,    0, -213,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  172,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0,    0,    0,  -17,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  133,  -14,   -9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -86,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    1,    5,  -25,  163,    0,   11,    0,    0,  130,
  -70,    0,  111,    0,    0,    0,  -90,    0,  104,    0,
  -59,    0,    0,    0,    0,   -8,   19,
};
final static int YYTABLESIZE=230;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         53,
   53,   76,   22,   88,   12,   87,   49,  111,   93,  104,
   29,   20,   97,   36,   30,  117,    2,  119,   28,   32,
   82,   49,   83,   20,   37,   49,  141,   49,   47,   26,
   47,   36,   39,   48,   60,   48,   84,   44,   61,   36,
   33,   49,  137,   54,   47,  126,   34,   63,   42,   48,
   37,   85,    6,    7,   49,   27,   86,  120,  121,   78,
  128,  129,   89,   43,   38,   48,   90,   41,   47,   20,
    1,    2,    3,  105,  106,   69,    4,   70,   91,    5,
    6,    7,    8,   71,   39,   72,    2,    3,  124,   73,
   49,    4,   54,   54,    5,   54,   54,  130,   74,  132,
   59,    2,    3,  107,  108,   79,    4,   80,   81,    5,
    6,    7,    8,  143,  118,  118,  145,   94,  146,   95,
   64,   65,   66,   67,   96,   98,  100,  101,  102,  103,
   33,  112,  113,  115,    1,    2,    3,    2,  114,  116,
    4,  118,  123,    5,    6,    7,    8,    2,    3,    2,
    3,  125,    4,  127,    4,    5,  131,    5,    2,    3,
  133,  134,  136,    4,  135,  138,    5,  139,  140,  142,
  144,    1,   15,   16,   45,   31,   77,   99,   92,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    0,    0,    0,    0,   75,    0,   21,    0,
    0,    0,    0,    0,    0,    0,    6,    7,   52,   52,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   40,  125,    0,  125,   32,   98,   68,   80,
   10,    1,   72,   41,   10,  125,  257,  125,    8,   40,
   43,   47,   45,   13,   44,   43,  125,   45,   43,   40,
   45,   59,   22,   43,   34,   45,   59,   27,   34,   59,
   61,   59,  133,   33,   59,  116,  123,   37,  256,   59,
   44,   42,  266,  267,   80,   40,   47,  272,  273,   49,
  120,  121,   62,  271,  261,   59,   62,  261,   40,   59,
  256,  257,  258,   82,   83,   41,  262,   41,   68,  265,
  266,  267,  268,   41,   74,   61,  257,  258,  114,   41,
  116,  262,   82,   83,  265,   85,   86,  123,   59,  125,
  256,  257,  258,   85,   86,   41,  262,   44,  274,  265,
  266,  267,  268,  139,  110,  111,  142,  123,  144,   59,
  275,  276,  277,  278,   59,  123,   41,  270,   41,   59,
   61,   59,  270,  270,  256,  257,  258,  257,   61,   44,
  262,  137,   61,  265,  266,  267,  268,  257,  258,  257,
  258,   61,  262,  260,  262,  265,   44,  265,  257,  258,
  123,   44,   44,  262,  269,  269,  265,   61,  269,   61,
   61,    0,   41,   41,  261,   13,   47,   74,   68,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,   -1,   -1,   -1,   -1,  256,   -1,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  266,  267,  274,  274,
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
"declaracionProcedimiento : encabezadoProc bloqueProc",
"encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' tipo ',' NS '=' tipo",
"encabezadoProc : PROC identificador '(' ')' NA '=' tipo ',' NS '=' tipo",
"encabezadoProc : PROC identificador '(' error ')' NA '=' tipo ',' NS '=' tipo",
"parametrosProc : parametro",
"parametrosProc : parametro ',' parametro",
"parametrosProc : parametro ',' parametro ',' parametro",
"parametro : tipo identificador",
"bloqueProc : '{' bloque '}'",
"bloqueProc : '{' error '}'",
"bloque : bloque sentenciaDeclarativa",
"bloque : bloque sentenciaEjecutable",
"bloque : sentenciaDeclarativa",
"bloque : sentenciaEjecutable",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : OUT '(' CADENA ')' ';'",
"sentenciaEjecutable : identificador '(' parametrosProc ')' ';'",
"sentenciaEjecutable : IF cuerpoIf END_IF",
"sentenciaEjecutable : cicloFor",
"sentenciaEjecutable : OUT '(' error ')' ';'",
"sentenciaEjecutable : IF error END_IF",
"cicloFor : FOR '(' condicionFor ')' '{' bloqueSentencia '}'",
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
"tipo : FLOAT",
"tipo : INTEGER",
"identificador : ID",
"constante : CTE",
"constante : '-' CTE",
};

//#line 132 "gramatica.y"

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



//#line 343 "Parser.java"
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
{mostrarMensaje("Reconocio declaracion de una o mas variables");}
break;
case 8:
//#line 23 "gramatica.y"
{yyerror("Error en la sentencia, tipo invalido");}
break;
case 11:
//#line 30 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo");}
break;
case 12:
//#line 33 "gramatica.y"
{mostrarMensaje("Reconocio PROC con parametros");}
break;
case 13:
//#line 34 "gramatica.y"
{mostrarMensaje("Reconocio PROC sin parametros");}
break;
case 14:
//#line 35 "gramatica.y"
{yyerror("Error en los parametros de procedimiento");}
break;
case 18:
//#line 43 "gramatica.y"
{mostrarMensaje("Reconocio parametro");}
break;
case 19:
//#line 46 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento");}
break;
case 20:
//#line 47 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento");}
break;
case 26:
//#line 57 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA");}
break;
case 27:
//#line 58 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento");}
break;
case 29:
//#line 60 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR");}
break;
case 30:
//#line 61 "gramatica.y"
{yyerror("Error en la cadena");}
break;
case 31:
//#line 62 "gramatica.y"
{yyerror("Error en el cuerpo del IF");}
break;
case 33:
//#line 68 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR");}
break;
case 38:
//#line 79 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR");}
break;
case 39:
//#line 80 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR");}
break;
case 44:
//#line 91 "gramatica.y"
{mostrarMensaje("Reconocio IF con cuerpo en ELSE");}
break;
case 45:
//#line 94 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE");}
break;
case 46:
//#line 97 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion");}
break;
case 47:
//#line 100 "gramatica.y"
{mostrarMensaje("Reconocio suma");}
break;
case 48:
//#line 101 "gramatica.y"
{mostrarMensaje("Reconocio resta");}
break;
case 50:
//#line 105 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion");}
break;
case 51:
//#line 106 "gramatica.y"
{mostrarMensaje("Reconocio division");}
break;
case 55:
//#line 114 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual");}
break;
case 56:
//#line 115 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual");}
break;
case 57:
//#line 116 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto");}
break;
case 58:
//#line 117 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual");}
break;
case 59:
//#line 120 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT");}
break;
case 60:
//#line 121 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER");}
break;
case 61:
//#line 124 "gramatica.y"
{mostrarMensaje("Reconocio identificador");}
break;
case 62:
//#line 127 "gramatica.y"
{mostrarMensaje("Reconocio constante");}
break;
case 63:
//#line 128 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa");}
break;
//#line 628 "Parser.java"
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
	this.errores.add(error + " en linea " + this.lineaActual)	;
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
