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
b@Main dd ?
a@Main dd ?
c@Main dw ?
.code
overflow:
invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK 
call fin
divcero:
invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK 
call fin
main:
FNINIT
FNCLEX
FLD b@Main
FIADD c@Main
FSTP @aux1
FLD @aux1
FCOMP _MAX
FSTSW @aux2
MOV AX, @aux2
SAHF
JA overflow
MOV EBX, @aux1
MOV a@Main, EBX
FNINIT
FNCLEX
fin: invoke ExitProcess, 0
end main
