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
import java.util.Stack;
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
public final static short CTE=257;
public final static short ID=258;
public final static short IF=259;
public final static short THEN=260;
public final static short ELSE=261;
public final static short END_IF=262;
public final static short OUT=263;
public final static short FUNC=264;
public final static short RETURN=265;
public final static short FOR=266;
public final static short INTEGER=267;
public final static short FLOAT=268;
public final static short PROC=269;
public final static short NS=270;
public final static short NA=271;
public final static short CADENA=272;
public final static short UP=273;
public final static short DOWN=274;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    3,    3,    3,    3,
    3,    4,    4,    6,   11,   11,   11,   11,   11,   11,
    7,    7,    9,   13,   14,   15,   16,   16,   12,   12,
   12,   17,   17,   17,   17,   17,   17,    8,    8,   18,
   18,   18,   19,   19,   19,   20,   20,   10,    5,    5,
};
final static short yylen[] = {                            2,
    1,    2,    2,    1,    1,   13,    1,    1,    1,    4,
    4,    3,    1,    3,    2,    2,    2,    1,    1,    1,
    9,    7,    5,    6,    3,    3,    1,    1,    3,    2,
    2,    1,    1,    1,    1,    1,    1,    4,    3,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
   48,    0,    0,    0,    0,    0,    0,    4,    5,    7,
    8,    9,    0,    0,    0,    0,    0,    2,    3,    0,
    0,    0,    0,   49,    0,   46,   47,    0,    0,    0,
   45,    0,    0,    0,    0,    0,    0,    0,   13,    0,
    0,   50,    0,   31,   32,   33,   36,   37,    0,    0,
   34,   35,    0,    0,    0,   10,    0,    0,    0,    0,
   11,    0,   38,    0,    0,    0,    0,   43,   44,   25,
    0,   23,    0,    0,    0,   12,    0,   18,   20,   19,
    0,    0,    0,    0,    0,    0,   22,   14,   17,   15,
   16,   26,   27,   28,    0,    0,    0,   24,    0,   21,
    0,    0,    0,    0,    6,
};
final static short yydgoto[] = {                          6,
    7,    8,    9,   38,   26,   72,   10,   11,   12,   27,
   82,   28,   34,   35,   74,   95,   53,   29,   30,   31,
};
final static short yysindex[] = {                      -187,
    0,  -28,  -14,   -1, -215,    0, -187,    0,    0,    0,
    0,    0,  -33,  -40, -222, -201,   26,    0,    0,  -32,
 -201,  -32,  -32,    0, -189,    0,    0,   23,  -43,    6,
    0,   29,   28,   36,   32, -201,  -10,   14,    0,  -18,
  -10,    0, -175,    0,    0,    0,    0,    0,  -32,  -32,
    0,    0,  -32,  -32,  -32,    0,  -30,  -26, -201,   18,
    0, -201,    0,  -26,    6,    6,  -10,    0,    0,    0,
 -228,    0,  -56,   42, -171,    0, -188,    0,    0,    0,
  -53, -116, -201, -193,   43,  -26,    0,    0,    0,    0,
    0,    0,    0,    0, -201,  -30, -160,    0,   59,    0,
 -165,   49,  -30,  -26,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,  108,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,    0,    0,    0,    3,    0,    0,    0,
   71,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   24,   47,   72,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  107,  109,   79,  -47,  -50,  -60,  -42,  -37,   16,
    0,    0,    0,    0,    0,    0,   45,   -2,   44,   41,
};
final static int YYTABLESIZE=325;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   42,   50,   39,   51,   25,   52,   21,   22,   88,   70,
   78,   14,   25,   77,   25,   13,   51,   37,   52,   40,
   41,   89,   13,   40,   49,   15,   50,   22,   79,    1,
    2,   33,   49,   80,   50,   97,   39,    4,   16,   90,
   63,   42,   17,   42,   91,   42,   41,   54,   99,   32,
   67,   39,   55,  105,   61,  104,    1,   62,   75,   42,
   42,   62,   42,   43,   40,   36,   40,   42,   40,   56,
    1,    2,   86,   87,   73,    3,   58,   76,    4,   93,
   94,    5,   40,   40,   64,   40,   81,   41,   57,   41,
   59,   41,   65,   66,   68,   69,   71,   81,   92,   85,
   84,  100,  101,   96,  102,   41,   41,    1,   41,  103,
   98,   30,   29,   18,   60,   19,    0,   83,    0,    0,
    0,    0,    0,    0,    0,   42,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    2,    0,    0,    0,    0,    0,   40,    4,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   41,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   20,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   44,    0,    0,   23,   24,    1,   45,   46,
   47,   48,   20,    0,   24,    1,   24,    0,    0,    0,
    0,   45,   46,   47,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   42,    0,   42,   42,
   39,   39,    0,   42,    0,   39,   42,    0,   39,   42,
    0,   39,    0,    0,    0,   42,   42,   42,   42,   40,
    0,   40,   40,    0,    0,    0,   40,    0,    0,   40,
    0,    0,   40,    0,    0,    0,    0,    0,   40,   40,
   40,   40,   41,    0,   41,   41,    0,    0,    0,   41,
    0,    0,   41,    0,    0,   41,    0,    0,    0,    0,
    0,   41,   41,   41,   41,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         43,
    0,   45,    0,   60,   45,   62,   40,   61,  125,   57,
   71,   40,   45,   64,   45,    0,   60,   20,   62,   22,
   23,   82,    7,    0,   43,   40,   45,   61,   71,  258,
  259,   16,   43,   71,   45,   86,   21,  266,   40,   82,
   59,   41,  258,   43,   82,   45,    0,   42,   96,  272,
   53,   36,   47,  104,   41,  103,  258,   44,   41,   59,
   60,   44,   62,   41,   41,   40,   43,  257,   45,   41,
  258,  259,  261,  262,   59,  263,   41,   62,  266,  273,
  274,  269,   59,   60,  260,   62,   71,   41,   61,   43,
   59,   45,   49,   50,   54,   55,  123,   82,   83,  271,
   59,  262,   44,   61,  270,   59,   60,    0,   62,   61,
   95,   41,   41,    7,   36,    7,   -1,   73,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  125,   -1,  125,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  258,  259,   -1,   -1,   -1,   -1,   -1,  125,  266,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,  256,  257,  258,  275,  276,
  277,  278,  256,   -1,  257,  258,  257,   -1,   -1,   -1,
   -1,  275,  276,  277,  278,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,  258,  259,
  258,  259,   -1,  263,   -1,  263,  266,   -1,  266,  269,
   -1,  269,   -1,   -1,   -1,  275,  276,  277,  278,  256,
   -1,  258,  259,   -1,   -1,   -1,  263,   -1,   -1,  266,
   -1,   -1,  269,   -1,   -1,   -1,   -1,   -1,  275,  276,
  277,  278,  256,   -1,  258,  259,   -1,   -1,   -1,  263,
   -1,   -1,  266,   -1,   -1,  269,   -1,   -1,   -1,   -1,
   -1,  275,  276,  277,  278,
};
}
final static short YYFINAL=6;
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
null,null,null,null,null,null,null,"CTE","ID","IF","THEN","ELSE","END_IF","OUT",
"FUNC","RETURN","FOR","INTEGER","FLOAT","PROC","NS","NA","CADENA","UP","DOWN",
"\"<=\"","\">=\"","\"!=\"","\"==\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloquePrograma",
"bloquePrograma : bloquePrograma sentenciaDeclarativa",
"bloquePrograma : bloquePrograma sentenciaEjecutables",
"bloquePrograma : sentenciaDeclarativa",
"bloquePrograma : sentenciaEjecutables",
"sentenciaDeclarativa : PROC ID '(' listaParametros ')' NA '=' constante ',' NS '=' constante bloqueSentencias",
"sentenciaEjecutables : sentenciaIf",
"sentenciaEjecutables : asignacion",
"sentenciaEjecutables : cicloFor",
"sentenciaEjecutables : OUT '(' CADENA ')'",
"sentenciaEjecutables : identificador '(' listaParametros ')'",
"listaParametros : listaParametros ',' identificador",
"listaParametros : identificador",
"bloqueSentencias : '{' listaSentencias '}'",
"listaSentencias : listaSentencias asignacion",
"listaSentencias : listaSentencias cicloFor",
"listaSentencias : listaSentencias sentenciaIf",
"listaSentencias : sentenciaIf",
"listaSentencias : cicloFor",
"listaSentencias : asignacion",
"sentenciaIf : IF '(' condicion ')' THEN bloqueSentencias ELSE bloqueSentencias END_IF",
"sentenciaIf : IF '(' condicion ')' THEN bloqueSentencias END_IF",
"cicloFor : FOR '(' condicionFor ')' bloqueSentencias",
"condicionFor : inicioFor ';' comparacionFor ';' incDecFor identificador",
"inicioFor : identificador '=' constante",
"comparacionFor : identificador comparador identificador",
"incDecFor : UP",
"incDecFor : DOWN",
"condicion : expresion comparador expresion",
"condicion : error expresion",
"condicion : expresion error",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : '<'",
"comparador : '>'",
"comparador : \"!=\"",
"comparador : \"==\"",
"asignacion : identificador '=' expresion ';'",
"asignacion : identificador error expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : constante",
"factor : identificador",
"identificador : ID",
"constante : CTE",
"constante : '-' CTE",
};

