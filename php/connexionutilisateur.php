<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$util=array();

while ($rows = $s->fetch(PDO::FETCH_LAZY)) {
   
}

$nbrow=$s->rowCount();

echo (string) $nbrow;