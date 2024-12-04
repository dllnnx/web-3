let canvasPainter;
window.onload = function (){
    canvasPainter = new CanvasPainter();
    canvasPainter.redrawAll(document.getElementById("formId:rInput").value);
    canvasPainter.canvas.addEventListener('click', function(event) {
        canvasPainter.parseClick(event)
    });
}