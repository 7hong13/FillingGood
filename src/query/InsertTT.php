<?php
include('connect.php');


$gname = $_GET['gname'];
$day = $_GET['day'];
$time = (int)$_GET['time'];
$value = (double)$_GET['value'];

$sql = "insert into timetable(groupname, day, timeindex, value) values(\"$gname\", \"$day\", $time, $value)";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();