<?php
include('connect.php');


$id = $_GET['id'];
$name = $_GET['name'];
$role = $_GET['role'];

$sql = "insert into groupsmembers(memberID, groupName, role)
	values(\"$id\", \"$name\", \"$role\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();