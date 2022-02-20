package com.example.weatherapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = Color.parseColor("#1383C3")

        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        getJasonData(lat, long)
    }

    private fun getJasonData(lat: String?, long: String?) {

        val API_KEY = "0894086387bb2129ab3fe68255f75a73"
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun setValues(response: JSONObject) {
        tvCityName.text = response.getString("name")
        val lat = response.getJSONObject("coord").getString("lat")
        val long = response.getJSONObject("coord").getString("lon")
        tvCoordinates.text = "$lat , $long"
        tvWeather.text = response.getJSONArray("weather").getJSONObject(0).getString("main")

        val mainWeather = response.getJSONObject("main")

        var tempTemperature = mainWeather.getString("temp")
        tempTemperature = (((tempTemperature).toFloat() - 273.15).toInt()).toString()
        tvTemperature.text = "$tempTemperature°C"

        var minTemperature = mainWeather.getString("temp_min")
        minTemperature = (floor((minTemperature).toFloat() - 273.15).toInt()).toString()
        tvMinTemperature.text = "Min: $minTemperature°C"

        var maxTemperature = mainWeather.getString("temp_max")
        maxTemperature = (ceil((maxTemperature).toFloat() - 273.15).toInt()).toString()
        tvMaxTemperature.text = "Max: $maxTemperature°C"

        tvPressure.text = mainWeather.getString("pressure")
        tvHumidity.text = "${mainWeather.getString("humidity")}%"

        val windValues = response.getJSONObject("wind")
        tvWindSpeed.text = windValues.getString("speed")
        tvDegree.text = "Degree: ${windValues.getString("deg")}°"
    }
}