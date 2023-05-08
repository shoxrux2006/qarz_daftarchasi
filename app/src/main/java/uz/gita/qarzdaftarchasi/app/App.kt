package uz.gita.qarzdaftarchasi.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.gita.qarzdaftarchasi.lang.LangType
import uz.gita.qarzdaftarchasi.lang.Word

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}