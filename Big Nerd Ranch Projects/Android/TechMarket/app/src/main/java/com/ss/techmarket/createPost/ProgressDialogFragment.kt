package com.ss.techmarket.createPost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.ss.techmarket.databinding.FragmentProgressDialogBinding
import org.json.JSONArray
import org.json.JSONObject

class ProgressDialogFragment : DialogFragment() {

    private var _binding: FragmentProgressDialogBinding? = null
    private val binding get() = _binding!!
    private val args: ProgressDialogFragmentArgs by navArgs()
    private lateinit var createPostViewModel: CreatePostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createPostViewModel = CreatePostViewModel()
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgressDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUpload()

        binding.retryButton.setOnClickListener {
            binding.progressContainer.visibility = View.VISIBLE
            binding.retryContainer.visibility = View.GONE
            startUpload()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun startUpload() {
        val jsonObject = JSONObject(args.jsonObject)
        val title = jsonObject.getString("title")
        val description = jsonObject.getString("description")
        val price = jsonObject.getString("price")
        val category = jsonObject.getInt("category_id")
        val city = jsonObject.getInt("city")
        val tagsJsonArray = jsonObject.getJSONArray("tags")
        val tags = mutableListOf<Int>()
        for (i in 0 until tagsJsonArray.length()) {
            tags += tagsJsonArray.getInt(i)
        }
        val imagesJsonArray = jsonObject.getJSONArray("images")
        val uriList = mutableListOf<Uri>()
        for (i in 0 until imagesJsonArray.length()) {
            uriList += Uri.parse(imagesJsonArray.getString(i))
        }
        val onFinishedCallback: (JSONArray) -> Unit = { array ->
            val response = productRequestBody(
                title,
                description,
                price,
                array,
                category,
                city,
                tags
            )
            createPostViewModel.createPost(response)
            dismiss()
        }

        val onErrorCallback: () -> Unit = {
            binding.progressContainer.visibility = View.GONE
            binding.retryContainer.visibility = View.VISIBLE
        }

        val progressCallback: (Int) -> Unit = { progress ->
            binding.uploadProgressBar.progress = progress
        }

        uploadImages(
            uriList,
            requireContext(),
            onFinishedCallback,
            progressCallback,
            onErrorCallback
        )
    }
}