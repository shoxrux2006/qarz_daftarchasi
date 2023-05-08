package uz.gita.qarzdaftarchasi.presentation.debtDetails.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.qarzdaftarchasi.domain.repository.MainRepository
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.navigation.AppNavigation
import uz.gita.qarzdaftarchasi.presentation.imagePreview.ImagePreviewScreen
import javax.inject.Inject

@HiltViewModel()
class DebtDetailsVMImpl @Inject constructor(
    private val mainRepository: MainRepository,
    val navigation: AppNavigation
) : DebtDetailsVM, ViewModel() {


    override fun onEventDispatcher(intent: DebtDetailsIntent) = intent {
        when (intent) {
            is DebtDetailsIntent.ImagePreview -> {
                navigation.navigateTo(ImagePreviewScreen(intent.path))
            }
            is DebtDetailsIntent.AddSum -> {
                updateDebt(intent.debtEntity)
            }
        }
    }

    private fun updateDebt(debtEntity: DebtEntity) = intent {
        viewModelScope.launch {
            mainRepository.updateDebt(debtEntity)
            reduce { DebtDetailsUIState.Success(debtEntity) }
        }
    }

    override val container: Container<DebtDetailsUIState, DebtDetailsSideEffect> =
        container(DebtDetailsUIState.Default)

}