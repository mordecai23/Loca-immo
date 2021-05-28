<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


//$query=$_POST['query'];
$query="SELECT u.ville, COUNT(*) AS nb, a.idAnnonce FROM depot_consulte d INNER JOIN Annonce_immo a ON d.idAnnonce=a.idAnnonce Inner JOIN utilisateur u on d.idutilisateur=u.idUtilisateur WHERE a.idAnnonce IN (SELECT idAnnonce FROM depot_consulte d1 INNER JOIN utilisateur u on d1.idutilisateur=u.idUtilisateur WHERE u.mail='toto' AND d1.type=1) AND d.type=2 GROUP BY u.ville,a.idAnnonce ";
$s = $db->prepare($query);
$s->execute();
$id=array();
$res=array();
$resfinal=array();
$tab=array();
while ($rows = $s->fetch(PDO::FETCH_LAZY)) {

    array_push($res, array("ville"=>$rows['ville'],"nb"=>$rows['nb']));
    array_push($id,$rows["idAnnonce"]);
}
for( $i=0;$i<sizeof($id);$i++)
{
if (array_key_exists($id[$i],$tab))
    {    
    array_push($tab[$id[$i]],$res[$i]);
    }
    else{
        array_push($tab,array($id[$i]=>array($res[$i])));
    }
}
echo json_encode(array("res"=>$tab));

