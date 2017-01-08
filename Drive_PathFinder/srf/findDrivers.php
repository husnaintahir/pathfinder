<?php
include_once "config.php";

if(isset($_POST["findAllDrivers"])){
	$data = $db->fetchAllRows("SELECT * FROM drivers_info WHERE isOnline = 1");
	if($data != ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$data));
	} else{
		echo json_encode(array("result"=>"1","msg"=>"0"));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"-1"));
}


// msg => "1" ---- user found
// msg => "0" ---- user not found
// msg => "-1" ---- params missing

// result => "1" ---- query placed
// result => "0" ---- query not placed
?>

