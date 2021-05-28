<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 



$query=$_POST['query'];
//$query="SELECT lien FROM image WHERE idAnnonce='11516561' ";
        
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("lien"=>$rows['lien']));
}


echo json_encode(array("res"=>$res));

//https://terl3recette.000webhostapp.com/Getimage.php