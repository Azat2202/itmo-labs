<?php

use php\Checker;

header('Access-Control-Allow-Origin: *');
date_default_timezone_set('Europe/Moscow');

require __DIR__ . "/Checker.php";

@session_start();

const table_head = "<table border='0' id='outputTable'>
        <tr>
            <th>x</th>
            <th>y</th>
            <th>r</th>
            <th>Статус</th>
            <th>Текущее время</th>
            <th>Время работы скрипта</th>
        </tr>";
const table_tail = '</table>';

if (!isset($_SESSION["shots"])) {
    $_SESSION["shots"] = array();
}

$current_time = date("H:i:s");
$start_time = microtime(true);

if ($_SERVER["REQUEST_METHOD"] == "GET") {
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode(array(
        "html" => createTable(),
        'dots' => $_SESSION["shots"]
    ));
    exit();
}

if (isset($_POST["delete"])) {
    if ($_POST["delete"] == 'all') {
        $_SESSION["shots"] = array();
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(array(
            "html" => createTable(),
            'dots' => $_SESSION["shots"]
        ));
        exit();
    }
}

if (isset($_POST["x"]) && isset($_POST["y"]) && isset($_POST["r"])) {
    $x = intval($_POST["x"]);
    $y = floatval($_POST["y"]);
    $r = intval($_POST["r"]);
    $checker = new Checker();
    if ($checker -> validate($x, $y, $r)){
        $cord_status = $checker -> check($x, $y, $r);
        $elapsed_time = round((microtime(true) - $start_time) * 1000000, 2);
        $new_result = array(
            "x" => $x,
            "y" => $y,
            "r" => $r,
            "success" => $cord_status,
            "currentTime" => $current_time,
            "benchmarkTime" => $elapsed_time
        );
        $_SESSION["shots"][] = $new_result;
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(array(
            "html" => createTable(),
            "x" => $x,
            "y" => $y,
            "success" => $checker -> check($x, $y, $r)
        ));
        exit();
    } else{
        header('Content-Type: application/json; charset=utf-8');
        http_response_code(400);
        echo json_encode(array(
            "html" => createTable()
        ));
        exit();
    }
}

function createTable(){
    $out = "";
    $out = $out . table_head;
    if(sizeof($_SESSION["shots"]) > 0) {
        foreach (array_reverse($_SESSION["shots"]) as $table_row) {
            $out = $out . "<tr>";
            $out = $out . "<td>" . $table_row["x"] . "</td>";
            $out = $out . "<td>" . $table_row["y"] . "</td>";
            $out = $out . "<td>" . $table_row["r"] . "</td>";
            $out = $out . "<td>" .
                ($table_row["success"]
                    ? "<span class='success'>попадание</span>"
                    : "<span class='fail'>промах</span>")
                . "</td>";
            $out = $out . "<td>" . $table_row["currentTime"] . "</td>";
            $out = $out . "<td>" . $table_row["benchmarkTime"] . "</td>";
            $out = $out . "</tr>\r\n";
        }
    }
    return $out . table_tail;
}