package uz.gita.qarzdaftarchasi.lang

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf

object Word {
    private var lang = mutableStateOf(LangType.LATIN)
    var search = ""
    var debt=""
    var left=""
    var pay=""
    var moneyTp=""
    var name=""
    var phone=""
    var add=""
    var sum=""
    @Composable
    fun ChangeLang(langType: LangType) {
        lang.value = langType
        when (lang.value) {
            LangType.LATIN -> {
                add="qo'shish"
                sum="Qarz miqdori"
                phone="Telefon"
                name="Ism va familiya"
                moneyTp="so'm"
                pay="to'lash"
                debt="Qarz"
                left="Qoldi"
                search = "Qidiruv"
            }
            LangType.KRILL -> {
                add="кошиш"
                sum="Карз микдори"
                phone="телефон"
                name="исм ва фамилия"
                moneyTp="сум"
                pay="тулаш"
                debt="Карз"
                left="Колди"
                search = "кидирув"
            }
        }
    }
}