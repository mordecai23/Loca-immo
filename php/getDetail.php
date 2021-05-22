<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
$s = $db->prepare($query);
$s->execute();
$ct="";
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {
    $ct=$rows['mail']."&&&&".$rows['numTel']."&&&&".$rows['idUtilisateur'];
}

echo $ct;
