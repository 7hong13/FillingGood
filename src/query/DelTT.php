<?php
include('connect.php');

$gname = $_GET['gname'];
$day = $_GET['day'];
$time = $_GET['time'];

$sql = "delete from timetable where groupname = \"$gname\", day = \"$day\", time = \"$time\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();