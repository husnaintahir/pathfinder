<?php
include_once "config.php";

if(isset($_POST["username"]) && isset($_POST["password"])){
	$isUserExist = $db->fetchRow("*","drivers_info"," username = '".$_POST["username"]."' AND password = '".$_POST["password"]."'");
	if($isUserExist != ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$isUserExist));
	}else{
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

