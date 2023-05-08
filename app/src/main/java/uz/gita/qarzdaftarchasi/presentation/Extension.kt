package uz.gita.qarzdaftarchasi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.gita.qarzdaftarchasi.R
import uz.gita.qarzdaftarchasi.ui.theme.ErrorColor
import uz.gita.qarzdaftarchasi.utils.PhoneMaskTransformation

@Composable
fun Button(text: String, onClick: () -> Unit) {
    Button(
        onClick,
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            color = Color.White,
//            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedEditText(
    hint: String,
    maxLength: Int,
    errorMessage: String,
    placeHolderRes: Int = R.drawable.baseline_person_24,
    onChange: (String) -> Unit
) {
    val isError = errorMessage.isNotEmpty()
    var textChange by rememberSaveable {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        singleLine = true,
        value = textChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(placeHolderRes),
                contentDescription = "logo",
                tint = if (isError) ErrorColor else MaterialTheme.colorScheme.primary
            )
        },
        onValueChange = {
            if (it.length <= maxLength) {
                textChange = it
                onChange(textChange)
            }
        },
        label = {
            Text(
                text = hint,
                color = if (isError) ErrorColor else MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
        isError = isError
    )
    if (isError) {
        Text(
            text = errorMessage, color = ErrorColor, style = MaterialTheme.typography.bodySmall
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SumEdit(
    hint: String,
    errorMessage: String,
    onChange: (String) -> Unit
) {
    val isError = errorMessage.isNotEmpty()
    var textChange by rememberSaveable {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        visualTransformation = PhoneMaskTransformation("### ### ### ### ### ### ### ###"),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_attach_money_24),
                tint = if (isError) ErrorColor else MaterialTheme.colorScheme.primary,
                contentDescription = "logo"
            )
        },

        singleLine = true,
        value = textChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        onValueChange = {
            if (it.length <= 18) {
                textChange = it
                onChange(textChange)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(
                text = hint,
                color = if (isError) ErrorColor else MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
        isError = isError
    )
    if (isError) {
        Text(
            text = errorMessage, color = ErrorColor, style = MaterialTheme.typography.bodySmall
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberEdit(
    hint: String,
    errorMessage: String,
    onChange: (String) -> Unit
) {
    val isError = errorMessage.isNotEmpty()
    var textChange by rememberSaveable {
        mutableStateOf("+998")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colorScheme.surface),
        visualTransformation = PhoneMaskTransformation("####(##)-###-##-##"),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_phone_24),
                tint =  if (isError) ErrorColor else MaterialTheme.colorScheme.primary,
                contentDescription = "logo"
            )
        },

        singleLine = true,
        value = textChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = ErrorColor,
            errorCursorColor = ErrorColor,
            errorLabelColor = ErrorColor,
            focusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        onValueChange = {
            if (it.length <= 13) {
                textChange = it
                onChange(textChange)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        label = {
            Text(
                text = hint,
                color =  if (isError) ErrorColor else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        maxLines = 1,
        isError = isError
    )
    if (isError) {
        Text(
            text = errorMessage, color = ErrorColor, style = MaterialTheme.typography.bodySmall
        )
    }

}