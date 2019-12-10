<?php
include('connect.php');


$gname = $_GET['gname'];
$date = $_GET['date'];
$start = $_GET['start'];
$end = $_GET['end'];
$name = $_GET['name'];
$desc = $_GET['descrip'];
$loc = $_GET['loc'];
$trank = $_GET['trank'];
$lrank = $_GET['lrank'];

$col = "groupname, date, starttime, endtime, name, description, location";
$val = "\"$gname\", \"$date\", \"$start\", \"$end\", \"$name\", \"$desc\", \"$loc\"";

if($trank){
	$col =  $col.", choicedtimerank";
	$val =  $val.", \"$trank\"";
}
if($lrank){
	$col = $col.", choicedlocationrank";
	$val = $val.", \"$lrank\"";
}
$sql = "insert into groupschedule($col)
	values($val)";

#쿼리 결과 확인
if($mysqli->query($sql) == TRUE)
	echo "insert";
else
	echo "Error!".$mysqli->error;

$mysqli->close();