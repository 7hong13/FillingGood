<?php
include('connect.php');


$gname = $_GET['gname'];
$id = $_GET['id'];
$name = $_GET['name'];
$time = (int)$_GET['time'];
$rank = (int)$_GET['rank'];

$sql = "insert into timerecommending(groupname, memberid, name, recommendrank, expectTime)
	values(\"$gname\", \"$id\",\"$name\", $rank, $time)";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();