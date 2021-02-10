.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
mensaje db "Mensaje por pantalla", 0
errorOverflowSuma db "Ha ocurrido overflow al sumar, programa terminado inesperadamente", 0
errorDivisionCero db "Se intenta dividir por cero, programa terminado por su seguridad", 0
_MAX dd 2147483647
@aux2 dw ?
@aux1 dd ?
@aux2 dw ?
@aux3 dd ?
@aux4 dd ?
@aux6 dd ?
@aux7 dd ?
@aux8 dw ?
@aux9 dd ?
@aux10 dw ?
@aux11 dd ?
@aux12 dw ?
@aux13 dd ?
id4@Main@f3 dw ?
id3@Main@f3@f1 dw ?
id2@Main@f3@f1 dw ?
id1@Main@a dw ?
ax2@Main@f3 dd ?
ax1@Main@f3@f1 dd ?
ax@Main dd ?
_45 dw 45
h@Main dd ?
g@Main@b@d@f dd ?
e@Main@b@d dd ?
c@Main@b dd ?
id6@Main@f3 dw ?
id5@Main@f3 dw ?
_20 dw 20
.code
overflow:
invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK 
call fin
divcero:
invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK 
call fin
a@Main:
FILD _45
FADD h@Main
FSTP @aux1
FLD @aux1
FCOMP _MAX
FSTSW @aux2
MOV AX, @aux2
SAHF
JA overflow
MOV EBX, @aux1
MOV ax@Main, EBX
ret
f1@Main@f3:
FILD _20
FMUL ax2@Main@f3
FSTP @aux4
MOV EBX, @aux4
MOV ax2@Main@f3, EBX
ret
f3@Main:
ret
f@Main@b@d:
FLD c@Main@b
FADD g@Main@b@d@f
FSTP @aux7
FLD @aux7
FCOMP _MAX
FSTSW @aux8
MOV AX, @aux8
SAHF
JA overflow
FLD @aux7
FADD e@Main@b@d
FSTP @aux9
FLD @aux9
FCOMP _MAX
FSTSW @aux10
MOV AX, @aux10
SAHF
JA overflow
FLD @aux9
FADD h@Main
FSTP @aux11
FLD @aux11
FCOMP _MAX
FSTSW @aux12
MOV AX, @aux12
SAHF
JA overflow
MOV EBX, @aux11
MOV c@Main@b, EBX
ret
d@Main@b:
ret
b@Main:
ret
main:
call d@Main@b
call d@Main@b
fin: invoke ExitProcess, 0
end main
