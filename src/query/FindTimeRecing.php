<?php


include('connect.php');

$gname = $_GET['gname'];

#groupname, role
if($result = $mysqli->query("select * from Timerecommending where groupname = \"{$gname}\"")){
	if($result->num_rows){
		echo "결과 있음";
	} else{
		echo "결과 없음";
	}
} else{
	echo "결과 없음";
}

$mysqli->close();