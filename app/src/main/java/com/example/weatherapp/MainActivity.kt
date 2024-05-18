package com.example.weatherapp

import android.app.DownloadManager.Request
import android.content.Context
import android.os.Bundle
import android.text.Editable
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
import com.example.weatherapp.model.Weather.SummaryResponse
import com.example.weatherapp.model.Weather.WeatherObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.utils.io.core.Input
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
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
    private lateinit var weatherObject: WeatherObject

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
            weatherObject = WeatherObject(null, null)
            city = inputCity.text.toString()
            if (city == "") {
                infoContainer.visibility = RelativeLayout.GONE
                errorScreen.text = "Please Enter City Name"
                errorScreen.visibility = TextView.VISIBLE
            } else {
                val latch = CountDownLatch(1)
                fetchGeoLocation(city, latch).start()
                latch.await(1000, TimeUnit.MILLISECONDS)
                Log.d("Check if coordination updated correctly", "get ${coord}")

                var urlString = ""
                if (coord.name != "") {
                    // Check if the city name is not empty
                    // Build the URL for fetching weather data using the city name
                    urlString = "https://api.openweathermap.org/data/3.0/onecall?lat=${coord.lat}&lon=${coord.lon}&appid=$apiKey&units=metric"

                    // Fetch weather data from the constructed URL in a separate thread
                    // Update the UI to show an error message if no city name is entered
                    fetchWeatherData(urlString).start()
                }
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

    fun fetchGeoLocation(city: String, latch: CountDownLatch): Thread {
        // Create and return a new Thread object to handle the network operation
        val urlString = "https://api.openweathermap.org/geo/1.0/direct?q=${city}&appid=${apiKey}"
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
                Log.d("LOCATION LOG", "Getting Location Response for $city")
                // If successful, read the stream from the connection
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val type = object : TypeToken<List<Coord>>() {}.type
                val response: List<Coord> = jsonParser.fromJson(inputStreamReader, type)

                if (response.isEmpty()) {
                    updateErrorScreen(null)
                } else {
                    println("Get the following values: (${response[0].lon}, ${response[0].lat})")
                    coord = Coord(response[0].name, response[0].state, response[0].lat, response[0].lon)
                }
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
            latch.countDown()
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
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())

            // Initialize a URL object from the string URL provided
            val url = URL(urlString)
            val summaryURL = URL("https://api.openweathermap.org/data/3.0/onecall/day_summary?lat=${coord.lat}&lon=${coord.lon}&date=$today&appid=$apiKey&units=metric")

            // Open a URL connection and cast it to HttpURLConnection
            val connection = url.openConnection() as HttpsURLConnection
            val summaryConnection = summaryURL.openConnection() as HttpsURLConnection

            // Check if the response from the connection is successful (HTTP 200 OK)
            if (connection.responseCode == 200 && summaryConnection.responseCode == 200) {
                // Logging success message with the retrieved data
                Log.d("WEATHER FETCH", "Getting Weather Response for ${coord.name}")
                // If successful, read the stream from the connection
                // Parse the JSON response into a CurrentWeatherResponse object using Gson
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val response = jsonParser.fromJson(inputStreamReader, CurrentWeatherResponse::class.java)
                println(SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date(response.current.dt*1000)))
                println(response)

                val inputSystemSummary = summaryConnection.inputStream
                val inputStreamSummaryReader = InputStreamReader(inputSystemSummary, "UTF-8")
                val summaryResponse = jsonParser.fromJson(inputStreamSummaryReader, SummaryResponse::class.java)
                println(summaryResponse)

                // Update the global weather object
                weatherObject = WeatherObject(response, summaryResponse)

                // Update the UI to reflect the fetched data
                updateInfoScreen()

                // Close the BufferedReader
                inputStreamReader.close()
                inputSystem.close()

                inputStreamSummaryReader.close()
                inputSystemSummary.close()

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
                if (weatherObject.currentWeatherResponse != null && weatherObject.summaryResponse != null) {
                    println(weatherObject)

                    findViewById<TextView>(R.id.city).text = "${coord.name}, ${coord.state}"
                    findViewById<TextView>(R.id.update_time).text = "Updated at: ${SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.ENGLISH).format(Date(weatherObject.currentWeatherResponse!!.current.dt*1000))}"
                    findViewById<TextView>(R.id.condition).text = weatherObject.currentWeatherResponse!!.current.weather.first().main
                    findViewById<TextView>(R.id.current_temp).text = "${weatherObject.currentWeatherResponse!!.current.temp}°C"
                    findViewById<TextView>(R.id.min_temp).text = "Min Temp: ${weatherObject.summaryResponse!!.temperature.min}°C"
                    findViewById<TextView>(R.id.max_temp).text = "Max Temp: ${weatherObject.summaryResponse!!.temperature.max}°C"

                    val sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherObject.currentWeatherResponse!!.current.sunrise*1000))
                    val sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(weatherObject.currentWeatherResponse!!.current.sunset*1000))
                    findViewById<TextView>(R.id.sunrise).text = sunrise
                    findViewById<TextView>(R.id.sunset).text = sunset

                    findViewById<TextView>(R.id.wind).text = weatherObject.currentWeatherResponse!!.current.windSpeed
                    findViewById<TextView>(R.id.pressure).text = weatherObject.currentWeatherResponse!!.current.pressure
                    findViewById<TextView>(R.id.humidity).text = weatherObject.currentWeatherResponse!!.current.humidity

                    infoContainer.visibility = RelativeLayout.VISIBLE
                    errorScreen.visibility = TextView.GONE
                }
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
