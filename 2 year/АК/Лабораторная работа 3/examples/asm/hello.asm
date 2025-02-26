_start:
            PUSH    0x65       ; [0x65]
            PUSH    welcome     ; [0x65; welcome]
print_invitation:
            OVER                ; [0x65; welcome; 0x65]
            OVER                ; [0x65; welcome; 0x65; welcome]
            LOAD                ; [0x65; welcome; 0x65; "W"]
            TEST                ; [0x65; welcome; 0x65; "W"]
            PUSH    save_null   ; [0x65; welcome; 0x65; "W"; save_null]
            JZ                  ; [0x65; welcome; 0x65; "W"]
            SWAP                ; [0x65; welcome; "W"; 0x65]
            SAVE                ; [0x65; welcome]
            INC                 ; [0x65; welcome++]
            PUSH   print_invitation  ; [[0x65; welcome; print_invitation]
            JUMP                ; [0x65; welcome]

save_null:                      ; [0x65; welcome; 0x65; "\0"]
            SWAP                ; [0x65; welcome; "\0"; 0x65]
            SAVE                ; [0x65; welcome]
            POP                 ; [0x65]
            PUSH    data        ; [0x65; data]
read_input:
            OVER                ; [0x65; data; 0x65]
            LOAD                ; [0x65; data; "A"]
            TEST                ; [0x65; data; "A"]
            PUSH   print_greeting  ; [0x65; data; "A"; print_greeting]
            JZ                  ; [0x65; data; "A"]
            OVER                ; [0x65; data; "A"; data]
            SAVE                ; [0x65; data]
            INC                 ; [0x65; data++]
            PUSH    read_input  ; [0x65; data; read_input]
            JUMP                ; [0x65; data]

print_greeting:                 ; [0x65; data; "\0"]
            POP                 ; [0x65; data]
            POP                 ; [0x65]
            PUSH    greeting    ; [0x65; greeting]
print_data:
            OVER                ; [0x65; greeting; 0x65]
            OVER                ; [0x65; greeting; 0x65; greeting]
            LOAD                ; [0x65; greeting; 0x65; "H"]
            TEST                ; [0x65; greeting; 0x65; "H"]
            PUSH    print_name  ; [0x65; greeting; 0x65; "H"; print_name]
            JZ                  ; [0x65; greeting; 0x65; "H"]
            SWAP                ; [0x65; greeting; "A"; 0x65]
            SAVE                ; [0x65; greeting]
            INC                 ; [0x65; greeting++]
            PUSH   print_data   ; [0x65; greeting; print_data]
            JUMP

print_name:                     ; [0x65; greeting; 0x65; "\0"]
            POP                 ; [0x65; greeting; 0x65]
            POP                 ; [0x65; greeting]
            POP                 ; [0x65]
            PUSH    data        ; [0x65; data]
print_name_data:
            OVER                ; [0x65; data; 0x65]
            OVER                ; [0x65; data; 0x65; data]
            LOAD                ; [0x65; data; 0x65; "A"]
            TEST                ; [0x65; data; 0x65; "A"]
            PUSH    exit        ; [0x65; data; 0x65; "A"; exit]
            JZ                  ; [0x65; data; 0x65; "A"]
            SWAP                ; [0x65; data; "A"; 0x65]
            SAVE                ; [0x65; data]
            INC                 ; [0x65; data++]
            PUSH    print_name_data
            JUMP

exit:
            HALT



welcome:
            WORD    "What`s your name?"
greeting:
            WORD    "Hello, "
data:
            WORD    0x0
