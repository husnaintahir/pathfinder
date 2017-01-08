<?php

ini_set('display_startup_errors',1);
ini_set('display_errors',1);
error_reporting(-1);


$DBHOST = "localhost";
$DBUSER = "root";
$DBPASS = "";
$DBNAME = "smart_route_finder";

include_once 'database.class.php';

$db = new Database();
$db->connection($DBHOST,$DBUSER,$DBPASS,$DBNAME,1);


?>