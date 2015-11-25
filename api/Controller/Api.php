<?php
/*
Punto de entrada de la API para hacer get. Los parametros que llegaran:

Api.php? user=X & pass=Y & hash=md5(X + Y)

O si ya se dispone de clave de descifrado:

Api.php? request=R & md5(clave + YYMMDD + R) 
*/

  
require_once ("Connection.php");
require_once ("Rest.php");
require_once ("Hash.php");
require_once ("Login.php");
require_once ("ApiCalls.php");

class Api extends Rest {
	private $_conn = NULL;
	private $_metodo;
	private $_argumentos;
	public function __construct() {
		parent::__construct ();
		$this->conectarDB ();
		
	}
	private function conectarDB() {
		try {
			$dbCon = new DbConnection ();
			$this->_conn = $dbCon->getConnection ();
			
		} catch ( PDOException $e ) {
			echo 'Falló la conexión: ' . $e->getMessage ();
			
		}
		
	}
	
	private function devolverError($id) {
		$errores = array (
				array (
						'estado' => "error",
						"msg" => "petición no encontrada" 
				),
				array (
						'estado' => "error",
						"msg" => "petición no aceptada" 
				),
				array (
						'estado' => "error",
						"msg" => "petición sin contenido" 
				),
				array (
						'estado' => "error",
						"msg" => "email o password incorrectos" 
				),
				array (
						'estado' => "error",
						"msg" => "error borrando usuario" 
				),
				array (
						'estado' => "error",
						"msg" => "error actualizando nombre de usuario" 
				),
				array (
						'estado' => "error",
						"msg" => "error buscando usuario por email" 
				),
				array (
						'estado' => "error",
						"msg" => "error creando usuario" 
				),
				array (
						'estado' => "error",
						"msg" => "usuario ya existe" 
				) 
		);
		return $errores [$id];
	}
	
	public function procesarLLamada() {
		
		/* test ok
		$hash = new Hash ( $this->_conn );
        echo $hash->testLogin($_REQUEST ['name'], $_REQUEST ['password'], $_REQUEST ['hash'] );
	    */
		$hash = new Hash ( $this->_conn );
        echo $hash->testRequest($_REQUEST ['name'], $_REQUEST ['request'], $_REQUEST ['hash'] );
		
		$login = new Login ( $this->_conn );
		$login->login();
		
		// si viene con los parametros user y pass significa que se quiere loguear para tener la clave
		if (isset ( $_REQUEST ['user'] ) && isset ( $_REQUEST ['user'] ) && isset ( $_REQUEST ['hash'] ) ) {
	        		
		echo'1';		
			
		}else if ( isset ( $_REQUEST ['user'] ) && isset ( $_REQUEST ['request'] ) && isset ( $_REQUEST ['hash'] ) ) {
			echo '2';
			
		}
		
	}
	
	public function procesarLLamadaOld() {
		if (isset ( $_REQUEST ['url'] )) {
			
			// si por ejemplo pasamos explode('/','////controller///method////args///') el resultado es un array con elem vacios;
			// Array ( [0] => [1] => [2] => [3] => [4] => controller [5] => [6] => [7] => method [8] => [9] => [10] => [11] => args [12] => [13] => [14] => )
			$url = explode ( '/', trim ( $_REQUEST ['url'] ) );
			// con array_filter() filtramos elementos de un array pasando función callback, que es opcional.
			// si no le pasamos función callback, los elementos false o vacios del array serán borrados
			// por lo tanto la entre la anterior función (explode) y esta eliminamos los '/' sobrantes de la URL
			$url = array_filter ( $url );
			$this->_metodo = strtolower ( array_shift ( $url ) );
			$this->_argumentos = $url;
			$func = $this->_metodo;
			
			$apiCalls = new ApiCalls ( $this->_conn );
			
			if (( int ) method_exists ( $apiCalls, $func ) > 0) {
				
				if (count ( $this->_argumentos ) > 0) {
					call_user_func_array ( array (
							$apiCalls,
							$this->_metodo 
					), $this->_argumentos );
				} else { // si no lo llamamos sin argumentos, al metodo del controlador
					call_user_func ( array (
							$apiCalls,
							$this->_metodo 
					) );
				}
			} else
				$this->mostrarRespuesta ( $this->convertirJson ( $this->devolverError ( 0 ) ), 404 );
		}
		$this->mostrarRespuesta ( $this->convertirJson ( $this->devolverError ( 0 ) ), 404 );
	}
	
}
$api = new Api ();
$api->procesarLLamada ();  