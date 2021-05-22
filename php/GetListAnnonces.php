<?php
require_once "connexion.php";
$db = DataBase::getInstance(); 


$query=$_POST['query'];
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
        array_push($tab,array("id"=>$id[$i],$res[$i]));
    }
}
echo json_encode(array("res"=>$tab));

