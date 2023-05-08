package uz.gita.qarzdaftarchasi.domain.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters(){
    @TypeConverter
    fun toList(listOfString: String?): List<String?>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun toString(listOfString: List<String?>?): String? {
        return Gson().toJson(listOfString)
    }
}
