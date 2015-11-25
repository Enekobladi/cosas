<?php
/*

Comprobar hash de la consulta.
Si es login: md5 (user + pass);
Si no: md5 (request + YYMMDD + llave)
  
*/

class Hash  {
	private $_conn = NULL;
	public function __construct($_conn) {
		$this->_conn = $_conn;
	}
	
	// 
	// Comprobar hash login
	// 
	//
	public function testLogin ($user, $pass, $hash) {
		$sumHash = md5 ($user . $pass);
		
	   	if ( strcmp($sumHash, $hash) == 0){
			return true;
			
		}else{
			return false;
			
		}
		
	}
	
	// 
	// Comprobar hash de las consultas
	// 
	//
	public function testRequest ($name, $request, $hash) {
		$llave = $this->getLLave($name);
		
		echo ' ' . $llave . ' ' . $request . ' ' . date("Y.m.d") . ' ' ;
		
		// La fecha añade un extra de salt para que cambie a diario
		$sumHash = md5 ($llave . $request . date("Y.m.d") );
		
		// Si el hash es correcto comprobar que el usuario exista
	   	if ( strcmp($sumHash, $hash) == 0){
			return true;
			
		}else{
			return false;
			
		}
		
	}
	
	private function getLLave ($name){
		$query = $this->_conn->prepare ( 
					"SELECT hash FROM user WHERE name=:name" );
					$query->bindValue ( ":name", $name );
					$query->execute ();
					
			if ($fila = $query->fetch ( PDO::FETCH_ASSOC )) {
				return $fila ['hash'];
				
			}
			
	}
	
	public function login() {
		$name = ( $_REQUEST ['name'] );
		$pwd =  ( $_REQUEST ['password'] );
	
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
