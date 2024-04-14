const ACCENT_BACKGROUND = "#00ADB5";
const MAIN_BACKGROUND = "#EEEEEE";

const validator = new Validator();
window.onload = function () {
    const canvasPrinter = new CanvasPrinter();
    canvasPrinter.drawStartImage()
    document.querySelectorAll('input[class="X-button"]').forEach(button => {
        button.addEventListener('click', () => {
            document.querySelectorAll('input[class="X-button"]').forEach(button =>{
                button.style.backgroundColor = MAIN_BACKGROUND
            })
            button.style.backgroundColor = ACCENT_BACKGROUND
            validator.lastClickedX = button.value;
        })
    })

    document.querySelector("input[type=text]")
        .addEventListener("focusout", validator.validateY);

    document.querySelectorAll('input[name="R-radio-group"]').forEach(radio => {
        radio.addEventListener('click', () => {
            validator.lastClickedR = radio.value;
            canvasPrinter.redrawAll(radio.value)
        })
    })

    canvasPrinter.canvas.addEventListener('click', function(event) {
        canvasPrinter.parseClick(event)
    });
    // Not auto update page after form submit
    $('form').submit(function(e) {
        e.preventDefault();
    })
}