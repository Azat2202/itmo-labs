%define last_element 0

%macro colon 2
    
    %2:
        dq  last_element
        db  %1, 0
    %define last_element %2

%endmacro
