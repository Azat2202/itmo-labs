function parseForm(){
    if(!validator.validateAll()) {
        swal.fire(
            'Неверно заполнена форма!',
            'Проверьте что все поля заполнены и Y в требуемых пределах',
            'warning'
        )
        return
    }
    sendPoint(
        validator.lastClickedX,
        document.getElementById("Y-input").value.replace(',', '.'),
        validator.lastClickedR
    )
    location.reload()
}

function sendPoint(x, y, r){
    $.ajax({
        type: "POST",
        url: "controller-servlet",
        dataType: "json",
        async: false,
        data: {
            "X": x,
            "Y": y,
            "R": r,
            "timezone": - new Date().getTimezoneOffset()
        }
    });
}