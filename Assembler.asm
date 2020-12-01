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
@aux1 dd ?
@aux2 dw ?
@aux3 dd ?
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
MOV AX, 45
CWDE
MOV EBX, EAX
MOV ax@Main, EBX
ret
f1@Main@f3:
MOV AX, 20
CWDE
MOV EBX, EAX
MOV ax2@Main@f3, EBX
ret
f3@Main:
ret
f@Main@b@d:
FLD c@Main@b
FADD g@Main@b@d@f
FSTP @aux1
FLD @aux1
FCOMP _MAX
FSTSW @aux2
MOV AX, @aux2
SAHF
JA overflow
MOV EBX, @aux1
MOV c@Main@b, EBX
ret
d@Main@b:
ret
b@Main:
ret
main:
fin: invoke ExitProcess, 0
end main
