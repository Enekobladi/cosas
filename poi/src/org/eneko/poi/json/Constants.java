package org.eneko.poi.json;

/**
 * {"id":"1", "title":"Casa Batlló",
 * "address":"Paseo de Gracia, 43, 08007 Barcelona",
 * "transport":"Underground:Passeig de Gràcia -L3",
 * "email":"http://www.casabatllo.es/en/",
 * "geocoordinates":"41.391926,2.165208", "description":"Casa Batlló is a key
 * feature in the architecture of modernist Barcelona. It was built by Antoni
 * Gaudí between 1904 and 1906 having been commissioned by the textile
 * industrialist Josep Batlló. Nowadays, the spectacular facade is an iconic
 * landmark in the city. The \"Manzana de la Discordia\", or Block of Discord,
 * is a series of buildings in Passeig de Gràcia. It is home to a collection of
 * works by the most renowned architects, amongst which is Casa Batlló. The
 * house, now a museum, is open to the public, both for cultural visits and for
 * celebrating events in its splendid modernist function rooms.",
 * "phone":"info@casabatllo.cat"}
 */
public class Constants {
	// URL to get contacts JSON
	public static String url = "http://t21services.herokuapp.com/points/";

	// JSON Node names
	public static final String TAG_ID = "id";
	public static final String TAG_TITLE = "title";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_TRANSPORT = "transport";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_GEOCORDINATES = "geocordinates";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_PHONE = "phone";

}
