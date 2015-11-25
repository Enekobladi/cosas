<?php
// Gestion de la BBDD
// Se utiliza ORM de serie de PHP llamado PDO
class DbConnection {
	const db_server = "localhost";
	const db_user = "api";
	const db_pwd = "1234";
	const db_name = "api";
	private $_conn = NULL;
	private $connection = NULL;
	public function __construct() {
		// parent::__construct();
		$this->_conectarDB ();
	}
	private function _conectarDB() {
		$dsn = 'mysql:dbname=' . self::db_name . ';host=' . self::db_server;
		
		try {
			$this->_conn = new PDO ( $dsn, self::db_user, self::db_pwd );
			
			$this->_conn->query ( "SELECT id, name, password FROM user" );
		} catch ( PDOException $e ) {
			echo 'Falló la conexión: ' . $e->getMessage ();
		} catch ( Exception $e2 ) {
			echo 'Error desconocido';
		}
	}
	public function getConnection() {
		return $this->_conn;
	}
}

?>
