<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("contenu"=>$rows['contenu'],"mail"=>$rows['mail']));
}

echo json_encode(array("res"=>$res));

