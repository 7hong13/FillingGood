<?php
include('connect.php');


$id = $_GET['id'];
$date = $_GET['date'];
$start = $_GET['start'];
$end = $_GET['end'];
$name = $_GET['name'];
$desc = $_GET['descrip'];
$loc = $_GET['loc'];
$prior = $_GET['prior'];

$sql = "insert into personalschedule(memberID, date, starttime, endtime, name, description, location, priority)
	values(\"$id\", \"$date\", \"$start\", \"$end\", \"$name\", \"$desc\", \"$loc\", \"$prior\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();