package eu.eu.cubi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class Notifications {

	private Context context;

	private int ok = R.drawable.ic_ok;

	private int error = R.drawable.ic_error;

	/**
	 * Mostrar mensaje en el área de notificaciones cuando se haya establecido
	 * comunicación con el lector Bluetooth
	 */
	private boolean preError = true;

	public Notifications(Context context) {
		this.context = context;
	}

	public void mostrarComprobando(boolean internet, boolean ip, boolean http,
			String text) {
		if (internet) {
			if (ip && http && preError) {
				showNotification(0, "OK", "Luz, Internet, torrent..OK",
						"Siguiente comprobacion: " + text, ok, false);
				preError = true;
			} else {
				if (!ip) {
					showNotification(0, "Error", "Error(no ping)!!!!",
							"Siguiente comprobacion: " + text, error, false);
					preError = true;
				} else if (!http) {
					showNotification(0, "Error", "Error(no transmission)!!!!",
							"Siguiente comprobacion: " + text, error, false);
					preError = true;
				}

			}

		} else {
			showNotification(0, "Error", "Error(no hay internet)!!!!",
					"activa Internet", error, false);
			preError = true;
		}

	}

	/**
	 * Ocultar mensaje del área de notificaciones y mostrar que ya no se pueden
	 * leer códigos de barras
	 */
	public void ocultarComprobando() {
		new OcultarComprobando().execute();

	}

	private void ocultarNotificacion(int id) {
		// Obtenemos una referencia al servicio de notificaciones
		String servicio = Context.NOTIFICATION_SERVICE;
		NotificationManager notManager = (NotificationManager) context
				.getSystemService(servicio);

		// Quitar notificación de Bluetooth escuchando
		notManager.cancel(id);

	}

	private void showNotification(int id, CharSequence state,
			CharSequence title, CharSequence descripcion, int icono,
			boolean alert) {
		// Obtenemos una referencia al servicio de notificaciones
		String servicio = Context.NOTIFICATION_SERVICE;
		NotificationManager notManager = (NotificationManager) context
				.getSystemService(servicio);

		// Configuramos la notificaci�n

		long hora = System.currentTimeMillis();

		Notification notif = new Notification(icono, state, hora);

		// Configuramos el Intent
		Intent notIntent = new Intent(context, MainActivity.class);

		PendingIntent contIntent = PendingIntent.getActivity(context, 0,
				notIntent, 0);

		notif.setLatestEventInfo(context, title, descripcion, contIntent);

		// AutoCancel: cuando se pulsa la notificaión esta desaparece
		notif.flags |= Notification.FLAG_NO_CLEAR;

		// Añadir sonido, vibración y luces si es posible
		// notif.defaults |= Notification.DEFAULT_VIBRATE;
		// notif.defaults |= Notification.DEFAULT_LIGHTS;
		// notif.defaults |= Notification.DEFAULT_SOUND;

		notManager.notify(id, notif);

	}

	private class OcultarComprobando extends AsyncTask<Void, Void, Void> {

		private static final int tiempo = 3;

		@Override
		protected Void doInBackground(Void... params) {
			int segundos = 1;
			while (segundos < tiempo && !isCancelled()) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				segundos++;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			ocultarNotificacion(1);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			ocultarNotificacion(0);

			showNotification(1, "Servicio parado", "No se comprobara casa!",
					"Servicio parado. No se comprobara casa!", error, false);

		}

	}
}
