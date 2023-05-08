package uz.gita.qarzdaftarchasi.presentation.addDebt

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import uz.gita.qarzdaftarchasi.R
import uz.gita.qarzdaftarchasi.ui.theme.ErrorColor
import java.io.File
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardViewPager(list: List<File>, addListener: () -> Unit, deleteListener: (File) -> Unit) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = list.size + 1,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 30.dp),
        modifier = Modifier
    ) { page ->
        if (page == list.size) {
            AddCartItem(pagerState.currentPage == page) {
                addListener()
            }
        } else CardItem(list[page].absolutePath, pagerState.currentPage == page) {
            deleteListener(list[page])
        }
    }
    LaunchedEffect(key1 = pagerState) {
        pagerState.animateScrollToPage(0)
    }
}

@Composable
private fun AddCartItem(isSelected: Boolean, onclick: () -> Unit) {
    val modifier = if (isSelected) {
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1.77f)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                onclick()
            }
            .padding(40.dp)
    } else {
        Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1.77f)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                onclick()
            }
            .padding(40.dp)
    }
    Icon(
        tint = Color.White,
        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
        contentDescription = "add",
        modifier = modifier
    )


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardItem(item: String, isSelected: Boolean, click: () -> Unit) {
    val modifier = if (isSelected) {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1.77f)
            .shadow(
                30.dp
            )
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    click()
                },
            )
    } else {
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1.77f)
            .shadow(
                30.dp
            )
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    click()
                },
            )
    }

    Box(
        modifier = modifier.background(Color.Gray)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item)
                .build(),
            contentDescription = "back",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun DeadLineTime(
    hint: String,
    errorDateMessage: String, onChange: (LocalDate) -> Unit
) {
    val errorBirth = errorDateMessage.isNotEmpty()
    var selectedDates: LocalDate? by rememberSaveable {
        mutableStateOf(null)
    }
    var state = rememberSheetState(visible = false, onCloseRequest = {

    })
    CalendarDialog(
        state = state,
        config = CalendarConfig(
            maxYear = 2040,
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { newDates ->
            selectedDates = newDates
            onChange(selectedDates!!)
        },
    )
    Text(text = if (selectedDates != null) {
        "${selectedDates!!.dayOfMonth}-${
            selectedDates!!.month.toString().substring(0, 3)
        }-${selectedDates!!.year}"
    } else hint,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                state.show()
            }
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(14.dp),
        style = MaterialTheme.typography.bodyMedium)
    if (errorBirth) {
        Text(
            text = errorDateMessage,
            color = ErrorColor,
            style = MaterialTheme.typography.titleSmall
        )
    }

}