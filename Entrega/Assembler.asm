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
_32766 dw 32766
j@Main dw ?
_2 dw 2
i@Main dw ?
.code
overflow:
invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK 
call fin
divcero:
invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK 
call fin
main:
MOV BX, 32766
MOV i@Main, BX
MOV BX, i@Main
ADD BX, 2
CMP BX, 32767
JA overflow
MOV j@Main, BX
fin: invoke ExitProcess, 0
end main
