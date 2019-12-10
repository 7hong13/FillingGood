<?php
include('connect.php');

#http://localhost/query/InsertMember.php?id=아이디값&name=이름값&age=나이값&major=전공값
$id = $_GET['id'];
$name = $_GET['name'];
$age = (int)$_GET['age'];
$major = $_GET['major'];

#인서트 쿼리
$sql = "insert into groupmember(ID, Name, Age, Major)
	values(\"$id\", \"$name\", $age, \"$major\")";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();