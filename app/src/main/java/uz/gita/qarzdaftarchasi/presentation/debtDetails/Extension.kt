package uz.gita.qarzdaftarchasi.presentation.debtDetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uz.gita.qarzdaftarchasi.R
import uz.gita.qarzdaftarchasi.ui.theme.ErrorColor
import uz.gita.qarzdaftarchasi.utils.PhoneMaskTransformation
import java.io.File


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
            .clickable {
                click()
            }
    } else {
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1.77f)
            .shadow(
                30.dp
            )
            .clickable {
                click()
            }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(list: List<File>, openListener: (String) -> Unit) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        pageCount = list.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 30.dp),
        modifier = Modifier
    ) { page ->
        CardItem(list[page].absolutePath, pagerState.currentPage == page) {
            openListener(list[page].absolutePath)
        }
        LaunchedEffect(key1 = pagerState) {
            pagerState.animateScrollToPage(0)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedText(
    hint: String,
    placeHolderRes: Int = R.drawable.baseline_person_24,
    text: String
) {

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        singleLine = true,
        enabled = false,
        value = text,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            disabledTextColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(placeHolderRes),
                contentDescription = "logo",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onValueChange = {

        },
        label = {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SumText(
    hint: String,
    text: String
) {
    OutlinedTextField(
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        visualTransformation = PhoneMaskTransformation("### ### ### ### ### ### ### ###"),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_attach_money_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "logo"
            )
        },

        singleLine = true,
        value = text,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            disabledTextColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        onValueChange = {

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberText(
    modifier: Modifier,
    hint: String,
    text: String
) {
    OutlinedTextField(
        enabled = false,
        modifier = modifier

            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        visualTransformation = PhoneMaskTransformation("####(##)-###-##-##"),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_phone_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "logo"
            )
        },

        singleLine = true,
        value = text,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            disabledTextColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        onValueChange = {

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        label = {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
    )


}