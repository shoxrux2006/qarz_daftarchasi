package uz.gita.qarzdaftarchasi.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

suspend fun GlideUtil(uri: Uri, @DrawableRes default: Int, context: Context): Flow<Bitmap> =
    callbackFlow {

        Glide.with(context).asBitmap().placeholder(default).load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    trySend(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        awaitClose {
            channel.close()
        }
    }.flowOn(Dispatchers.IO)

