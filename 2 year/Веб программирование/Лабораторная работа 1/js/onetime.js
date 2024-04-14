window.onload = function () {
    let canvasPrinter = new CanvasPrinter();
    canvasPrinter.redrawAll(document.querySelector("#R-input").value);
    $.ajax({
        type: 'GET',
        url: 'php/main.php',
        async: false,
        success: function (serverAnswer){
            const jsonObject = JSON.parse(JSON.stringify(serverAnswer));
            document.getElementById("outputContainer").innerHTML = jsonObject.html;
            dots = jsonObject.dots;
            jsonObject.dots.forEach((dot) => {
                canvasPrinter.drawPoint(
                    dot.x,
                    dot.y,
                    dot.success
                )
            })
        }
    });

    document.getElementById("R-input")
        .onchange = function () {
            canvasPrinter.redrawAll(document.querySelector("#R-input").value);
            dots.forEach((dot) => {
                canvasPrinter.drawPoint(
                    dot.x,
                    dot.y,
                    dot.success
                )
            })
        };

    // Ограничить количество знаков после запятой

    let button = document.querySelector("input[type=text]");
    button.addEventListener("input", validateY);
    button.addEventListener("focus", validateY);

    checkboxes = document.querySelectorAll('input[type=checkbox]')
    checkboxes.forEach((checkbox) => {
        checkbox.addEventListener("click", () => {
            checkboxes.forEach((c) => {
                if (c !== checkbox) {
                    c.checked = false;
                }
            });
        });
    });

    document.getElementById('checkButton').onclick = function (){
        if (validateX() && validateY()) {
            let x = document.querySelector('input[type="checkbox"]:checked').value;
            let y = document.getElementById("Y-input").value.replace(',', '.');
            let r = document.getElementById('R-input').value
            $.ajax({
                type: 'POST',
                url: 'php/main.php',
                async: false,
                data: { "x": x, "y": y, "r": r},
                success: function (serverAnswer){
                    const jsonObject = JSON.parse(JSON.stringify(serverAnswer));
                    document.getElementById("outputContainer").innerHTML = jsonObject.html;
                    dots.push({
                        'x': jsonObject.x,
                        'y': jsonObject.y,
                        'success': jsonObject.success
                    });
                    canvasPrinter.drawPoint(
                        jsonObject.x,
                        jsonObject.y,
                        jsonObject.success
                    );
                }
            });
        }
    }

    document.getElementById('clearButton').onclick = function (){
        $.ajax({
            type: 'POST',
            url: 'php/main.php',
            data: {'delete': 'all'},
            success: function (serverAnswer){
                const jsonObject = JSON.parse(JSON.stringify(serverAnswer));
                document.getElementById("outputContainer").innerHTML = jsonObject.html;
                dots = jsonObject.dots;
                canvasPrinter.redrawAll(document.querySelector("#R-input").value);
            }
        });
    }
};