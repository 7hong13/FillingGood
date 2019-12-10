<?php
include('connect.php');

$name = $_GET['name'];

$sql = "delete from recommending where groupname = \"$name\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();