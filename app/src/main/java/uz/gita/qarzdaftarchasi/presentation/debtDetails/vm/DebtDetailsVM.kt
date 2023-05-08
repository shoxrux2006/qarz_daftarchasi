package uz.gita.qarzdaftarchasi.presentation.debtDetails.vm

import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainIntent
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainUIState
import uz.gita.qarzdaftarchasi.utils.AppViewModel

interface DebtDetailsVM : AppViewModel<DebtDetailsIntent, DebtDetailsUIState, DebtDetailsSideEffect> {
}

sealed class DebtDetailsIntent {
   data class ImagePreview(val path:String):DebtDetailsIntent()
    data class AddSum(val debtEntity: DebtEntity) : DebtDetailsIntent()
}

sealed class DebtDetailsUIState {
    object Default:DebtDetailsUIState()
    data class Success(val data: DebtEntity) : DebtDetailsUIState()
}

sealed class DebtDetailsSideEffect {

}