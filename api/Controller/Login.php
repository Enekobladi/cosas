<?php
/*

Punto de entrada de la API para hacer get. Los parametros que llegaran:

Api.php? user=X & pass=Y & hash=md5(X + Y)

O si ya se dispone de clave de descifrado:

Api.php? user=X & request=R & hash=md5(clave + YYMMDD + R)
  
*/

require_once ("Connection.php");
require_once ("Rest.php");
require_once ("ApiCalls.php");
class Login extends Rest {
	private $_conn = NULL;
	public function __construct($_conn) {
		$this->_conn = $_conn;
	}
	
	// 
	// Comprobar que tiene password y a generado el hash correctamente
	// 
	//
	private function isLogged() {
	   	
	}
	
	public function login() {
		$name = ( $_REQUEST ['name'] );
		$pwd =  ( $_REQUEST ['password'] );
		
		echo $name . $pwd;
	
	               // si el nombre y password existen en la bbdd devolvemos la llave de conexion
	                $query = $this->_conn->prepare ( 
					"SELECT id, name, hash FROM user WHERE name=:name AND password=:pwd " );
					$query->bindValue ( ":name", $name );
					$query->bindValue ( ":pwd", md5 ( $pwd ) );
					$query->execute ();
	
					if ($fila = $query->fetch ( PDO::FETCH_ASSOC )) {
						// No dar pistas si el login ha sido correcto para no dar pistas !!
						$respuesta ['estado'] = 'correcto';
						$respuesta ['msg'] = 'datos pertenecen a usuario registrado';
						//$respuesta ['usuario'] ['id'] = $fila ['id'];
						//$respuesta ['usuario'] ['name'] = $fila ['name'];
						$respuesta ['usuario'] ['hash'] = $fila ['hash'];
						$this->mostrarRespuesta ( $this->convertirJson ( $respuesta ), 200 );
						
					}else {
						// Creo que no merece la pena. Devolver login incorrecto
	                    $respuesta ['estado'] = 'correcto';
						$respuesta ['msg'] = 'datos No pertenecen a usuario registrado';
						
						$this->mostrarRespuesta ( $this->convertirJson ( $respuesta ), 200 );
							
					}
	
	}
	
}
