package uz.gita.qarzdaftarchasi.presentation.main.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.qarzdaftarchasi.domain.repository.MainRepository
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.navigation.AppNavigation
import uz.gita.qarzdaftarchasi.presentation.addDebt.AddDebtScreen
import uz.gita.qarzdaftarchasi.presentation.debtDetails.DebtDetailsScreen
import uz.gita.qarzdaftarchasi.utils.NetworkResponse
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainVMImpl @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context,
    private val navigation: AppNavigation
) : MainVM, ViewModel() {
    override fun onEventDispatcher(intent: MainIntent) = intent {
        when (intent) {
            is MainIntent.AddSum -> {
                updateDebt(intent.debtEntity)
            }
            is MainIntent.Search -> {
                search(intent.str)
            }
            is MainIntent.DebtDetails -> {
                navigation.navigateTo(DebtDetailsScreen(intent.debtEntity))
            }
            is MainIntent.AddDebt -> {
                navigation.navigateTo(AddDebtScreen())
            }
            is MainIntent.Delete -> {
                deleteDebt(intent.item)
            }
        }
    }


    private fun getDebts() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getOnlineDebt().collectLatest {
                 async {
                    File(context.cacheDir, "images").mkdirs()
                    var count = it.lastIndex
                    while (count > -1) {
                        val debt = it[count]
                        var imageCount = debt.images.lastIndex
                        var conflict = -1
                        while (imageCount > -1) {
                            val it = debt.images[imageCount]
                            val file = File(it)
                            if (!file.exists()) {
                                if (conflict != imageCount) {
                                    conflict = imageCount
                                    val storage = FirebaseStorage.getInstance()
                                    val ref = storage.reference.child(it)
                                    ref.getFile(file).addOnCompleteListener {
                                        conflict = -1
                                        imageCount--
                                    }
                                }
                            } else {
                                conflict = -1
                                imageCount--
                            }
                        }
                        count--
                    }
                }.await()
                mainRepository.setOfflineDebt(it)
                mainRepository.getDebts().collectLatest {
                    reduce { MainUIState.Success(it) }
                }
            }
        }
    }


    private fun search(string: String) = intent {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.search(string).collectLatest {
                reduce { MainUIState.Success(it) }
            }
        }
    }

    private fun updateDebt(debtEntity: DebtEntity) = intent {
        viewModelScope.launch {
            mainRepository.updateDebt(debtEntity).collectLatest {
                when (it) {
                    is NetworkResponse.Error -> {}
                    is NetworkResponse.Failure -> {
                        mainRepository.updateDebtOffline(debtEntity)
                    }
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.NoConnection -> {}
                    is NetworkResponse.Success -> {
                        mainRepository.updateDebtOffline(it.data!!)
                    }
                }
            }
        }
    }


    private fun deleteDebt(debtEntity: DebtEntity) = intent {
        viewModelScope.launch {
            mainRepository.deleteDebt(debtEntity)
        }
    }

    private fun deleteDebt(debtEntity: List<DebtEntity>) = intent {
        viewModelScope.launch {
            mainRepository.deleteDebt(debtEntity)
        }
    }

    override val container: Container<MainUIState, MainSideEffect> =
        container(MainUIState.Success(emptyList()))

    init {
        getDebts()
    }
}