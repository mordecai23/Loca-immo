<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("nom"=>$rows['nom'],"nb"=>$rows['nb']));
}

echo json_encode(array("res"=>$res));

//https://terl3recette.000webhostapp.com/GetGlobalStats.php