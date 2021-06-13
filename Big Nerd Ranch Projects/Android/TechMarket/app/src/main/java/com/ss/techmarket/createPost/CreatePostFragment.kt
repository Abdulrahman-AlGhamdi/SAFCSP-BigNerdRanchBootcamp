package com.ss.techmarket.createPost

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.ss.techmarket.R
import com.ss.techmarket.databinding.FragmentCreatePostBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont
import gun0912.tedbottompicker.TedBottomPicker


class CreatePostFragment : BottomSheetDialogFragment(), NetworkListener {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var createPostViewModel: CreatePostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        init()
        pickImages()
        createPost()

        return binding.root
    }

    private fun init() {
        createPostViewModel = ViewModelProvider(this)[CreatePostViewModel::class.java]
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun pickImages() {
        binding.pickImage.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                pickImagesFromGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setDeniedMessage("Please turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).check()
    }

    private fun pickImagesFromGallery() {
        TedBottomPicker.with(requireActivity())
            .setPeekHeight(1600)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .showMultiImage {
                if (it != null && it.isNotEmpty()) {
                    createPostViewModel.promotionList.clear()
                    createPostViewModel.promotionList.addAll(it)
                    binding.recycler.adapter =
                        CreatePostImageAdapter(createPostViewModel.promotionList)
                }
            }
    }

    private fun createPost() {
        binding.add.setOnClickListener {
            when {
                binding.title.text.isBlank() -> displaySnackBar(getString(R.string.must_add_title))
                binding.price.text.isBlank() -> displaySnackBar(getString(R.string.must_add_price))
                createPostViewModel.promotionList.isEmpty() -> displaySnackBar(getString(R.string.must_pick_images))
                else -> getImages()
            }
        }
    }

    private fun getImages() {
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()
        val price = binding.price.text.toString()
        /* TODO("REPLACE THESE VALUES BY GETTING THEM FROM USER")*/
        val category: Int = 1                  /*TODO("THIS ONE")*/
        val city: Int = 1                  /*TODO("AND THIS ONE")*/
        val tags: List<Int> = emptyList()  /*TODO("AND THIS ONE")*/
        /* TODO("REPLACE THESE VALUES BY GETTING THEM FROM USER")*/

        val arg = productJsonObject(
            title,
            description,
            price,
            createPostViewModel.promotionList,
            category,
            city,
            tags
        )

        dismiss()

        val action =
            CreatePostFragmentDirections.actionCreatePostFragmentToProgressDialogFragment(arg)
        findNavController().navigate(action)
    }

    override fun onBadRequest() {
        displaySnackBar("Network Bad Request")
    }

    override fun onGatewayTimeOut() {
        displaySnackBar("Connection Time Out")
    }

    override fun onClientTimeOut() {
        displaySnackBar("Connection Time Out")
    }

    override fun onUnauthorized() {
        displaySnackBar("Unauthorized User")
    }

    override fun onInternalError() {
        displaySnackBar("Network Internal Error")
    }

    private fun displaySnackBar(message: String) {
        val snack = Snackbar.make(requireDialog().window?.decorView!!, message, Snackbar.LENGTH_SHORT)
        snack.changeFont(requireContext())
        snack.show()
    }

    override fun onStart() {
        super.onStart()
        createPostViewModel.repository.addNetworkListener(this)
    }

    override fun onStop() {
        super.onStop()
        createPostViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}