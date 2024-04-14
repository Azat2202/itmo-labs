var canvasPrinter;
window.onload = function (){
    canvasPrinter = new CanvasPrinter();
    canvasPrinter.drawStartImage();
    canvasPrinter.canvas.addEventListener('click', function(event) {
        canvasPrinter.parseClick(event)
    });
    setInterval(checkUpdate, 10000);
}