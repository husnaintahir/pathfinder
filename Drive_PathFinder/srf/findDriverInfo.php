<?php
include_once "config.php";
// echo "select * from drivers_info where username='".$_POST["username"]."'";
// exit;
 if(isset($_REQUEST["findDriverInfo"]) && isset($_REQUEST["username"])){
	$data = $db->fetchRow("*","drivers_info"," username = '".$_REQUEST["username"]."'");
	if($data != ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$data));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0"));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"-1","data"=>null));
}


// msg => "1" ---- user found
// msg => "0" ---- user not found
// msg => "-1" ---- params missing

// result => "1" ---- query placed
// result => "0" ---- query not placed
?>