//#line 118 "gramatica.y"

public Simbolo verificarRango(String num){
	System.out.println("El CTE NEGATIVO es "+num);	
	return null;
}

private void imprimir(String str) throws IOException {
	System.out.println(str);
	this.reconocidos.add(str + " linea " +this.lineaActual);
	
}



//#line 333 "Parser.java"
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
int yyparse() throws IOException
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
//#line 13 "gramatica.y"
{imprimir("Reconoce Programa");}
break;
case 6:
//#line 22 "gramatica.y"
{imprimir("Reconocio declaracion de procedimiento");}
break;
case 7:
//#line 25 "gramatica.y"
{imprimir("Reconoce If");}
break;
case 8:
//#line 26 "gramatica.y"
{imprimir("Reconoce Asignacion");}
break;
case 9:
//#line 27 "gramatica.y"
{imprimir("Reconoce For");}
break;
case 10:
//#line 28 "gramatica.y"
{imprimir("Reconoce Out");}
break;
case 12:
//#line 32 "gramatica.y"
{imprimir("Reconoce ListaParametros");}
break;
case 14:
//#line 36 "gramatica.y"
{imprimir("Reconoce bloqueSentencias");}
break;
case 15:
//#line 39 "gramatica.y"
{imprimir("Reconoce Lista sentencias asignacion");}
break;
case 16:
//#line 40 "gramatica.y"
{imprimir("Reconoce Lista sentencias CicloFor");}
break;
case 17:
//#line 41 "gramatica.y"
{imprimir("Reconoce Lista sentencias SentenciaIf");}
break;
case 21:
//#line 47 "gramatica.y"
{imprimir("Reconoce If completo");}
break;
case 22:
//#line 48 "gramatica.y"
{imprimir("Reconoce If parcial");}
break;
case 23:
//#line 51 "gramatica.y"
{imprimir("Reconoce cilcoFor");}
break;
case 24:
//#line 54 "gramatica.y"
{imprimir("Reconoce condicionesDelFor");}
break;
case 25:
//#line 57 "gramatica.y"
{imprimir("Reconoce inicioFor");}
break;
case 26:
//#line 59 "gramatica.y"
{imprimir("Reconoce comparacionFor	");}
break;
case 27:
//#line 62 "gramatica.y"
{imprimir("Reconoce UP");}
break;
case 28:
//#line 63 "gramatica.y"
{imprimir("Reconoce DOWN");}
break;
case 29:
//#line 66 "gramatica.y"
{imprimir("Reconoce condicion");}
break;
case 30:
//#line 67 "gramatica.y"
{yyerror("Error en la parte izquierda de la condición");}
break;
case 31:
//#line 68 "gramatica.y"
{yyerror("Error en la parte derecha de la condición");}
break;
case 32:
//#line 71 "gramatica.y"
{yyval.obj = "<=";}
break;
case 33:
//#line 72 "gramatica.y"
{yyval.obj = ">=";}
break;
case 34:
//#line 73 "gramatica.y"
{yyval.obj = "<";}
break;
case 35:
//#line 74 "gramatica.y"
{yyval.obj = ">";}
break;
case 36:
//#line 75 "gramatica.y"
{yyval.obj = "!=";}
break;
case 37:
//#line 76 "gramatica.y"
{yyval.obj = "==";}
break;
case 38:
//#line 79 "gramatica.y"
{imprimir("Reconocio Asignacion");}
break;
case 39:
//#line 80 "gramatica.y"
{yyerror("Error en el operador de asignacion, se espera ==");}
break;
case 40:
//#line 85 "gramatica.y"
{imprimir("Reconocio suma");}
break;
case 41:
//#line 86 "gramatica.y"
{imprimir("Reconocio resta");}
break;
case 43:
//#line 90 "gramatica.y"
{imprimir("Reconocio multiplicaion");}
break;
case 44:
//#line 91 "gramatica.y"
{imprimir("Reconocio division");}
break;
case 48:
//#line 99 "gramatica.y"
{ 
							{imprimir("Reconoce palabra reservada");}
                            Simbolo s = c.tablaSimbolo.get(val_peek(0).sval);                                              
                            yyval.obj = s;
                        }
break;
case 49:
//#line 106 "gramatica.y"
{
                        Simbolo s = c.tablaSimbolo.get(val_peek(0).sval);
                        yyval.obj = s;
                    }
break;
case 50:
//#line 111 "gramatica.y"
{
						Simbolo s = verificarRango("");
						  // Simbolo s = verificarRango(val_peek(0).sval);
							yyval.obj = s;
						}
break;
//#line 640 "Parser.java"
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
 * @throws IOException 
 */
public void run() throws IOException
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .
 */

// Definiciones propias

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

public int yylex() throws IOException {
	Token token = c.getToken();
	this.lineaActual = token.getLinea();
	yylval = new ParserVal(t);
	yylval.sval = token.getLexema();
	return token.getToken();
}

public void yyerror(String error){
	this.errores.add(error + " en linea " + this.lineaActual)	;
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
