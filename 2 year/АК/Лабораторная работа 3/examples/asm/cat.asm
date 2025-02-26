_start:
            PUSH    0x65       ; [0x65]
loop:
            DUP                 ; [0x65; 0x65]
            LOAD                ; [0x65; "char"]
            TEST                ; [0x65; "char"]
            PUSH    exit        ; [0x65; "char"; exit]
            JZ                  ; [0x65; "char"]
            OVER                ; [0x65; "char"; 0x65]
            SAVE                ; [0x65]
            PUSH    loop        ; [0x65; loop]
            JUMP                 ; [0x65]

exit:
            HALT
