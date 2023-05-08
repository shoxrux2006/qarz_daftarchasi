package uz.gita.qarzdaftarchasi.presentation.debtDetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.lang.Word
import uz.gita.qarzdaftarchasi.presentation.Button
import uz.gita.qarzdaftarchasi.presentation.SumEdit
import uz.gita.qarzdaftarchasi.presentation.debtDetails.vm.DebtDetailsIntent
import uz.gita.qarzdaftarchasi.presentation.debtDetails.vm.DebtDetailsUIState
import uz.gita.qarzdaftarchasi.presentation.debtDetails.vm.DebtDetailsVM
import uz.gita.qarzdaftarchasi.presentation.debtDetails.vm.DebtDetailsVMImpl
import java.io.File
import java.time.LocalDate


class DebtDetailsScreen(var entity: DebtEntity) : AndroidScreen() {


    @Composable
    override fun Content() {
        val viewModel: DebtDetailsVM = getViewModel<DebtDetailsVMImpl>()

        DebtDetailsScreenContent(viewModel.collectAsState().value, viewModel::onEventDispatcher)
    }

    @Composable
    fun DebtDetailsScreenContent(
        uiState: DebtDetailsUIState,
        onEventDispatcher: (DebtDetailsIntent) -> Unit
    ) {
        var debtEntity by remember {
            mutableStateOf(entity)
        }
        val context = LocalContext.current
        val date by remember {
            mutableStateOf(LocalDate.ofEpochDay(debtEntity.deadLine / (24 * 60 * 60 * 1000)))
        }
        var addDialog: DebtEntity? by remember {
            mutableStateOf(null)
        }
        when (uiState) {
            is DebtDetailsUIState.Success -> {
                debtEntity = uiState.data
            }
            DebtDetailsUIState.Default -> {

            }
        }
        addDialog?.let {
            var sumEditText by remember {
                mutableStateOf("")
            }
            var error by remember {
                mutableStateOf("")
            }
            Dialog(
                onDismissRequest = { addDialog = null },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    SumEdit(hint = "summa", errorMessage = error) {
                        sumEditText = it
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(text = "To'lash") {
                        if (sumEditText.isNotEmpty() && !sumEditText.contains(".")) {
                            addDialog!!.paidSum += sumEditText.toLong()
                            onEventDispatcher(DebtDetailsIntent.AddSum(addDialog!!))
                            addDialog = null
                        } else {
                            error = "summa kiriting"
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            ViewPager(list = debtEntity.images.map { path -> File(path) }) {
                onEventDispatcher(DebtDetailsIntent.ImagePreview(it))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedText(hint = Word.name, text = debtEntity.name)
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PhoneNumberText(
                    modifier = Modifier.weight(1f),
                    hint = Word.phone,
                    debtEntity.phoneNumber
                )
                Icon(
                    Icons.Default.Call,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${debtEntity.phoneNumber}")
                            startActivity(context, intent, null)
                        }
                        .padding(10.dp),
                    contentDescription = "call",
                    tint = MaterialTheme.colorScheme.primary

                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            SumText(hint = Word.sum, text = debtEntity.sum.toString())
            Spacer(modifier = Modifier.height(10.dp))
            SumText(hint = "Qoldi", text = (debtEntity.sum - debtEntity.paidSum).toString())
            Spacer(modifier = Modifier.height(10.dp))
            SumText(hint = "To'landi", text = debtEntity.paidSum.toString())
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${date.dayOfMonth}-${
                    date.month.toString().substring(0, 3)
                }-${date.year}",
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(14.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(text = "To'lash") {
                addDialog = debtEntity
            }
        }
    }
}