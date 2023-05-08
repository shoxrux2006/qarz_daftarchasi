package uz.gita.qarzdaftarchasi.shp

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Shp @Inject constructor(@ApplicationContext val context: Context) {
    private val shp = context.getSharedPreferences("shp", Context.MODE_PRIVATE)

    fun getString(name: String): String = shp.getString(name, "")!!
    fun setString(name: String, data: String) {
        shp.edit().putString(name, data)
            .apply()
    }
    fun getInt(name: String): Int = shp.getInt(name, 1)!!
    fun setInt(name: String, data: Int) {
        shp.edit().putInt(name, data)
            .apply()
    }

    fun getLong(name: String): Long = shp.getLong(name, 0)
    fun setLong(name: String, data: Long) {
        shp.edit().putLong(name, data).apply()
    }

    fun getBool(name: String): Boolean = shp.getBoolean(name, true)
    fun setBool(name: String, data: Boolean) {
        shp.edit().putBoolean(name, data).apply()
    }
}