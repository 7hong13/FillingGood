<?php
	$host = "localhost";
	$user = "root";
	$pw = "rhd0tka3";
	$dbname = "fillinggood";
	
	$mysqli = new mysqli($host, $user, $pw, $dbname);

	#에러 체크
	if($mysqli->connect_errno){
		printf("Connect failed: $s\n", $mysqli->connect_error);
		exit();
	}
	
	#한글 표기
	$mysqli->query('set names utf8');
?>