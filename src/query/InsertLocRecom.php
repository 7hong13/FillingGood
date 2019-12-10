<?php
include('connect.php');


$gname = $_GET['gname'];
$id = $_GET['id'];
$rank = $_GET['rank'];
$loc = $_GET['loc'];

$sql = "insert into locrecommending(groupname, memberid, recommendrank, recommendlocation)
	values(\"$gname\", \"$id\", \"$rank\", \"$loc\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();