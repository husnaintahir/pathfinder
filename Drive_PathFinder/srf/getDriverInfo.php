<?php
include_once 'config.php';

if(isset($_POST["findDriverInfo"]) && isset($_POST["username"])){
	$userInfo = $db->fetchRow("*", "drivers_info", " username = '".$_POST["username"]."'");
	if($userInfo != ""){
		echo json_encode(array("result"=>"1","msg"=>"1","data"=>$userInfo));
	} else {
		echo json_encode(array("result"=>"1","msg"=>"0"));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"-1","data"=>null));
}

?>