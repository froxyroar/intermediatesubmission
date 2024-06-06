package com.dicoding.picodiploma.loginwithanimation.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.BuildConfig
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import kotlinx.coroutines.launch
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private var selectedImageUri: Uri? = null
    private var photoFile: File? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivStoryImage.setImageURI(uri)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoFile != null) {
            selectedImageUri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", photoFile!!)
            binding.ivStoryImage.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(AddStoryViewModel::class.java)

        binding.buttonGallery.setOnClickListener {
            openGallery()
        }

        binding.buttonCamera.setOnClickListener {
            checkAndOpenCamera()
        }

        binding.buttonAdd.setOnClickListener {
            uploadStory()
        }

        viewModel.uploadResult.observe(this) { result ->
            if (result != null) {
                if (result.error == true) {
                    Toast.makeText(this, "Upload failed: ${result.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun openCamera() {
        photoFile = getPhotoFile()
        val fileUri: Uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", photoFile!!)
        takePicture.launch(fileUri)
    }

    private fun getPhotoFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_", ".jpg", storageDir)
    }

    private fun uploadStory() {
        val description = binding.edAddDescription.text.toString()
        val token = intent.getStringExtra("EXTRA_TOKEN") ?: ""

        if (description.isNotEmpty() && selectedImageUri != null) {
            val file = if (selectedImageUri!!.toString().startsWith("content")) {
                getFileFromUri(selectedImageUri!!)
            } else {
                photoFile
            }
            if (file != null) {
                lifecycleScope.launch {
                    viewModel.uploadStory("Bearer $token", file, description)
                }
            }
        } else {
            Toast.makeText(this, "Please provide description and image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getFileFromUri(uri: Uri): File? {
        // Implement this function to convert Uri to File
        return null
    }

    private fun checkAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera()
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 101
    }
}