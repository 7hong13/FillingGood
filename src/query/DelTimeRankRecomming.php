<?php
include('connect.php');

$name = $_GET['name'];
$id = $_GET['id'];
$rank = (int)$_GET['rank'];

$sql = "delete from timerecommending where groupname = \"$name\" and memberid = \"$id\" and recommendrank = $rank";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();