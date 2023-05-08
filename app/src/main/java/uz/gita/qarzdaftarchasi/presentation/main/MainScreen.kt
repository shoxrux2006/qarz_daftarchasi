package uz.gita.qarzdaftarchasi.presentation.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.qarzdaftarchasi.R
import uz.gita.qarzdaftarchasi.domain.room.DebtEntity
import uz.gita.qarzdaftarchasi.lang.Word
import uz.gita.qarzdaftarchasi.presentation.Button
import uz.gita.qarzdaftarchasi.presentation.SumEdit
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainIntent
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainUIState
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainVM
import uz.gita.qarzdaftarchasi.presentation.main.vm.MainVMImpl
import uz.gita.qarzdaftarchasi.ui.theme.ErrorColor
import uz.gita.qarzdaftarchasi.ui.theme.Green

class MainScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: MainVM = getViewModel<MainVMImpl>()
        val uiState = viewModel.collectAsState().value
        MainScreenContent(uiState, viewModel::onEventDispatcher)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MainScreenContent(uiState: MainUIState, onEventDispatcher: (MainIntent) -> Unit) {
        var list: List<DebtEntity> = remember {
            mutableListOf()
        }
        var deleteMode by rememberSaveable {
            mutableStateOf(false)
        }
        val deleteList = remember {
            mutableStateListOf<DebtEntity?>()
        }
        var deleteCount by remember {
            mutableStateOf(0)
        }
        when (uiState) {
            is MainUIState.Success -> {
                if (deleteCount == 0) {
                    deleteMode = false
                }
                uiState.list.forEach { _ ->
                    deleteList.add(null)
                }
                list = uiState.list
            }
        }

        var addDialog: DebtEntity? by remember {
            mutableStateOf(null)
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
                            onEventDispatcher(MainIntent.AddSum(addDialog!!))
                            addDialog = null
                        } else {
                            error = "summa kiriting"
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }

        Box()
        {
            Column(modifier = Modifier.fillMaxSize()) {
                if (list.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Qarzlar yo'q",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }

                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 10.dp)
                    ) {
                        stickyHeader {
                            if (deleteMode) {
                                var clicked by remember {
                                    mutableStateOf(false)
                                }
                                if (clicked) {
                                    DeleteDebt(
                                        list = deleteList,
                                        {
                                            deleteList.clear()
                                            deleteCount = 0
                                            deleteMode = false
                                        },
                                        onEventDispatcher = onEventDispatcher
                                    )
                                }

                                Row() {
                                    Column(modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            if(deleteCount!=list.size){
                                            deleteCount=list.size
                                            deleteList.clear()
                                            list.forEach {
                                                deleteList.add(it)
                                            }
                                            }else{
                                                deleteCount=0
                                                deleteList.clear()
                                            }

                                        }
                                        .weight(1f)
                                        .background(MaterialTheme.colorScheme.onPrimary)
                                        .padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Done, contentDescription = "")
                                        Text(text = "belgilash")
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                clicked = false
                                                clicked = true
                                            }
                                            .weight(1f)

                                            .background(MaterialTheme.colorScheme.onPrimary)
                                            .padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(Icons.Default.Delete, "")
                                        Text(text = "O'chirish")
                                    }

                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                ) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    SearchEditText(hint = Word.search) {
                                        onEventDispatcher(MainIntent.Search(it))
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                        items(list.size) {
                            Spacer(modifier = Modifier.height(10.dp))
                            DebtItem(
                                debtEntity = list[it],
                                isSelected = deleteList[it] != null,
                                click = { its ->
                                    if (its) {
                                        deleteMode = true
                                        if (deleteList[it] != null) {
                                            deleteCount--
                                            deleteList[it] = null
                                        } else {
                                            deleteCount++
                                            deleteList[it] = list[it]
                                        }
                                    } else {
                                        if (deleteMode) {
                                            if (deleteList[it] != null) {
                                                deleteCount--
                                                deleteList[it] = null
                                            } else {
                                                deleteCount++
                                                deleteList[it] = list[it]
                                            }
                                        } else {
                                            onEventDispatcher(MainIntent.DebtDetails(list[it]))
                                        }
                                    }
                                    if (deleteMode && deleteCount == 0) {
                                        deleteMode = false
                                    }
                                }) {
                                addDialog = list[it]
                            }
                        }

                    }

                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                Row() {
                    Spacer(modifier = Modifier.weight(1f))
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(10.dp), onClick = {
                            onEventDispatcher(
                                MainIntent.AddDebt
                            )
                        }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DeleteDebt(
        list: List<DebtEntity?>,
        delete: () -> Unit,
        onEventDispatcher: (MainIntent) -> Unit
    ) {
        var deleteDialog by remember {
            mutableStateOf(true)
        }
        if (deleteDialog) {
            AlertDialog(
                onDismissRequest = { deleteDialog = false },
                title = { Text(text = "Waring") },
                text = { Text(text = "Are you sure want to delete?") },
                confirmButton = {
                    TextButton(onClick = {
                        val newList: ArrayList<DebtEntity> = arrayListOf()
                        list.forEach {
                            if (it != null) {
                                newList.add(it)
                            }
                        }
                        delete()
                        onEventDispatcher(MainIntent.Delete(newList))
                        deleteDialog = false
                    }) {
                        Text(text = "Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        deleteDialog = false
                    }) {
                        Text(text = "Cancel")
                    }
                },
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DebtItem(
        debtEntity: DebtEntity,
        isSelected: Boolean,
        click: (Boolean) -> Unit,
        add: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))

                .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .combinedClickable(
                        onClick = {
                            click(false)
                        },
                        onLongClick = {
                            click(true)
                        },
                    )
            ) {
                if (debtEntity.images.isEmpty()) {
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 5.dp, top = 5.dp, bottom = 5.dp),
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "image"
                    )
                } else {
                    AsyncImage(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 5.dp, top = 5.dp, bottom = 5.dp),
                        model =
                        ImageRequest.Builder(LocalContext.current)
                            .data(debtEntity.images[0])
                            .build(),
                        contentDescription = "image"
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .padding(5.dp)
                ) {
                    Text(
                        maxLines = 1,
                        text = debtEntity.name,
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        maxLines = 1,
                        text = "${Word.debt}:${debtEntity.sum} ",
                        color = Green,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        maxLines = 1,
                        text = "${Word.left}:${(debtEntity.sum - debtEntity.paidSum)}",
                        color = ErrorColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Button(onClick = {
                add()
            }) {
                Text(
                    text = Word.pay,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchEditText(
        hint: String,
        onChange: (String) -> Unit
    ) {
        var textChange by rememberSaveable {
            mutableStateOf("")
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            singleLine = true,
            value = textChange,
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.surface
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "logo",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            onValueChange = {

                textChange = it
                onChange(textChange)
            },
            maxLines = 1,

            placeholder = { Text(text = hint) }
        )

    }


}