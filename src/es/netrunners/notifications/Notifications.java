package es.netrunners.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Notifications extends Activity {

	int NOTIFICATION_ID = 1;

	// Creamos una implementación anónima de OnClickListener
	public OnClickListener btnToastListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Context context = getApplicationContext();
			CharSequence text = "¡Hola toast!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (this.getIntent().getExtras() != null) {
			Boolean notif = getIntent().getExtras().getBoolean("Notification");
			NotificationManager nm;
			nm = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			if (notif != null && notif)
				nm.cancel(NOTIFICATION_ID);
		}

		Button btnToast = (Button) findViewById(R.id.btnToast);
		btnToast.setOnClickListener(btnToastListener);

		Button btnDialog = (Button) findViewById(R.id.btnDialog);
		btnDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Segundos que el ProgressDialog aparecerá
				final int seconds = 5;

				// Iniciar y mostrar ProgressDialog
				final ProgressDialog progressDialog = ProgressDialog.show(
						Notifications.this, "Por favor Espere ...",
						"Cargando ...");

				// Establecemos un Temporizador para ocultar el Dialog al
				// transcurrir el tiempo especificado
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(seconds * 1000);
							progressDialog.dismiss();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	public void showStatusBarNotification(View v) {

		// Creamos una instancia del Administrador de Notificaciones
		NotificationManager nm = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Establecemos algunos valores
		CharSequence from = "NetRunners";
		CharSequence message = "Esto es un Texto de Notificación...";

		// Creamos el intent que será llamado cuando se haga clic en el evento
		// de la notificación
		Intent intent = new Intent(getApplicationContext(), Notifications.class);
		intent.putExtra("Notification", true);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// Creamos la notificación
		Notification notification = new Notification(R.drawable.ic_launcher,
				message, System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), from, message,
				contentIntent);

		// Enviamos la Notificación
		nm.notify(NOTIFICATION_ID, notification);
	}
}