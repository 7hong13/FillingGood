<?php
include('connect.php');

$gname = $_GET['gname'];
$date = $_GET['date'];
$start = $_GET['start']

$sql = "delete from schedulefeeds where groupnamefk = \"$gname\", datefk = \"$date\", starttimefk = \"$start\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();