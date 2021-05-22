<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();

//https://terl3recette.000webhostapp.com/inscription.php