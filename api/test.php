<?php
echo "pre si";

require_once ("db_controller/db_connection.php");

echo "si2";

$api = new DbConnection ();

echo "si";

echo $api->getConnection ();

echo "no funciona";

?>