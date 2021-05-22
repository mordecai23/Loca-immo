<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$ct=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {
    array_push($ct, $rows['TypeContact']);
}

echo $ct[0];
