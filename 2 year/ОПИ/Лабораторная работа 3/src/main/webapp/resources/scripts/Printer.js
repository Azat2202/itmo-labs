class Printer{
    SIZE = 408;
    LINE_WIDTH = 2;
    TEXT_LINE_HEIGHT = 3;
    constructor() {
        this.canvas = document.getElementById("graph");
        this.ctx = this.canvas.getContext("2d");
        this.lastR = 2;
        // if (document.getElementById("rInput")) {
        //     this.lastR = document.getElementById("rInput").value;//null;
        // }
        console.log(this.lastR);
    }

    setLastR(r) {
        this.lastR = r;
        updateGraph();
    }

    drawStart() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.drawGraph(this.lastR); //lastRR);//
        this.drawAxes();
        this.setPointerAtDot(0.5);
        this.setPointerAtDot(1);
        this.setPointerAtDot(1.5);
        this.setPointerAtDot(2);
        this.setPointerAtDot(2.5);
        this.setPointerAtDot(3);
        this.setPointerAtDot(3.5);
        this.setPointerAtDot(4);
        this.setPointerAtDot(4.5);
        this.setPointerAtDot(5);
        this.drawPoints(this.lastR); //lastRR);//

    }


    redraw(r){
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.drawGraph(r);
        this.drawAxes();
        this.setPointerAtDot(0.5);
        this.setPointerAtDot(1);
        this.setPointerAtDot(1.5);
        this.setPointerAtDot(2);
        this.setPointerAtDot(2.5);
        this.setPointerAtDot(3);
        this.setPointerAtDot(3.5);
        this.setPointerAtDot(4);
        this.setPointerAtDot(4.5);
        this.setPointerAtDot(5);
        this.drawPoints(r);
    }

    drawAxes() {
        this.ctx.fillStyle = "black";
        this.ctx.strokeStyle = "black";
        this.drawArrow(this.TEXT_LINE_HEIGHT * 5, this.SIZE / 2, this.SIZE - this.TEXT_LINE_HEIGHT  * 5, this.SIZE / 2);
        this.drawArrow( this.SIZE / 2, this.SIZE - this.TEXT_LINE_HEIGHT * 5, this.SIZE / 2, this.TEXT_LINE_HEIGHT * 5);

        this.ctx.textAlign = "center";
        this.ctx.textBaseline = "middle";
        this.ctx.fillText(`X`, this.SIZE - this.TEXT_LINE_HEIGHT * 5, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 5);
        this.ctx.fillText(`Y`, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 5, this.TEXT_LINE_HEIGHT * 5);
    }


    setPointerAtDot(max_r = 3) {
        const points = 12; // 12 8
        const pixels = this.SIZE / points;
        this.ctx.textAlign = "center";
        this.ctx.textBaseline = "middle";
        this.ctx.strokeStyle = "black";
        if (max_r != 1.5 && max_r != 2.5 && max_r != 3.5 && max_r != 4.5 && max_r != 0.5) {
            this.ctx.beginPath()
            this.ctx.lineWidth = this.LINE_WIDTH / 2.3;
            this.ctx.moveTo(this.SIZE / 2 + pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT);
            this.ctx.lineTo(this.SIZE / 2 + pixels * max_r, this.SIZE / 2 - this.TEXT_LINE_HEIGHT);
            this.ctx.moveTo(this.SIZE / 2 + this.TEXT_LINE_HEIGHT, this.SIZE / 2 - pixels * max_r);
            this.ctx.lineTo(this.SIZE / 2 - this.TEXT_LINE_HEIGHT, this.SIZE / 2 - pixels * max_r);
            this.ctx.moveTo(this.SIZE / 2 - pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT);
            this.ctx.lineTo(this.SIZE / 2 - pixels * max_r, this.SIZE / 2 - this.TEXT_LINE_HEIGHT);
            this.ctx.moveTo(this.SIZE / 2 + this.TEXT_LINE_HEIGHT, this.SIZE / 2 + pixels * max_r);
            this.ctx.lineTo(this.SIZE / 2 - this.TEXT_LINE_HEIGHT, this.SIZE / 2 + pixels * max_r);
            this.ctx.stroke();
            this.ctx.fillText(max_r.toString(), this.SIZE / 2 + pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 5);
            this.ctx.fillText(max_r.toString(), this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 5, this.SIZE / 2 - pixels * max_r);
            this.ctx.fillText((max_r * (-1)).toString(), this.SIZE / 2 - pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 5);
            this.ctx.fillText((max_r * (-1)).toString(), this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 5, this.SIZE / 2 + pixels * max_r);
        } else {
            this.ctx.beginPath()
            this.ctx.lineWidth = this.LINE_WIDTH / 2.3;
            this.ctx.moveTo(this.SIZE / 2 + pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 2 / 3);
            this.ctx.lineTo(this.SIZE / 2 + pixels * max_r, this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 2 / 3);
            this.ctx.moveTo(this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 2 / 3, this.SIZE / 2 - pixels * max_r);
            this.ctx.lineTo(this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 2 / 3, this.SIZE / 2 - pixels * max_r);
            this.ctx.moveTo(this.SIZE / 2 - pixels * max_r, this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 2 / 3);
            this.ctx.lineTo(this.SIZE / 2 - pixels * max_r, this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 2 / 3);
            this.ctx.moveTo(this.SIZE / 2 + this.TEXT_LINE_HEIGHT * 2 / 3, this.SIZE / 2 + pixels * max_r);
            this.ctx.lineTo(this.SIZE / 2 - this.TEXT_LINE_HEIGHT * 2 / 3, this.SIZE / 2 + pixels * max_r);
            this.ctx.stroke();
        }
    }

    drawArrow(fromx, fromy, tox, toy) {
        var headlen = 10; // length of head in pixels 10
        var dx = tox - fromx;
        var dy = toy - fromy;
        var angle = Math.atan2(dy, dx);
        this.ctx.strokeStyle = "black";
        this.ctx.beginPath();
        this.ctx.lineWidth = this.LINE_WIDTH;
        this.ctx.moveTo(fromx, fromy);
        this.ctx.lineTo(tox, toy);
        this.ctx.lineTo(tox - headlen * Math.cos(angle - Math.PI / 6), toy - headlen * Math.sin(angle - Math.PI / 6));
        this.ctx.moveTo(tox, toy);
        this.ctx.lineTo(tox - headlen * Math.cos(angle + Math.PI / 6), toy - headlen * Math.sin(angle + Math.PI / 6));
        this.ctx.stroke();
    }

    drawGraph(r){
        if (r != null) {
            this.lastR = r;
            // lastRR = r;
            const points = 12; // 12
            const pixels = this.SIZE / points;
            this.ctx.fillStyle = '#dfd3e3';
            this.ctx.strokeStyle = "#dfd3e3";
            this.ctx.beginPath();
            this.ctx.moveTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2 - r * pixels / 2);
            this.ctx.lineTo(this.SIZE / 2 + r * pixels, this.SIZE / 2 - r * pixels / 2);
            this.ctx.lineTo(this.SIZE / 2 + r * pixels, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.fill();

            this.ctx.beginPath();
            this.ctx.moveTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2 - r * pixels, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2 - r * pixels);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.fill();


            this.ctx.beginPath();
            this.ctx.arc(
                this.SIZE / 2,
                this.SIZE / 2,
                r * pixels / 2,
                Math.PI * 2,
                Math.PI / 2,
            );
            this.ctx.moveTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2 + r * pixels / 2);
            this.ctx.lineTo(this.SIZE / 2 + r * pixels / 2, this.SIZE / 2);
            this.ctx.lineTo(this.SIZE / 2, this.SIZE / 2);
            this.ctx.fill();
        }
    }

    drawPoint(x, y, r, success, r1) {
        // let current_r = coords.selectedR;
        // if (current_r != null) {
        //     x *= current_r / r;
        //     y *= current_r / r;
        // }
        // let r1 = this.lastR;
        if (r1 != null) {
            x *= r1 / r;
            y *= r1 / r;
        }
        // this.ctx.fillStyle = "darkslategrey";
        this.ctx.fillStyle = success
            ? "#36f57f"
            : "#ff2400";
        const points = 12; // 12
        const pixels = this.SIZE / points;
        this.ctx.beginPath();
        this.ctx.arc(
            this.SIZE / 2 + pixels * x,
            this.SIZE / 2 - y * pixels,
            3,
            0,
            Math.PI * 2,
        );
        this.ctx.fill();
        this.ctx.stroke();
        this.ctx.beginPath();
        this.ctx.strokeStyle = success
            ? "#227442"
            : "#990000";
        this.ctx.lineWidth = 1.5;
        this.ctx.arc(
            this.SIZE / 2 + pixels * x,
            this.SIZE / 2 - y * pixels,
            3,
            0,
            Math.PI * 2
        );
        this.ctx.stroke();
    }

    drawPoints(r1) {
        var dots = [];
        $('#outputTable tr').each(function () {
            let row = $(this);
            dots.push({
                "x": parseFloat(row.find("td:eq(0)").text()),
                "y": parseFloat(row.find("td:eq(1)").text()),
                "r": parseFloat(row.find("td:eq(2)").text()),
                "success": row.find("td:eq(3)").text() == "true",
                "currentTime": row.find("td:eq(4)").text(),
                "execTime": row.find("td:eq(5)").text()
            })
        });
        dots.shift();
        dots.forEach(dot => {
            this.drawPoint(dot.x, dot.y, dot.r, dot.success, r1)
        })
    }

    clickPoint(event) {
        const xPixels = event.offsetX * this.canvas.width / this.canvas.clientWidth | 0;
        const yPixels = event.offsetY * this.canvas.height / this.canvas.clientHeight | 0;
        const points = 12;
        const pixels = this.SIZE / points;
        const x = - (this.SIZE / 2 - xPixels) / pixels;
        const y = (this.SIZE / 2 - yPixels) / pixels;

        if(!this.lastR) {
            // var setError = document.getElementById("errorRadio");
            // setError.setCustomValidity("Please choose R");
            // setError.reportValidity();
            return;
        }

        if(x >= 4 || x <= -4 || y >= 5 || y <= -5) {
            // var setError = document.getElementById("checkButton");
            // setError.setCustomValidity("Selected point is out of area:\nX: [-5; 3]\nY: (-5; 5)");
            // setError.reportValidity();
            return;
        }

        addAttempt(
            [
                { name: "x", value: (Math.round(x * 1000) / 1000).toString() },
                { name: "y", value: (Math.round(y * 1000) / 1000).toString() },
                { name: "r", value: this.lastR.toString() }
            ]
        )
        updateGraph();

        // sendReq(x.toFixed(3), y.toFixed(3), this.lastR);
        // location.reload();
    }
}