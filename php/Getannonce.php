<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 



$query=$_POST['query'];
//$query="SELECT Annonce_immo.idAnnonce,titre,description,typeBien,dureedispo,Adresse,TypeContact,ville,Loyer,surface FROM Annonce_immo INNER JOIN depot_consulte on Annonce_immo.idAnnonce=depot_consulte.idAnnonce WHERE depot_consulte.idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='toto')";
        
$s = $db->prepare($query);
$s->execute();
$res=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("idAnnonce"=>$rows['idAnnonce'],"titre"=>$rows['titre'],"description"=>$rows['description'],"typeBien"=>$rows['typeBien'], "dureedispo"=>$rows['dureedispo'], "Adresse"=>$rows['Adresse'], "TypeContact"=>$rows['TypeContact'], "Loyer"=>$rows['Loyer'], "ville"=>$rows['ville'], "surface"=>$rows['surface'] ));
}

$nbrow=$s->rowCount();

echo json_encode(array("res"=>$res));

//https://terl3recette.000webhostapp.com/Getannonce.php