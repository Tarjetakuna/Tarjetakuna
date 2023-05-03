package com.github.sdp.tarjetakuna.ui.scanner

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentScannerBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * This fragment is responsible to take a picture of the card
 */
class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var scannerViewModel: ScannerViewModel

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scannerViewModel = ViewModelProvider(this)[ScannerViewModel::class.java]

        // Code adapted from https://developer.android.com/training/camerax
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // bind the view model to the layout when text or object is detected in image
        scannerViewModel.textDetected.observe(viewLifecycleOwner) {
            binding.scannerTextInImageText.text = it.text
        }

        scannerViewModel.objectDetected.observe(viewLifecycleOwner) {
            binding.scannerObjectInImageText.text = it.text
        }

        binding.scannerScanButton.setOnClickListener {
            takePhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return root
    }

    // Code adapted from https://developer.android.com/training/camerax
    /**
     * Check if all permissions are granted or not
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        val baseContext = this.context ?: return false
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check whether permissions have been granted after the request,
     * replaces the onRequestPermissionsResult method from the activity
     */
    fun requestPermissionsResult(requestCode: Int, perm: Array<String>, results: IntArray) {
        // check that permissions results is for scanner fragment
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // check if all permissions are granted
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // if not, show a toast and navigate back out of this fragment
                Toast.makeText(
                    this.context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO : should be independent of MainActivity
                (this.activity as MainActivity).navigateUp()
            }
        }
    }

    /**
     * Take a photo and save it to the device
     */
    private fun takePhoto() {
        if (!takePicture()) {
            binding.scannerHiddenText.text = "Error taking picture"
            Toast.makeText(this.context, "Error taking picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePicture(): Boolean {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return false
        val context = this.context ?: return false
        val contentResolver = this.activity?.contentResolver ?: return false

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Tarjetakuna-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    val msg = getString(R.string.scanner_photo_failed, exc.message)
                    binding.scannerHiddenText.text = msg
                    view?.let { Snackbar.make(it, msg, Snackbar.LENGTH_LONG).show() }
                    Log.e(TAG, msg, exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = getString(R.string.scanner_photo_saved, output.savedUri)
                    binding.scannerHiddenText.text = msg
                    view?.let { Snackbar.make(it, msg, Snackbar.LENGTH_LONG).show() }
                    Log.d(TAG, msg)
                }
            }
        )
        return true
    }

    /**
     * Start the camera and bind the "preview" = live preview on screen, "takePicture" = save the
     * picture on the device, and "analysis" = text recognition and object detection to be done correctly
     * Bound to the lifecycle of the fragment, so it will be automatically unbound when the fragment is
     * destroyed.
     */
    private fun startCamera() {
        // Code adapted from https://developer.android.com/training/camerax
        val context = this.context ?: return

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview to show the camera preview on screen
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.scannerImagePreview.surfaceProvider)
                }

            // ImageCapture to save photo on device
            imageCapture = ImageCapture.Builder().build()

            // ImageAnalysis to detect text and objects
            // TODO finish the image analyzer
//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also { it.setAnalyzer(cameraExecutor, setupImageAnalyzer()) }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    // TODO finish the image analyzer
//                    imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed while binding the camera to the view", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    // TODO finish the image analyzer
    /**
     * Setup the image analyzer to detect text and objects with proper callbacks
     */
//    private fun setupImageAnalyzer(): ImageAnalyzer {
//        val textDetectedListener: TextDetectedListener = object : TextDetectedListener {
//            override fun callback(text: Text) {
//                scannerViewModel.detectTextSuccess(text)
//            }
//
//            override fun errorCallback(exception: Exception) {
//                scannerViewModel.detectTextError(exception)
//            }
//        }
//
//        val objectDetectedListener: ObjectDetectedListener = object : ObjectDetectedListener {
//            override fun callback(text: String) {
//                scannerViewModel.detectObjectSuccess(text)
//            }
//
//            override fun errorCallback(exception: Exception) {
//                scannerViewModel.detectObjectError(exception)
//            }
//        }
//
//        return ImageAnalyzer(textDetectedListener, objectDetectedListener)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    // Code adapted from https://developer.android.com/training/camerax
    /**
     * Companion object to hold constants
     */
    companion object {
        // Tag for the [Log]
        private const val TAG = "ScannerFragment"

        // Format for the name of the file where the images will be saved
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        // Request code for permissions
        const val REQUEST_CODE_PERMISSIONS = 10

        // Permissions required for the app to run
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
