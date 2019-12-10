<?php
include('connect.php');

$id = $_GET['id'];
$date = $_GET['date'];
$start = $_GET['start'];

$sql = "delete from personalschedule where memberid = \"$id\" and date = $date and starttime = $start";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();