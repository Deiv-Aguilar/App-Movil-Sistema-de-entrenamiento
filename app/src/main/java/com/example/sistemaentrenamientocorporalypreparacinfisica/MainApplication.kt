package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.app.ActivityManager
import android.os.Build
import android.os.Process
import androidx.multidex.MultiDexApplication

class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // Solo inicializa Supabase si la ejecución ocurre en el proceso principal de la app
        if (isMainProcess()) {
            SupabaseInstance.getInstance(this)
        }
    }

    private fun isMainProcess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getProcessName() == packageName
        } else {
            val pid = Process.myPid()
            val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            manager.runningAppProcesses?.any { it.pid == pid && it.processName == packageName } ?: false
        }
    }
}