<?php
include('connect.php');


$name = $_GET['name'];
$desc = $_GET['description'];


$sql = "insert into groupinfo(name, description)
	values(\"$name\", \"$desc\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();