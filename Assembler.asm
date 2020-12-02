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
@aux1 dw ?
@aux2 dd ?
@aux3 dw ?
@aux4 dd ?
b@Main dd ?
a@Main dd ?
hola db "hola", 0
c@Main dw ?
.code
overflow:
invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK 
call fin
divcero:
invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK 
call fin
main:
FLD a@Main
FICOMP c@Main
FSTSW @aux1
MOV AX, @aux1
SAHF
JAE L12
FLD b@Main
FIADD c@Main
FSTP @aux2
FLD @aux2
FCOMP _MAX
FSTSW @aux3
MOV AX, @aux3
SAHF
JA overflow
MOV EBX, @aux2
MOV a@Main, EBX
JMP L15
L12:
invoke MessageBox, NULL, addr hola, addr mensaje, MB_OK
L15:
fin: invoke ExitProcess, 0
end main
