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
id1@Main@a@b dd ?
id1@Main@a@b@cc dd ?
hola db "hola", 0
_90 dw 90
z@Main dw ?
ii@Main dd ?
_77 dw 77
_44 dw 44
i@Main@a dw ?
jj@Main dd ?
_2 dw 2
_1 dw 1
_21 dw 21
_20 dw 20
.code
overflow:
invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK 
call fin
divcero:
invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK 
call fin
cc@Main@a@b:
MOV AX, 90
CWDE
MOV EBX, EAX
MOV id1@Main@a@b, EBX
ret
b@Main@a:
MOV AX, 2
CWDE
MOV EBX, EAX
MOV id1@Main@a@b, EBX
MOV AX, 77
CWDE
MOV EBX, EAX
MOV id1@Main@a@b, EBX
ret
c@Main@a:
MOV BX, i@Main@a
CMP BX, 20
JNE L30
call L30
invoke MessageBox, NULL, addr hola, addr mensaje, MB_OK
ret
a@Main:
MOV BX, 44
MOV z@Main, BX
call b@Main@a
ret
main:
MOV BX, 20
MOV z@Main, BX
L30:
MOV BX, 21
MOV z@Main, BX
FLD ii@Main
FIADD _1
FSTP @aux1
FLD @aux1
FCOMP _MAX
FSTSW @aux2
MOV AX, @aux2
SAHF
JA overflow
MOV EBX, @aux1
MOV jj@Main, EBX
fin: invoke ExitProcess, 0
end main
