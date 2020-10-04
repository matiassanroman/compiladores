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
    7,   21,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,    3,    1,    3,    3,    1,
    2,   12,   11,   12,    1,    3,    5,    2,    3,    3,
    2,    2,    1,    1,    1,    5,    5,    3,    1,    5,
    3,    7,    5,    3,    3,    3,    3,    2,    2,    2,
    1,    1,    1,   10,    7,    4,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,   61,    0,    0,    0,   60,   59,    0,    0,    0,
    4,    5,    0,    7,    0,    0,   25,   29,    0,   10,
    0,    0,    0,   42,   43,    0,    0,    0,    2,    3,
    0,    0,    0,    0,   11,    8,    0,   31,    0,    0,
   28,    0,    0,    0,    0,    0,    0,    6,    0,    0,
    0,   62,   54,   53,    0,    0,   52,    0,   23,   24,
    0,    9,   55,   56,   57,   58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   18,    0,    0,    0,
    0,   46,    0,    0,   20,   19,   21,   22,    0,   35,
   37,    0,   30,   26,   34,    0,    0,    0,    0,    0,
   27,    0,    0,    0,   50,   51,   41,    0,    0,    0,
    0,    0,    0,    0,    0,   40,   32,    0,    0,   33,
    0,    0,    0,   17,    0,   38,   39,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   44,    0,
   13,    0,   14,   12,
};
final static short yydgoto[] = {                          9,
   10,   11,  107,   13,   19,   14,   15,   16,   35,   50,
   51,   61,   17,   23,   18,   45,  108,   46,   40,  120,
   54,   67,   24,   25,   55,   56,   57,
};
final static short yysindex[] = {                      -188,
 -238,    0,  -39,  -13,   -7,    0,    0, -238,    0, -188,
    0,    0, -238,    0,  -33, -102,    0,    0,  -21,    0,
 -218, -238, -208,    0,    0, -217, -238,   15,    0,    0,
   -3, -204, -239, -159,    0,    0, -238,    0, -158,   34,
    0,   40,   45,   41,   54,   55,  -41,    0, -238,   75,
   77,    0,    0,    0,  -19,   24,    0, -121,    0,    0,
 -123,    0,    0,    0,    0,    0, -239,   -1,   64,   65,
 -149,    4, -238,   85, -142,   88,    0,   71, -204, -239,
 -239,    0, -239, -239,    0,    0,    0,    0,   70,    0,
    0, -174,    0,    0,    0, -174,   73, -133,   79, -129,
    0,  106,   24,   24,    0,    0,    0, -111, -109, -200,
   91, -204,   94, -204, -101,    0,    0, -149, -149,    0,
 -204,  116, -204,    0,   38,    0,    0,  119, -105,  122,
 -174,  -99,  107,  -98, -100,  108, -204,  111,    0, -204,
    0, -204,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  167,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  132,    0,    0,    0,    0,  -11,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -30,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  133,    2,    6,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -4,    3,  -27,  163,    0,    9,    0,    0,  130,
  -70,    0,  112,    0,    0,    0,  -81,    0,  105,    0,
  -59,    0,    0,    0,    0,   20,   21,
};
final static int YYTABLESIZE=226;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         75,
   22,   86,   12,   85,   49,   29,   32,   91,  102,   20,
   36,   95,   30,  115,  109,  117,   28,    2,    2,   49,
   34,   20,   37,   80,  139,   81,   26,   33,   36,   59,
   39,   49,   27,   49,   52,   44,   60,   36,   42,   82,
   37,   53,   38,  124,   47,   62,   47,   49,   48,  135,
   48,   49,   41,   43,   47,   48,   87,   77,  126,  127,
   47,    6,    7,   88,   48,   83,   20,    1,    2,    3,
   84,  118,  119,    4,   68,   89,    5,    6,    7,    8,
   69,   39,    2,    3,  122,   70,   49,    4,   53,   53,
    5,   53,   53,  128,   72,  130,   58,    2,    3,  103,
  104,   71,    4,  105,  106,    5,    6,    7,    8,  141,
  116,  116,  143,   73,  144,   78,   63,   64,   65,   66,
   79,   92,   93,   94,   52,   98,   96,   99,  100,  101,
   33,  110,    1,    2,    3,    2,  111,  116,    4,  112,
  113,    5,    6,    7,    8,    2,    3,    2,    3,  114,
    4,  121,    4,    5,  123,    5,    2,    3,  125,  129,
  131,    4,  132,  133,    5,  134,    1,  137,  140,  136,
  138,  142,   15,   16,   45,   31,   76,   97,   90,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   74,    0,   21,    0,    0,    0,
    0,    0,    0,    0,    6,    7,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,  125,    0,  125,   32,   10,   40,   67,   79,    1,
   41,   71,   10,  125,   96,  125,    8,  257,  257,   47,
  123,   13,   44,   43,  125,   45,   40,   61,   59,   34,
   22,   43,   40,   45,  274,   27,   34,   59,  256,   59,
   44,   33,  261,  114,   43,   37,   45,   59,   43,  131,
   45,   79,  261,  271,   40,   59,   61,   49,  118,  119,
   59,  266,  267,   61,   59,   42,   58,  256,  257,  258,
   47,  272,  273,  262,   41,   67,  265,  266,  267,  268,
   41,   73,  257,  258,  112,   41,  114,  262,   80,   81,
  265,   83,   84,  121,   41,  123,  256,  257,  258,   80,
   81,   61,  262,   83,   84,  265,  266,  267,  268,  137,
  108,  109,  140,   59,  142,   41,  275,  276,  277,  278,
   44,  123,   59,   59,  274,   41,  123,  270,   41,   59,
   61,   59,  256,  257,  258,  257,  270,  135,  262,   61,
  270,  265,  266,  267,  268,  257,  258,  257,  258,   44,
  262,   61,  262,  265,   61,  265,  257,  258,  260,   44,
  123,  262,   44,  269,  265,   44,    0,   61,   61,  269,
  269,   61,   41,   41,  261,   13,   47,   73,   67,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,  256,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  266,  267,
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
"cuerpoIncompleto : '(' condicion ')' '{' bloqueSentencia '}' ELSE",
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
};

