function validateX(){
    checkboxes = document.querySelectorAll('input[type=checkbox]')
    for (let checkbox of checkboxes){
        if(checkbox.checked){
            return true;
        }
    }
    checkboxes[4].setCustomValidity("Please choose box");
    checkboxes[4].reportValidity();
    return false;
}

function validateY() {
    element = document.getElementById("Y-input");
    y = element.value.replace(',', '.');
    if (!isNumeric(y) || parseFloat(y) > 5 || parseFloat(y) < -5) {
        element.setCustomValidity("Please enter an integer between -5 and 5");
        element.reportValidity();
        return false;
    } else {
        element.setCustomValidity("");
        element.reportValidity();
        return true;
    }

}

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
