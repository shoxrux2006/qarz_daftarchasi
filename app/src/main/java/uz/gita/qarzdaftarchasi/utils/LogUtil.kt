package uz.gita.qarzdaftarchasi.utils

import android.util.Log
import uz.gita.qarzdaftarchasi.BuildConfig

fun l(string: String){
    if(BuildConfig.DEBUG){
        Log.d("TTT",string)
    }
}