//#line 130 "gramatica.y"

void mostrarMensaje(String mensaje){
	System.out.println(mensaje);
}



//#line 342 "Parser.java"
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
case 8:
//#line 23 "gramatica.y"
{yyerror("Error en la sentencia, tipo invalido");}
break;
case 11:
//#line 30 "gramatica.y"
{mostrarMensaje("Reconocio procedimiento completo");}
break;
case 14:
//#line 35 "gramatica.y"
{yyerror("Error en los parametros de procedimiento");}
break;
case 18:
//#line 42 "gramatica.y"
{yyerror("Error en el parametro, tipo invalido");}
break;
case 19:
//#line 45 "gramatica.y"
{mostrarMensaje("Reconocio bloque de procedimiento");}
break;
case 20:
//#line 46 "gramatica.y"
{yyerror("Error en el cuerpo del procedimiento");}
break;
case 30:
//#line 60 "gramatica.y"
{yyerror("Error en la cadena");}
break;
case 31:
//#line 61 "gramatica.y"
{yyerror("Error en el cuerpo del IF");}
break;
case 32:
//#line 64 "gramatica.y"
{mostrarMensaje("Reconocio ciclo FOR");}
break;
case 33:
//#line 67 "gramatica.y"
{mostrarMensaje("Reconocio condicion del FOR");}
break;
case 38:
//#line 78 "gramatica.y"
{mostrarMensaje("Reconocio incremento-UP del FOR");}
break;
case 39:
//#line 79 "gramatica.y"
{mostrarMensaje("Reconocio decremento-UP del FOR");}
break;
case 44:
//#line 90 "gramatica.y"
{mostrarMensaje("Reconocio IF con cuerpo en ELSE");}
break;
case 45:
//#line 93 "gramatica.y"
{mostrarMensaje("Reconocio IF sin cuerpo en ELSE");}
break;
case 46:
//#line 96 "gramatica.y"
{mostrarMensaje("Reconocio Asignacion");}
break;
case 47:
//#line 99 "gramatica.y"
{mostrarMensaje("Reconocio suma");}
break;
case 48:
//#line 100 "gramatica.y"
{mostrarMensaje("Reconocio resta");}
break;
case 49:
//#line 101 "gramatica.y"
{mostrarMensaje("Reconocio termino");}
break;
case 50:
//#line 104 "gramatica.y"
{mostrarMensaje("Reconocio multiplicacion");}
break;
case 51:
//#line 105 "gramatica.y"
{mostrarMensaje("Reconocio division");}
break;
case 52:
//#line 106 "gramatica.y"
{mostrarMensaje("Reconocio factor");}
break;
case 55:
//#line 113 "gramatica.y"
{mostrarMensaje("Reconocio comparador menor-igual");}
break;
case 56:
//#line 114 "gramatica.y"
{mostrarMensaje("Reconocio comparador mayor-igual");}
break;
case 57:
//#line 115 "gramatica.y"
{mostrarMensaje("Reconocio comparador distinto");}
break;
case 58:
//#line 116 "gramatica.y"
{mostrarMensaje("Reconocio comparador igual");}
break;
case 59:
//#line 119 "gramatica.y"
{mostrarMensaje("Reconocio tipo FLOAT");}
break;
case 60:
//#line 120 "gramatica.y"
{mostrarMensaje("Reconocio tipo INTEGER");}
break;
case 61:
//#line 123 "gramatica.y"
{mostrarMensaje("Reconocio identificador");}
break;
case 62:
//#line 126 "gramatica.y"
{mostrarMensaje("Reconocio constante");}
break;
//#line 611 "Parser.java"
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