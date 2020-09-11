package com.mp2.bhojanam.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mp2.bhojanam.R
import java.util.*

class SplashActivity : AppCompatActivity() {

    val MULTIPLE_PERMISSIONS = 123
    var permissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        progressBar = findViewById(R.id.progressBar)

        Handler().postDelayed(Runnable {
            if (checkPermissions()) {
                getCallDetails()
            }
        },3000)

    }
    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(applicationContext, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            progressBar.visibility = View.GONE
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray
     ) {
        when (requestCode) {
             MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // permissions granted.
                    getCallDetails() // Now you call here what ever you want :)
                } else {
                    Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
                return
            }
        }
    }

    private fun getCallDetails() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
