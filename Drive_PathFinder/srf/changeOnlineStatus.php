<?php
include_once "config.php";

if(isset($_POST["did"]) && isset($_POST["status"]) && isset($_POST["latlng"])){
	$updateQuery = "UPDATE drivers_info SET location = '".$_POST["latlng"]."', isOnline = '".$_POST["status"]."' WHERE DID = '".$_POST["did"]."'";
	if($db->query($updateQuery) == ""){
		echo json_encode(array("result"=>"1","msg"=>"1"));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0"));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"-1"));
}


// msg => "1" ---- success
// msg => "0" ---- fail
// msg => "-1" ---- params missing

// result => "1" ---- query placed
// result => "0" ---- query not placed
?>

