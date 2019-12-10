<?php
include('connect.php');

#http://localhost/query/insert.php?id=아이디값&name=이름값&age=나이값&major=전공값
$id = $_GET['id'];
$name = $_GET['name'];
$age = (int)$_GET['age'];
$major = $_GET['major'];

#인서트 쿼리
$sql = "update groupmember set name = \"$name\", age = \"$age\", major = \"$major\"
	where id = \"$id\"";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "update";
else
	echo "Error!".$mysqli->error;

$mysqli->close();