package uz.gita.qarzdaftarchasi.presentation.addDebt.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.qarzdaftarchasi.domain.repository.MainRepository
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.navigation.AppNavigation
import uz.gita.qarzdaftarchasi.utils.NetworkResponse
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddDebtVMImpl @Inject constructor(
    private val mainRepository: MainRepository,
    private val navigation: AppNavigation
) : AddDebtVM, ViewModel() {
    var list: ArrayList<File> = arrayListOf()
    var file: File = File("")
    override fun onEventDispatcher(intent: AddDebtIntent) = intent {
        when (intent) {
            is AddDebtIntent.AddDebt -> {
                reduce {
                    AddDebtUIState.Error(
                        if (!intent.name.isNotEmpty()) "ism kiriting" else "",
                        if (!(intent.phone.length == 13)) "telefon raqam kiriting" else "",
                        if (!(intent.sum.isNotEmpty() && !intent.sum.contains(".") && intent.sum.toDouble() > 1)) "qarz miqdorini kiriting" else "",
                        if (!(intent.deadLine != 0L)) "deadline kiriting" else ""
                    )
                }
                if (intent.name.isNotEmpty()) {
                    if (intent.phone.length == 13) {
                        if (intent.sum.toDouble() > 1) {
                            if (intent.deadLine != 0L) {
                                addDebt(
                                    DebtEntity(
                                        System.currentTimeMillis(),
                                        intent.name,
                                        intent.phone,
                                        intent.list.map {
                                            it.absolutePath
                                        }.toList(),
                                        intent.sum.toLong(),
                                        0,
                                        intent.deadLine
                                    )
                                )

                            }
                        }
                    }
                }
            }
            AddDebtIntent.AddImage -> {
                postSideEffect(
                    AddDebtSideEffect.AddImage
                )
            }
            is AddDebtIntent.DeleteImage -> {
                file = intent.file
                postSideEffect(AddDebtSideEffect.DeleteImage(intent.file))
            }
            is AddDebtIntent.SaveImage -> {
                list.add(intent.file)
                reduce { AddDebtUIState.Images(list) }
            }
            AddDebtIntent.ConfirmDelete -> {
                list.remove(file)
                reduce { AddDebtUIState.Images(list) }
            }
        }
    }

    override val container: Container<AddDebtUIState, AddDebtSideEffect> =
        container(AddDebtUIState.Error("", "", "", ""))

    private fun addDebt(debtEntity: DebtEntity) = intent {
        viewModelScope.launch {
            mainRepository.addDebt(debtEntity).collectLatest {
                when (it) {
                    is NetworkResponse.Error -> {}
                    is NetworkResponse.Failure -> {
                        mainRepository.addDebtOffline(debtEntity)

                      navigation.back()
                    }
                    is NetworkResponse.Loading -> {}
                    is NetworkResponse.NoConnection -> {}
                    is NetworkResponse.Success -> {
                        mainRepository.getOnlineDebt().collectLatest {
                         mainRepository.setOfflineDebt(it)
                            navigation.back()
                        }
                    }
                }
            }
        }
    }

}