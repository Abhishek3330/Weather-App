package test.com.weatherapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import test.com.weatherapp.databinding.ActivityMainBinding
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")
        //Toast.makeText(this, long+" "+lat, Toast.LENGTH_LONG).show()

        // Set States Bar color
        window.statusBarColor = Color.parseColor("#FF15324A")

        getJesondata(lat, long)
    }

    private fun getJesondata(lat: String?, long: String?) {
        val API_KEY="d676ed172388fd9e6fdef3e771df8723"
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"

       // Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener{ response ->
                // Display the first 500 characters of the response string.
                // Toast.makeText(this, response.toString(),Toast.LENGTH_LONG).show()

                setValue(response)

            },
            Response.ErrorListener {
                Toast.makeText(this,"Error in Fetching data",Toast.LENGTH_SHORT).show()
            })

       // Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }

    private fun setValue(response: JSONObject) {
        binding.city.text = response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long = response.getJSONObject("coord").getString("lon")
        binding.cordinates.text = "${lat},${long}"
        binding.weather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")

        var temper = response.getJSONObject("main").getString("temp")
        // change temperature kelwin to degree to String
        temper = ((((temper).toFloat()-273.15)).toInt()).toString()
        binding.temp.text = "${temper}°C"

        var mintemp = response.getJSONObject("main").getString("temp_min")
        // change temperature kelwin to degree to String
        mintemp = ((((mintemp).toFloat()-273.15)).toInt()).toString()
        binding.minTemp.text = "${mintemp}°C"

        var maxtemp = response.getJSONObject("main").getString("temp_max")
        // change temperature kelwin to degree to String
        maxtemp = ((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        binding.maxTemp.text = "${maxtemp}°C"

        binding.pressure.text = response.getJSONObject("main").getString("pressure")

        binding.humidity.text = response.getJSONObject("main").getString("humidity")+"%"

        binding.wind.text = response.getJSONObject("wind").getString("speed")

        binding.degree.text = "Degree : "+response.getJSONObject("wind").getString("deg")+"°"

        binding.gust.text = "Gust : "+response.getJSONObject("wind").getString("gust")


    }
}