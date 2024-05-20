package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.model.Weather.Coord
import com.example.weatherapp.model.Weather.CurrentWeatherResponse
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.HttpsURLConnection


//import com.example.weatherapp.model.Weather.CurrentWeatherResponse
//import com.google.gson.Gson
//import org.json.JSONObject
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale


class MainActivity : AppCompatActivity() {

    // Declare necessary variables (eg. city, API )
    private var city: String = ""
    private var apiKey: String = "c3bedf58e027becd738ffc70e92c4e09"
    private lateinit var coord: Coord
    private lateinit var currentWeatherResponse: CurrentWeatherResponse

    private lateinit var inputCity: EditText
    private lateinit var searchBtn: Button
    private lateinit var infoContainer: RelativeLayout
    private lateinit var errorScreen: TextView


    // TODO
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                            this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                            Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call the superclass implementation
        enableEdgeToEdge()
        // Set the content view for this activity, specifying the layout resource to be used
        setContentView(R.layout.activity_main)

        // Initialize the view components by finding it by its ID in the layout
        // Try to create containers that will help in hiding and unhiding of layouts based on responses
        infoContainer = findViewById(R.id.info_container)
        errorScreen = findViewById(R.id.error)
        inputCity = findViewById(R.id.input_city)
        searchBtn = findViewById(R.id.search)

        // Initially hide the weather information and error message views
        infoContainer.visibility = RelativeLayout.GONE
        coord = Coord()

        // Set an OnClickListener on the search button
        // Inside the OnClickListener Event do the following
        // Retrieve the city name from the EditText
        searchBtn.setOnClickListener {
            coord = Coord()
            city = inputCity.text.toString()
            if (city == "") {
                infoContainer.visibility = RelativeLayout.GONE
                errorScreen.text = "Please Enter City Name"
                errorScreen.visibility = TextView.VISIBLE
            } else {
                val urlString = "https://api.openweathermap.org/data/2.5/weather?q=${city}&units=metric&appid=${apiKey}"
                fetchWeatherData(urlString).start()
            }
            // Dismiss the keyboard so it doesn't cover the UI
            dismissKeyboard()
        }
    }


    private fun dismissKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Check if no view has focus:
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    // TODO
    // Write a function to fetch weather Data
    // Make sure you use background Thread

    /**
     * Fetches weather data from a specified URL and processes the response.
     *
     * @param urlString The URL string from which the weather data is to be fetched.
     * @return A thread that, when started, performs the network request and data processing.
     */
    fun fetchWeatherData(urlString: String): Thread {
        // Create and return a new Thread object to handle the network operation
        return Thread {
            // Create a Gson object for JSON parsing
            val jsonParser = Gson()

            // Initialize a URL object from the string URL provided
            val url = URL(urlString)

            // Open a URL connection and cast it to HttpURLConnection
            val connection = url.openConnection() as HttpsURLConnection

            // Check if the response from the connection is successful (HTTP 200 OK)
            if (connection.responseCode == 200) {
                // Logging success message with the retrieved data
                Log.d("WEATHER FETCH", "Getting Weather Response")
                // If successful, read the stream from the connection
                // Parse the JSON response into a CurrentWeatherResponse object using Gson
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = jsonParser.fromJson(inputStreamReader, CurrentWeatherResponse::class.java)
                println(SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(response.dt*1000)))
                println(response)

                // Update the global weather object
                currentWeatherResponse = response

                // Update the UI to reflect the fetched data
                updateInfoScreen()

                // Close the BufferedReader
                inputStreamReader.close()
                inputSystem.close()

            } else {
                // If connection failed, log the failure and response code
                val errorSystem = connection.errorStream
                val errorStreamReader = InputStreamReader(errorSystem, "UTF-8")
                val error = jsonParser.fromJson(errorStreamReader, Error::class.java)
                Log.d("CONNECTION ERROR", error.message)
                // Update the UI to show an error message
                updateErrorScreen(error)
                errorStreamReader.close()
                errorSystem.close()
            }
        }
    }

    // TODO
    // Write a function to update Error Screen
    // Make sure you update UI on main Thread
    // Hint:  runOnUiThread {
    //            kotlin.run {
    //              Write update code
    //          }
    //        }
    private fun updateInfoScreen() {
        runOnUiThread {
            kotlin.run {
                println(currentWeatherResponse)

                findViewById<TextView>(R.id.city).text = "${currentWeatherResponse.name}, ${currentWeatherResponse.sys.country}"
                findViewById<TextView>(R.id.update_time).text =
                    "Updated at: ${SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.ENGLISH).format(Date(currentWeatherResponse.dt*1000))}"

                findViewById<TextView>(R.id.condition).text = currentWeatherResponse.weather[0].main
                findViewById<TextView>(R.id.current_temp).text = "${currentWeatherResponse.main.temp}°C"
                findViewById<TextView>(R.id.min_temp).text = "Min Temp: ${currentWeatherResponse.main.tempMin}°C"
                findViewById<TextView>(R.id.max_temp).text = "Max Temp: ${currentWeatherResponse.main.tempMax}°C"

                val sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeatherResponse.sys.sunrise*1000))
                val sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentWeatherResponse.sys.sunset*1000))
                findViewById<TextView>(R.id.sunrise).text = sunrise
                findViewById<TextView>(R.id.sunset).text = sunset

                findViewById<TextView>(R.id.wind).text = currentWeatherResponse.wind.speed.toString()
                findViewById<TextView>(R.id.pressure).text = currentWeatherResponse.main.pressure
                findViewById<TextView>(R.id.humidity).text = currentWeatherResponse.main.humidity

                infoContainer.visibility = RelativeLayout.VISIBLE
                errorScreen.visibility = TextView.GONE
            }
        }
    }

    // TODO
    // Write a function to update Error Screen
    // Make sure you update UI on main Thread
    // Hint:  runOnUiThread {
    //            kotlin.run {
    //              Write update code
    //          }
    //        }
    private fun updateErrorScreen(error: Error?) {
        runOnUiThread {
            kotlin.run {
                infoContainer.visibility = RelativeLayout.GONE
                errorScreen.visibility = TextView.VISIBLE
                errorScreen.text = "Something went wrong"
            }
        }
    }
}
