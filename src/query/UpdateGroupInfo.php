<?php
include('connect.php');


$name = $_GET['name'];
$desc = $_GET['description'];


$sql = "update groupinfo set description = \"$desc\"
	where name = \"$name\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "update";
else
	echo "Error!".$mysqli->error;

$mysqli->close();