package uz.gita.qarzdaftarchasi.presentation.addDebt

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.qarzdaftarchasi.lang.Word
import uz.gita.qarzdaftarchasi.presentation.Button
import uz.gita.qarzdaftarchasi.presentation.OutlinedEditText
import uz.gita.qarzdaftarchasi.presentation.PhoneNumberEdit
import uz.gita.qarzdaftarchasi.presentation.SumEdit
import uz.gita.qarzdaftarchasi.presentation.addDebt.vm.*
import uz.gita.qarzdaftarchasi.utils.ImagePicker
import java.io.File
import java.time.ZoneOffset


class AddDebtScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: AddDebtVM = getViewModel<AddDebtVMImpl>()
        val uiState = viewModel.collectAsState().value
        val context = LocalContext.current
        var showAddImageDialog by remember {
            mutableStateOf(false)
        }
        var showDeleteImageDialog: File? by remember {
            mutableStateOf(null)
        }
        val imagePicker = ImagePicker()
        imagePicker.captureListener {
            if (it != null) {
                viewModel.onEventDispatcher(AddDebtIntent.SaveImage(it))
            } else {
                viewModel.onEventDispatcher(AddDebtIntent.ConfirmDelete)
            }
        }
        imagePicker.dismissListener {
            showDeleteImageDialog = null
            showAddImageDialog = false
        }
        imagePicker.TakePicture(showAddImageDialog)
        if (showDeleteImageDialog != null) {
            imagePicker.DeleteImage(file = showDeleteImageDialog!!)
        }
        viewModel.collectSideEffect {
            when (it) {
                AddDebtSideEffect.AddImage -> {
                    showAddImageDialog = true
                }
                is AddDebtSideEffect.DeleteImage -> {
                    showDeleteImageDialog = it.file
                }
                is AddDebtSideEffect.Message -> {
                    Toast.makeText(context, it.str, Toast.LENGTH_SHORT).show()
                }
            }
        }

        AddDebtContent(uiState, viewModel::onEventDispatcher)
    }

    @Composable
    fun AddDebtContent(uiState: AddDebtUIState, onEventDispatcher: (AddDebtIntent) -> Unit) {
        var errorName by remember {
            mutableStateOf("")
        }
        var errorPhone by remember {
            mutableStateOf("")
        }
        var errorSum by remember {
            mutableStateOf("")
        }
        var errorDate by remember {
            mutableStateOf("")
        }

        var textName by remember {
            mutableStateOf("")
        }
        var textPhone by remember {
            mutableStateOf("")
        }
        var textSum by remember {
            mutableStateOf("")
        }
        var date by rememberSaveable {
            mutableStateOf(0L)
        }
        var images: List<File> by remember {
            mutableStateOf(listOf())
        }

        var showAddImageDialog by remember {
            mutableStateOf(false)
        }

        if (showAddImageDialog) {

            val imagePicker = ImagePicker()
            imagePicker.captureListener {
                if (it != null) {
                    onEventDispatcher(AddDebtIntent.SaveImage(it))
                } else {
                    onEventDispatcher(AddDebtIntent.ConfirmDelete)
                }
            }
        }
        when (uiState) {
            is AddDebtUIState.Error -> {
                errorName = uiState.name
                errorPhone = uiState.phone
                errorSum = uiState.sum
                errorDate = uiState.date
            }
            is AddDebtUIState.Images -> {
                images = uiState.list
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            CardViewPager(
                list = images,
                addListener = {
                    onEventDispatcher(AddDebtIntent.AddImage)
                }) {
                onEventDispatcher(AddDebtIntent.DeleteImage(it))
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedEditText(hint = Word.name, maxLength = 20, errorMessage = errorName) {
                textName = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            PhoneNumberEdit(hint = Word.phone, errorMessage = errorPhone) {
                textPhone = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            SumEdit(hint = Word.sum, errorMessage = errorSum) {
                textSum = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            DeadLineTime(hint = "DeadLine", errorDateMessage = errorDate) {
                date = it.atStartOfDay(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(text = Word.add) {
                onEventDispatcher(AddDebtIntent.AddDebt(images, textName, textSum, textPhone, date))
            }
        }
    }
}