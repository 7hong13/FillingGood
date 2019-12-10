<?php


include('connect.php');

$gname = $_GET['gname'];

#groupname, role
if($result = $mysqli->query("select * from timetable where groupname = \"{$gname}\"")){
	if($result->num_rows){
		for($count = 0; $count < $result->num_rows; $count++){
			$row = mysqli_fetch_array($result);
			printf("%s#%d#%lf<br>", 
				$row[1], $row[2], $row[3]);
		}
		#$result->free();
	} else{
	echo "결과 없음";
	}
} else{
	echo "결과 없음";
}

$mysqli->close();