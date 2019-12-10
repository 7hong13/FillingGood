<?php


include('connect.php');

#groupname, role
if($result = $mysqli->query("select id from groupmember")){
	if($result->num_rows){
		for($count = 0; $count < $result->num_rows; $count++){
			$row = mysqli_fetch_array($result);
			printf("%s<br>", $row[0]);
		}
		#$result->free();
	} else{
	echo "결과 없음";
	}
}

$mysqli->close();