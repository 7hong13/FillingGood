<?php
include('connect.php');


$gname = $_GET['gname'];
$id = $_GET['id'];
$rank = (int)$_GET['rank'];

$sql = "insert into group(timechoiced)
	values(1)";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();