<?php
include('connect.php');


$gname = $_GET['gname'];
$day = $_GET['day'];
$time = (int)$_GET['time'];
$value = (double)$_GET['value'];

$sql = "update timetable set value = $value
	where groupname = \"$gname\" and day = \"$day\" and timeindex = $time";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "update";
else
	echo "Error!".$mysqli->error;

$mysqli->close();