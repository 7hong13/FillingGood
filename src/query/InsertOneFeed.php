<?php
include('connect.php');


$name = $_GET['name'];
$date = $_GET['date'];
$start = $_GET['start'];
$feedmem = $_GET['feedmem'];
$feed = $_GET['feed'];

$sql = "insert into schedulefeeds(groupnamefk, datefk, starttimefk, feedmem, feed)
	values(\"$name\", \"$date\", \"$start\", \"$feedmem\", \"$feed\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();