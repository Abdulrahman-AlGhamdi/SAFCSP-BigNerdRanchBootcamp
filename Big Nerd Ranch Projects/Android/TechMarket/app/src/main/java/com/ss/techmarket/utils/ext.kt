package com.ss.techmarket.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Snackbar.changeFont(context: Context) {
    val snack =
        view.findViewById(R.id.snackbar_text) as TextView
    snack.typeface = ResourcesCompat.getFont(context, R.font.quest_regular)
}

fun ImageLoader.fetchPhoto(
    url: Any, icon: Boolean,
    context: Context,
    lifecycleOwner: LifecycleOwner? = null,
    onError: ((Drawable?) -> Unit)? = null,
    onSuccess: (Drawable) -> Unit,
    coroutineScope: CoroutineScope =
        CoroutineScope(Dispatchers.Main)
): Job {

    return coroutineScope.launch {
        val imageKey = if (icon) {
            val key = "${url}_icon"
            MemoryCache.Key.invoke(key)
        } else {
            MemoryCache.Key.invoke(url.toString())
        }
        if (this@fetchPhoto.memoryCache[imageKey] == null) {
            val imageRequest = ImageRequest.Builder(context)
                .data(url)
                .lifecycle(lifecycleOwner)
                .memoryCacheKey(imageKey)
                .also {
                    if (icon) {
                        it.transformations(CircleCropTransformation())
                    }
                }
                .target(
                    onStart = {
                        // do nothing
                    },
                    onError = { error ->
                        if (onError != null) {
                            onError(error)
                        }
                    },
                    onSuccess = { drawable ->
                        onSuccess(drawable)
                    }
                ).build()
            this@fetchPhoto.enqueue(imageRequest)
        } else {
            val bitmap = this@fetchPhoto.memoryCache[imageKey]
            val drawable = BitmapDrawable(context.resources, bitmap)
            onSuccess(drawable)
        }
    }
}