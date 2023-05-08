package uz.gita.qarzdaftarchasi.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import uz.gita.qarzdaftarchasi.R
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ComposeFileProvider : FileProvider(
    R.xml.filepath
) {
    companion object {
        var file = File("")
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File(directory, "${System.currentTimeMillis()}.jpg")
            this.file = file
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}


class ImagePicker() {
    private var imageFile: ((File?) -> Unit)? = null
    private var dismissListener: (() -> Unit)? = null
    private var scope: Job? = null

    fun dismissListener(block: () -> Unit) {
        dismissListener = block
    }

    fun captureListener(block: (File?) -> Unit) {
        imageFile = {
            scope?.cancel()
            block(it)
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun TakePicture(
        isShow: Boolean
    ) {
        val context = LocalContext.current
        val provider = ComposeFileProvider
        val file = provider.file
        val uri = provider.getImageUri(context)
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uris ->
                uris?.let {
                    scope = GlobalScope.launch(Dispatchers.IO) {
                        val directory = File(context.cacheDir, "images")
                        directory.mkdirs()
                        val file = File(directory, "${System.currentTimeMillis()}.jpg")
                        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
                        GlideUtil(
                            uri = it,
                            default = R.drawable.baseline_person_24,
                            context
                        ).collectLatest {
                            it.compress(Bitmap.CompressFormat.JPEG, 100, os)
                            os.close()
                            imageFile?.let { it1 -> it1(file) }
                        }
                    }
                }
            })
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    imageFile?.let { it1 -> it1(file) }
                }
            })
        if (isShow) {
            Dialog(onDismissRequest = {
                dismissListener?.let { it() }
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            dismissListener?.let { it() }
                            cameraLauncher.launch(uri)
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(30.dp),
                            painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                            contentDescription = "camera"
                        )
                        Text(text = "Camera")
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            dismissListener?.let { it() }
                            imagePicker.launch("image/*")
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(30.dp),
                            painter = painterResource(id = R.drawable.baseline_image_24),
                            contentDescription = "gallery"
                        )
                        Text(text = "Gallery")
                    }

                }
            }
        }
    }

    @Composable
    fun DeleteImage(file: File?) {

        if (file != null) {
            AlertDialog(
                onDismissRequest = {
                    dismissListener?.let { it() }
                },
                title = { Text(text = "Waring") },
                text = { Text(text = "Are you sure want to delete?") },
                confirmButton = {
                    TextButton(onClick = {
                        imageFile?.let { it(null) }
                        file.delete()
                        dismissListener?.let { it() }
                    }) {
                        Text(text = "Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        dismissListener?.let { it() }
                    }) {
                        Text(text = "Cancel")
                    }
                },
            )
        }
    }
}





