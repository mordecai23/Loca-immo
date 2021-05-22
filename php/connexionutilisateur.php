<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();

while ($rows = $s->fetch(PDO::FETCH_LAZY)) {
   
}

$nbrow=$s->rowCount();

echo (String)$nbrow;

//https://terl3recette.000webhostapp.com/connexionutilisateur.php