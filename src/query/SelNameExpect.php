<?php


include('connect.php');

$gname = $_GET['gname'];
$id = $_GET['id'];
$rank = (int)$_GET['rank'];

#groupname, role
if($result = $mysqli->query("select * from timerecommending where groupname = \"{$gname}\" and memberid = \"$id\" and recommendrank = $rank")){
	if($result->num_rows){
		for($count = 0; $count < $result->num_rows; $count++){
			$row = mysqli_fetch_array($result);
			printf("%d#%s", 
				$row[8], $row[9]);
		}
		#$result->free();
	} else{
	echo "결과 없음";
	}
} else{
	echo "결과 없음";
}

$mysqli->close();