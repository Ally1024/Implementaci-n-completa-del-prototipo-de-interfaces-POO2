package com.example.avancesproyecto.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

// Clase encargada de centralizar la configuracion y el envio de alertas del sistema
class NotificationHelper(private val context: Context) {

    // Identificador unico para el canal de notificaciones requerido a partir de Android O
    private val CHANNEL_ID = "event_alerts_channel"

    // Identificador unico para la instancia de la notificacion activa
    private val NOTIFICATION_ID = 101

    // ==========================================
    // METODO: CREAR CANAL DE NOTIFICACION
    // ==========================================
    // Define el canal indispensable para dispositivos con Android 8.0 o superior.
    // Configura la prioridad alta para asegurar que la alerta se muestre como un banner emergente.
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alertas de Eventos"
            val descriptionText = "Canal para notificar alertas de eventos importantes"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Registro el canal dentro del servicio del sistema operativo
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // ==========================================
    // METODO: DISPARAR NOTIFICACION DE EVENTOS
    // ==========================================
    // Construye y despliega la alerta visual en la barra de tareas del dispositivo.
    // Incluye verificacion estricta de permisos en tiempo de ejecucion obligatoria desde Android 13.
    fun sendEventNotification(title: String, message: String) {
        // Validacion de seguridad para Android 13 o superior (API de Tiramisu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Si el estudiante denegó el permiso, cancelo la ejecucion de fondo de forma segura
            return
        }

        // Construccion estructural de la vista de la notificacion
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icono nativo del sistema para alertas de informacion
            .setContentTitle(title) // Texto del encabezado de la alerta
            .setContentText(message) // Cuerpo informativo del mensaje
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Establece la prioridad alta para compatibilidad con APIs viejas
            .setAutoCancel(true) // Remueve la notificacion automaticamente cuando el usuario le da un clic

        // Despacho la alerta a traves del gestor de compatibilidad nativo
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}