<?php
include('connect.php');


$gname = $_GET['gname'];
$id = $_GET['id'];
$rank = (int)$_GET['rank'];

$sql = "update recommending timechoiced = 1
	where groupname = $gname and memberid = $id and recommendrank = $rank";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "update";
else
	echo "Error!".$mysqli->error;

$mysqli->close();