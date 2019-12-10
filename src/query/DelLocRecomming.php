<?php
include('connect.php');

$gname = $_GET['gname'];

$sql = "delete from Locrecommending where groupname = \"$gname\" ";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "delete";
else
	echo "Error!".$mysqli->error;

$mysqli->close();