<?php
include('connect.php');


$name = $_GET['name'];
$desc = $_GET['description'];
$start = $_GET['start'];
$end = $_GET['end'];

$sql = "insert into group(name, description, startscheduleperiod, endscheduleperiod)
	values(\"$name\", \"$desc\", \"$start\", \"$end\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();