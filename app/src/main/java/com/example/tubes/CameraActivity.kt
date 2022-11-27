package com.example.tubes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import com.vmadalin.easypermissions.EasyPermissions
import java.lang.Exception

class CameraActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks{
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_camera)
        getSupportActionBar()?.hide()
        requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestPermission(){
        EasyPermissions.requestPermissions(this, "Izinkan Kamera Mengakses Aplikasi Ini?",
            200, Manifest.permission.CAMERA)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Akses Didapatkan", Toast.LENGTH_SHORT).show()
        try {
            mCamera = Camera.open()
        }catch (e: Exception) {
            Log.d("Error", "Failed to get Camera" + e.message)
        }
        if(mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{ view: View? -> System.exit(0)}
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Kamera tidak diizinkan", Toast.LENGTH_SHORT).show()
    }
}