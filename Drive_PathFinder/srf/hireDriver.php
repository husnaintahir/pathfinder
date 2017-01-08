<?php
include_once "config.php";

/////////////// 
 if(isset($_REQUEST["driverName"]) && isset($_REQUEST["userHiring"])){
	$query = $db->query("UPDATE drivers_info SET isHired = '1' WHERE username = '".$_POST["driverName"]."'");
	if($query == ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$query));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0","data"=>$query));
	}
} else if(isset($_REQUEST["driverName"]) && isset($_REQUEST["userUnHiring"])){
	$query = $db->query("UPDATE drivers_info SET isHired = '0' WHERE username = '".$_POST["driverName"]."'");
	if($query == ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$query));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0","data"=>$query));
	}
} else if(isset($_REQUEST["driverName"]) && isset($_REQUEST["driverAccepting"])){
	$query = $db->query("UPDATE drivers_info SET isHired = '2' WHERE username = '".$_POST["driverName"]."'");
	if($query == ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$query));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0","data"=>$query));
	}
} else if(isset($_REQUEST["driverName"]) && isset($_REQUEST["findJob"])){
	$data = $db->fetchRow("*","drivers_info"," username = '".$_POST["driverName"]."' AND isHired = '1'");
	if($data != ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$data));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0","data"=>null));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"-1","data"=>null));
}


// msg => "1" ---- query placed/ job found
// msg => "0" ---- error/no job yet
// msg => "-1" ---- params missing

// result => "1" ---- query placed
// result => "0" ---- query not placed

// isHire = 0 ------------- notHired/job done
// isHire = 1 ------------- hired from user side
// isHire = 2 ------------- driver accepted offer
?>

