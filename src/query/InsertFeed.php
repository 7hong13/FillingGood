<?php
include('connect.php');


$gname = $_GET['gname'];
$date = $_GET['date'];
$start = $_GET['start'];
$writer = $_GET['writer'];
$feed = $_GET['feed'];

$sql = "insert into schedulefeeds(groupnamefk, datefk, starttimefk, feedmem, feed)
	values(\"$gname\", \"$date\", \"$start\", \"$writer\", \"$feed\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();