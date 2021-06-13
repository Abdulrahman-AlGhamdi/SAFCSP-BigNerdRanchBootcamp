package com.ss.techmarket.createPost

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.preprocess.BitmapEncoder
import com.cloudinary.android.preprocess.ImagePreprocessChain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

fun uploadImages(
    uriList: List<Uri>,
    context: Context,
    onFinishedCallback: (JSONArray) -> Unit,
    progressCallback: (Int) -> Unit,
    onErrorCallback: () -> Unit
) {
    val mediaManager = MediaManager.get()

    val imagesJsonArray = JSONArray()
    uriList.forEach {
        mediaManager
            .upload(it)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    //TODO("Not yet implemented")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    Log.d("TAG", "${resultData?.get("secure_url")}")
                    if (resultData != null) {
                        imagesJsonArray.put(resultData["secure_url"])
                    }

                    val progress =
                        ((imagesJsonArray.length() / uriList.size.toFloat()) * 100).toInt()
                    progressCallback(progress)

                    if (imagesJsonArray.length() == uriList.size) {
                        onFinishedCallback(imagesJsonArray)
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onErrorCallback()
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    //TODO("Not yet implemented")
                }

            })
            .preprocess(
                ImagePreprocessChain
                    .limitDimensionsChain(800, 800)
                    .saveWith(BitmapEncoder(BitmapEncoder.Format.PNG, 100))
            )
            .startNow(context)
    }
}

fun productRequestBody(
    title: String,
    description: String,
    price: String,
    imagesJsonArray: JSONArray,
    category: Int,
    city: Int,
    tags: List<Int>
): RequestBody {

    val tagsJsonArray = JSONArray()
    tags.forEach { tagsJsonArray.put(it) }

    val jsonObject = JSONObject().apply {
        put("title", title)
        put("description", description)
        put("price", price)
        put("images", imagesJsonArray.toString())
        put("category_id", category)
        put("city", city)
        put("tags", tagsJsonArray)
    }

    return jsonObject.toString().toRequestBody(
        "application/json".toMediaTypeOrNull()
    )
}

fun productJsonObject(
    title: String,
    description: String,
    price: String,
    uriList: List<Uri>,
    category: Int,
    city: Int,
    tags: List<Int>
): String {
    val imageJsonArray = JSONArray()
    uriList.forEach { imageJsonArray.put(it) }

    val tagsJsonArray = JSONArray()
    tags.forEach { tagsJsonArray.put(it) }

    return JSONObject().apply {
        put("title", title)
        put("description", description)
        put("price", price)
        put("images", imageJsonArray)
        put("category_id", category)
        put("city", city)
        put("tags", tagsJsonArray)
    }.toString()
}