<?php

/**
 * Gère les interactions avec la base de données.
 */
class DataBase
{
    
    protected static $config = [
        'hostname' => 'localhost',
        'database' => 'id13478871_androidlocation',
        'user' => 'id13478871_yashveerteeluck',
        'password' => 'C6#C*_/x8%4XaLjJ'
    ];

    //private static PDO $inst=new PDO("mysql:host=".self::$config["hostname"]. ";dbname=" . self::$config["database"], self::$config["user"], self::$config["password"], array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
        

    /**
     * Ouvre une connection avec la base de donnée et renvoie le résultat.
     */
    public static function getInstance()
    {
    
        $inst= new PDO("mysql:host=".self::$config["hostname"]. ";dbname=" . self::$config["database"], self::$config["user"], self::$config["password"], array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
        
        return $inst;
    }


}
