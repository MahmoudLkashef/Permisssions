package com.syncdev.permissions

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.syncdev.permissions.databinding.ActivityMainBinding
import com.syncdev.permissions.utils.Constants

class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLocation.setOnClickListener {
            checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,"Location",Constants.LOCATION_REQUEST_CODE)
        }

        binding.btnCamera.setOnClickListener{
            checkPermission(android.Manifest.permission.CAMERA,"Camera",Constants.CAMERA_REQUEST_CODE)
        }

        binding.btnStorage.setOnClickListener {
            checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,"Storage",Constants.STORAGE_REQUEST_CODE)
        }

        binding.btnMic.setOnClickListener {
            checkPermission(android.Manifest.permission.RECORD_AUDIO,"Mic",Constants.MIC_REQUEST_CODE)
        }

    }

    private fun checkPermission(permission:String,permissionName:String,requestCode:Int){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext,permission)== PackageManager.PERMISSION_GRANTED->
                    Toast.makeText(this, "$permissionName permission granted", Toast.LENGTH_SHORT).show()

                shouldShowRequestPermissionRationale(permission)->showDialog(permission,permissionName,requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun checkPermissionStatus(permissionName:String)
        {
            if(grantResults.isEmpty() || grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "$permissionName permission denied", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "$permissionName permission granted", Toast.LENGTH_SHORT).show()
        }

        when(requestCode){
            Constants.LOCATION_REQUEST_CODE->checkPermissionStatus("Location")
            Constants.STORAGE_REQUEST_CODE->checkPermissionStatus("Storage")
            Constants.CAMERA_REQUEST_CODE->checkPermissionStatus("Camera")
            Constants.MIC_REQUEST_CODE->checkPermissionStatus("Mic")
        }
    }
    private fun showDialog(permission: String, permissionName: String, requestCode: Int) {
        val builder=AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access you $permissionName is required for this application")
            setTitle("Permission Required")
            setPositiveButton("OK"){dialog,which->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission),requestCode)
            }
            setNegativeButton("cancel"){dialog,which->
                dialog.dismiss()
            }
        }
        val dialog=builder.create()
        dialog.show()
    }
}