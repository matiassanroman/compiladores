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
   19,   19,   20,   20,   17,   17,   14,   14,   23,   24,
   13,   25,   25,   25,   26,   26,   26,   27,   27,   22,
   22,   22,   22,   22,   22,    4,    4,    7,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    1,    2,   12,   11,   12,    1,    3,    5,    2,    2,
    3,    3,    2,    2,    1,    1,    1,    5,    5,    4,
    3,    1,    5,    3,    7,    7,    7,    5,    3,    3,
    3,    3,    2,    2,    2,    1,    1,    1,   10,    6,
    4,    3,    3,    1,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   68,    0,    0,    0,   67,   66,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   27,   32,   11,    0,
   10,    0,    0,    0,   47,   48,    0,    0,    0,    2,
    3,    0,    0,    0,    0,   12,    8,    0,   34,    0,
    0,   31,    0,    0,    0,    0,    0,    0,    0,    6,
    0,    0,    0,    0,    0,   69,    0,   59,   58,    0,
    0,   57,    0,   25,   26,    0,    9,   60,   61,   62,
   63,   64,   65,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   20,   30,   19,    0,    0,   70,
    0,    0,   51,    0,    0,   22,   21,   23,   24,    0,
   40,   42,    0,   33,   28,    0,   39,    0,    0,    0,
    0,    0,   29,    0,    0,    0,   55,   56,   46,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,
   36,   37,   35,    0,    0,   38,    0,    0,    0,   18,
    0,   43,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   49,    0,   14,    0,   15,   13,
};
final static short yydgoto[] = {                          9,
   10,   11,  119,   13,   20,   14,   15,   16,   36,   54,
   55,   66,   17,   24,   18,   47,  120,   48,   41,  136,
   59,   74,   25,   26,   60,   61,   62,
};
final static short yysindex[] = {                      -177,
 -232,    0,  -37,  -14,   -8,    0,    0, -212,    0, -177,
    0,    0, -232,    0,  -19,  -70,    0,    0,    0,   -9,
    0, -205, -212, -201,    0,    0, -220, -191,   31,    0,
    0,   -1,  -39,  -45, -158,    0,    0, -212,    0,  -53,
   32,    0,   37,   42,   53,   40,   64,   52,  -35,    0,
 -212,   55, -212,   81,   84,    0, -145,    0,    0,  -12,
   22,    0, -117,    0,    0, -120,    0,    0,    0,    0,
    0,    0,    0,  -45,    7,   72,   73,   10,  -44,   11,
 -212,  -31, -135,  100,    0,    0,    0,   85, -174,    0,
  -45,  -45,    0,  -45,  -45,    0,    0,    0,    0,   82,
    0,    0,  -86,    0,    0,  -86,    0, -139,   94, -112,
  103, -104,    0,  125,   22,   22,    0,    0,    0, -108,
 -106,   48,  -97, -186,  113,  -99,  116, -174,  -82,    0,
    0,    0,    0,  -44,  -44,    0,  -94,  137,  -92,    0,
   60,    0,    0,  140,  -84,  142,  -86,  -81,  126,  -80,
  -95,  129,  -83,  131,    0,  -79,    0,  -78,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  193,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  153,    0,    0,    0,    0,    0,
   -5,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -18,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  156,    3,   18,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -63,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    2,    4,  -22,  186,    0,   21,    0,    0,  151,
  -76,    0,  127,    0,    0,    0,  -90,    0,  121,    0,
  -59,    0,    0,    0,    0,    5,   26,
};
final static int YYTABLESIZE=232;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   57,   52,   23,   12,   97,   83,   73,   96,   72,  110,
   53,   30,  114,   31,  102,  121,  129,  123,  131,  107,
   33,   21,   41,   19,    2,   27,   53,  133,   29,  155,
   91,   28,   92,   21,   38,   43,   64,   54,   65,   54,
   41,   34,   38,   40,    2,   52,   93,   52,   46,   37,
   44,  140,   35,   54,   58,   39,  151,   50,   67,   42,
   53,   52,   53,   94,   45,    2,   53,   98,   95,   99,
   49,   85,   75,   87,  142,  143,   53,   76,    1,    2,
    3,   51,   77,   21,    4,  134,  135,    5,    6,    7,
    8,    6,    7,   78,  100,  115,  116,   63,    2,    3,
   79,   40,   85,    4,   80,   53,    5,    6,    7,    8,
   81,   58,   58,   86,   58,   58,  122,    2,    3,  117,
  118,   88,    4,  130,  130,    5,  130,   89,   90,  103,
  104,  105,  106,  108,  111,    1,    2,    3,   19,    2,
  112,    4,   34,  113,    5,    6,    7,    8,    2,    3,
    2,    3,  124,    4,  130,    4,    5,  125,    5,    2,
    3,    2,    3,  126,    4,  127,    4,    5,  128,    5,
    2,    3,  132,  137,  138,    4,  139,  141,    5,  144,
  145,  146,  147,  148,  149,  150,  153,  152,  154,  156,
  157,  158,    1,   16,  159,  160,   17,   50,   32,   84,
  101,  109,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    0,    0,    0,    0,   51,    0,   22,    0,
   82,   68,   69,   70,   71,    2,    6,    7,   56,   56,
    6,    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   41,   40,    0,  125,   41,   60,  125,   62,   41,
   33,   10,   89,   10,   74,  106,  125,  108,  125,   79,
   40,    1,   41,  256,  257,   40,   49,  125,    8,  125,
   43,   40,   45,   13,   44,  256,   35,   43,   35,   45,
   59,   61,   44,   23,  257,   43,   59,   45,   28,   59,
  271,  128,  123,   59,   34,  261,  147,   59,   38,  261,
   43,   59,   45,   42,  256,  257,   89,   66,   47,   66,
   40,   51,   41,   53,  134,  135,   59,   41,  256,  257,
  258,  256,   41,   63,  262,  272,  273,  265,  266,  267,
  268,  266,  267,   41,   74,   91,   92,  256,  257,  258,
   61,   81,   82,  262,   41,  128,  265,  266,  267,  268,
   59,   91,   92,   59,   94,   95,  256,  257,  258,   94,
   95,   41,  262,  120,  121,  265,  123,   44,  274,  123,
   59,   59,  123,  123,  270,  256,  257,  258,  256,  257,
   41,  262,   61,   59,  265,  266,  267,  268,  257,  258,
  257,  258,   59,  262,  151,  262,  265,  270,  265,  257,
  258,  257,  258,   61,  262,  270,  262,  265,   44,  265,
  257,  258,  125,   61,  274,  262,   61,  260,  265,  274,
   44,  274,  123,   44,  269,   44,   61,  269,  269,   61,
  274,   61,    0,   41,  274,  274,   41,  261,   13,   49,
   74,   81,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,   -1,   -1,   -1,   -1,  256,   -1,  256,   -1,
  256,  275,  276,  277,  278,  257,  266,  267,  274,  274,
  266,  267,
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
"encabezadoProc : PROC identificador '(' parametrosProc ')' NA '=' CTE ',' NS '=' CTE",
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

