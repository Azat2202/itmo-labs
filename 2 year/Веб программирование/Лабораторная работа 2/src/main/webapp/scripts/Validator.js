class Validator{
    lastClickedX = null;
    lastClickedR = null;
    validateAll(){
        return this.validateX() && this.validateY() && this.validateR();
    }

    validateX(){
        return this.lastClickedX != null;
    }

    validateY(){
        let element = document.getElementById("Y-input");
        let value = element.value.replace(',', '.');
        function isNumeric(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        }
        if (!isNumeric(value) || parseFloat(value) > 3 || parseFloat(value) < -3) {
            Swal.fire({
                title: 'Невалидный ввод!',
                text: 'Введите число от -3 до 3',
                icon: 'warning'
            });
            return false;
        }
        return true;
    }

    validateR(){
        return this.lastClickedR != null;
    }
}