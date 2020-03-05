package com.dicoding.picodicloma.mymoviecatalogue.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.dicoding.picodicloma.mymoviecatalogue.R


/**
 * Implementation of App Widget functionality.
 */
class FavoriteWatchableBannerWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.dicoding.picodicloma.TOAST_ACTION"
        const val EXTRA_ITEM = "com.dicoding.picodicloma.EXTRA_ITEM"
        private val TAG = FavoriteWatchableBannerWidget::class.java.simpleName

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = this.toUri(Intent.URI_INTENT_SCHEME).toUri()
            }

            val views =
                RemoteViews(
                    context.packageName,
                    R.layout.favorite_watchable_banner_widget
                )
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(
                R.id.stack_view,
                R.id.empty_view
            )


            val toastIntent = Intent(context, FavoriteWatchableBannerWidget::class.java).apply {
                action =
                    TOAST_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun notifyWidgetDataChanged(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget =
                ComponentName(context.packageName, FavoriteWatchableBannerWidget::class.java.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            Log.d(TAG, "onReceive: $appWidgetId")
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        intent?.action.let {
            if (it == TOAST_ACTION) {
                val viewIndex = intent?.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(
                    context,
                    "Touched view $viewIndex",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}