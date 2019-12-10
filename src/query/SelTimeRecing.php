<?php


include('connect.php');

$gname = $_GET['gname'];
$id = $_GET['id'];

#groupname, role
if($result = $mysqli->query("select * from timerecommending where groupname = \"$gname\" and memberid=\"$id\"")){
	if($result->num_rows){
		for($count = 0; $count < $result->num_rows; $count++){
			$row = mysqli_fetch_array($result);
			printf("%d#%s#%s#%s<br>",$row[2], $row[3], $row[4], $row[5]);
		}
		#$result->free();
	} else{
	echo "결과 없음";
	}
}

$mysqli->close();