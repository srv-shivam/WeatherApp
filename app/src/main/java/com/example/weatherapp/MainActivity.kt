package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                Toast.makeText(this, "${response.get("weather")}", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }
        )


        queue.add(jsonObjectRequest)
    }
}