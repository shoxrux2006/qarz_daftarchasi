package uz.gita.qarzdaftarchasi.presentation.addDebt.vm

import uz.gita.qarzdaftarchasi.utils.AppViewModel
import java.io.File

interface AddDebtVM : AppViewModel<AddDebtIntent, AddDebtUIState, AddDebtSideEffect> {
}

sealed class AddDebtIntent {
    data class AddDebt(
        val list: List<File>,
        val name: String,
        val sum: String,
        val phone: String,
        val deadLine: Long
    ) : AddDebtIntent()

    object AddImage : AddDebtIntent()
    data class SaveImage(val file: File) : AddDebtIntent()
    data class DeleteImage(val file: File) : AddDebtIntent()
    object ConfirmDelete : AddDebtIntent()
}

sealed class AddDebtUIState {
    data class Images(val list: List<File>) : AddDebtUIState()
    data class Error(
        val name: String,
        val phone: String,
        val sum: String,
        var date:String,
    ) : AddDebtUIState()
}

sealed class AddDebtSideEffect {

    object AddImage : AddDebtSideEffect()
    data class DeleteImage(val file: File) : AddDebtSideEffect()
    data class Message(val str: String) : AddDebtSideEffect()
}
