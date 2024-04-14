global perform_sepia_pixel

%macro load_float 2 
  movss %1, %2 
  shufps %1, %1, 0
%endmacro

section .rodata

r: dd 0.272, 0.349, 0.393, 0
g: dd 0.534, 0.686, 0.769, 0
b: dd 0.131, 0.168, 0.189, 0

section .text
perform_sepia_pixel:
  load_float xmm0, [rdi]
  load_float xmm1, [rdi + 4]
  load_float xmm2, [rdi + 8]

  movaps   xmm3, [r]
  movaps   xmm4, [g]
  movaps   xmm5, [b]

  mulps   xmm0, xmm3
  mulps   xmm1, xmm4
  mulps   xmm2, xmm5

  addps   xmm0, xmm1
  addps   xmm0, xmm2

  cvtps2dq  xmm0, xmm0

  packusdw  xmm0, xmm0
  packuswb  xmm0, xmm0

  movd      [rsi], xmm0
  ret

