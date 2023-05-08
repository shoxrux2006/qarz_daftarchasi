package uz.gita.qarzdaftarchasi.presentation.main.vm

import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.utils.AppViewModel

interface MainVM : AppViewModel<MainIntent, MainUIState, MainSideEffect> {
}

sealed class MainIntent {
    object AddDebt : MainIntent()
    data class Search(val str: String) : MainIntent()
    data class AddSum(val debtEntity: DebtEntity) : MainIntent()
    data class Delete(val item:List<DebtEntity>) : MainIntent()
    data class DebtDetails(val debtEntity: DebtEntity) : MainIntent()
}

sealed class MainUIState {
    data class Success(val list: List<DebtEntity>) : MainUIState()
}

sealed class MainSideEffect {
    data class Message(val message: String) : MainSideEffect()
}