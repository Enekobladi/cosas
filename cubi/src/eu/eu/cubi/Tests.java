package eu.eu.cubi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Tests {

	/** Tiempo máximo para establecer la conexion */
	private static final int TIMEOUT_CONEXION = 2000;

	/** Tiempo maximo para recibir los datos */
	private static final int TIMEOUT_DATOS = 3000;

	/** Número de veces que se intenta obtener la cadena con la version del http */
	private static final int HTTP_TRIES = 1;

	/** Url que se comprueba */
	private static final String PARAM_URL = "http://62.83.200.11:8732";

	/** IP que se comprueba */
	private static final String PARAM_IP = "62.83.200.11";

	public static boolean isHttpAvailable() {
		try {
			/*
			 * Metodo 2 // Url URL url = new URL(PARAM_URL);
			 * 
			 * // abrir conexion HttpURLConnection urlConnect =
			 * (HttpURLConnection)url.openConnection();
			 * 
			 * // intentar obtener contenido. Si no puede dara error Object
			 * objData = urlConnect.getContent();
			 * 
			 * return true;
			 */
			BufferedReader in = null;
			HttpParams httpParameters = new BasicHttpParams();

			// Tiempo de espera por si la conexión va lenta
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					TIMEOUT_CONEXION);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_DATOS);

			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet request = new HttpGet();

			request.setURI(new URI(PARAM_URL));

			// se intenta conectar y obtener la cadena
			// con el numero de la version X veces
			for (int i = 0; i < HTTP_TRIES; i++) {
				HttpResponse response = client.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));

				// va leyendo líneas de codigo html de la página de Google Play
				// si encuentra la versión dentro del código sale y devuelve
				// true
				String line = null;

				while ((line = in.readLine()) != null) {
					// Si la peticion http ha devuelto alguna linea es accesible
					if (line.length() > 0) {
						return true;
					}
				}

				if (in != null) {
					in.close();

				}

			}
			// Si se ha comprobado y no está la version devolver false
			return false;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// Si hay cualquier error de conexión etc... devolver false para
		// comprobar ftp
		return false;

	}

	/**
	 * Comprobar conectividad Internet
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * PING
	 * 
	 * @return
	 */
	public static boolean isIpAvailable() {
		InetAddress ip;
		try {

			/** MEtodo 1 */
			Process p1 = java.lang.Runtime.getRuntime().exec(
					"ping -c 1 " + PARAM_IP);
			int returnVal = p1.waitFor();
			if (returnVal == 0) {
				return true;
			}

			/** MEtodo 2 */
			ip = InetAddress.getByName(PARAM_IP);
			if (ip != null && ip.isReachable(TIMEOUT_CONEXION)) {
				return true;

			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
