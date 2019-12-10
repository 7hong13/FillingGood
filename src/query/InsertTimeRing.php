<?php
include('connect.php');


$gname = $_GET['gname'];
$id = $_GET['id'];
$rank = (int)$_GET['rank'];
$name = $_GET['name'];
$date = $_GET['date'];
$start = $_GET['start'];
$end = $_GET['end'];
$time = $_GET['time'];


$sql = "insert into Timerecommending(groupname, memberid, name, recommendrank, recommenddate, recommendstarttime, recommendendtime, expectTime)
	values(\"$gname\", \"$id\",\"$name\", $rank, $date, $start, $end, $time)";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();