package com.dicoding.picodicloma.mymoviecatalogue.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.dicoding.picodicloma.mymoviecatalogue.widget.StackRemoteViewsFactory


class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
       return StackRemoteViewsFactory(
           this.applicationContext
       )
    }
}