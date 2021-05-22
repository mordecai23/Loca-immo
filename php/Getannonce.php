<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("idAnnonce"=>$rows['idAnnonce'],"titre"=>$rows['titre'],"description"=>$rows['description'],"typeBien"=>$rows['typeBien']));
}

$nbrow=$s->rowCount();

echo json_encode(array("res"=>$res));

