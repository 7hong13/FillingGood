<?php
include('connect.php');


$name = $_GET['name'];
$start = $_GET['start'];
$end = $_GET['end'];


$sql = "update groupinfo set startscheduleperiod = \"$start\", endscheduleperiod = \"$end\"
	where name = \"$name\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "update";
else
	echo "Error!".$mysqli->error;

$mysqli->close();