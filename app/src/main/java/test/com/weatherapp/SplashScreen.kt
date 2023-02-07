package test.com.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CallLog.Locations
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class SplashScreen : AppCompatActivity() {
    lateinit var mfusedlocatin: FusedLocationProviderClient
    private  var myRequestCode=1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // for request location permission
        mfusedlocatin =LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

    }

    //User can Create Problem this type
    // 1. location permission -> deny
    // 2. location deny form setting
    // 3. gps off
    // 4. so we can take the permission while first time open the app then after next operation perform

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (Checkpermission()){
            if (LocationEnable()){
                mfusedlocatin.lastLocation.addOnCompleteListener{
                    task->
                    var location: Location?=task.result
                    if (location==null){
                        NewLocation()
                    }
                    else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },3000)

//                        Log.d("Location",location.longitude.toString())
//                        Toast.makeText(this,location.longitude.toString(),Toast.LENGTH_LONG).show()
//                        Toast.makeText(this,location.latitude.toString(),Toast.LENGTH_LONG).show()

                    }
                }
            }
            else{
                Toast.makeText(this,"Please turn on your GPS location",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() { //here may be create a problem 1h:1m
        var locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates = 1
        mfusedlocatin = LocationServices.getFusedLocationProviderClient(this)
        mfusedlocatin.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper())
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation : Location? = p0.lastLocation
        }
    }

    private fun LocationEnable(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
            ,android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    private fun Checkpermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
            return false

    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == myRequestCode){
            if (grantResults.isEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }
}