<?php
include('connect.php');

$id = $_GET['id'];

$sql = "delete from groupsmembers where memberid = \"$id\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();