var printer;
// var lastRR = 4;
window.onload = function (){
    printer = new Printer();
    printer.drawStart();
    printer.canvas.addEventListener('click', function(event) {
        printer.clickPoint(event)
    });
    // setInterval(checkUpdate, 10000);
}