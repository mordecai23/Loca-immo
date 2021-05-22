<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("loyer"=>$rows['loyer'],"surface"=>$rows['surface'],"type"=>$rows['type'],"ville"=>$rows['ville']));
}

echo json_encode(array("res"=>$res));
