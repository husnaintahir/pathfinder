<?php
include_once "config.php";

if(isset($_POST["username"]) && isset($_POST["fullname"]) && isset($_POST["password"]) && isset($_POST["vehicle_name"]) && isset($_POST["vehicle_number"]) && isset($_POST["phone"]) && isset($_POST["address"])){
	$isUserExist = $db->fetchRow("*","drivers_info"," username = '".$_POST["username"]."'");
	if($isUserExist == ""){
		$query = "INSERT INTO drivers_info(username, full_name, password, vehicle_num, vehicle_name, phone_num, address) VALUES('".$_POST["username"]."','".$_POST["fullname"]."','".$_POST["password"]."','".$_POST["vehicle_number"]."','".$_POST["vehicle_name"]."','".$_POST["phone"]."','".$_POST["address"]."')";
		$result = $db->query($query);
		if($result==""){
			$userInfo = $db->fetchRow("*","drivers_info"," username = '".$_POST["username"]."' AND password = '".$_POST["password"]."'");
			echo json_encode(array("result"=>"1","msg"=>"1","data"=>$userInfo));
		}
	} else {
		echo json_encode(array("result"=>"0","msg"=>"User Already Exists"));
	}
} else {
	echo json_encode(array("result"=>"0","msg"=>"Params Missing"));
}

?>