<?php
include('connect.php');

$name = $_GET['name'];
$date = $_GET['date'];
$start = $_GET['start'];
$feedmem = $_GET['feedmem'];

$sql = "delete from schedulefeeds where groupnamefk = \"$name\" and datefk = $date and starttimefk = $start and feedmem = \"$feedmem\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();