//#line 139 "gramatica.y"

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



//#line 355 "Parser.java"
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
//#line 59 "gramatica.y"
{mostrarMensaje("Reconocio OUT CADENA en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 29:
//#line 60 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento con parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 30:
//#line 61 "gramatica.y"
{mostrarMensaje("Reconocio llamda a procedimiento sin parametros en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 32:
//#line 63 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 33:
//#line 64 "gramatica.y"
{yyerror("Error en la cadena en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 34:
//#line 65 "gramatica.y"
{yyerror("Error en el cuerpo del IF en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 36:
//#line 69 "gramatica.y"
{yyerror("Error en la condicion del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 37:
//#line 70 "gramatica.y"
{yyerror("Error en el cuerpo del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 38:
//#line 73 "gramatica.y"
{mostrarMensaje("Reconocio encabezado del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 43:
//#line 84 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 44:
//#line 85 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 49:
//#line 96 "gramatica.y"
{mostrarMensaje("Reconocio IF con cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 50:
//#line 99 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 51:
//#line 102 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 52:
//#line 105 "gramatica.y"
{mostrarMensaje("Reconocio suma en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 53:
//#line 106 "gramatica.y"
{mostrarMensaje("Reconocio resta en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 55:
//#line 110 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 56:
//#line 111 "gramatica.y"
{mostrarMensaje("Reconocio division en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 60:
//#line 119 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 61:
//#line 120 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 62:
//#line 121 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 63:
//#line 122 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 64:
//#line 123 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 65:
//#line 124 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 66:
//#line 127 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 67:
//#line 128 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 68:
//#line 131 "gramatica.y"
{mostrarMensaje("Reconocio identificador en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 69:
//#line 134 "gramatica.y"
{mostrarMensaje("Reconocio constante en linea nro: "+compilador.Compilador.nroLinea);}
break;
case 70:
//#line 135 "gramatica.y"
{mostrarMensaje("Reconocio constante negativa en linea nro: "+compilador.Compilador.nroLinea);}
break;
//#line 668 "Parser.java"
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