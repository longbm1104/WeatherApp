package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.model.Weather.CurrentWeatherResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    // Declare necessary variables (eg. city, API )

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


        // Initially hide the weather information and error message views

        // Set an OnClickListener on the search button
        // Inside the OnClickListener Event do the following
        // Retrieve the city name from the EditText

        // Dismiss the keyboard so it doesn't cover the UI
        dismissKeyboard()

        // Check if the city name is not empty
            // Build the URL for fetching weather data using the city name

            // Fetch weather data from the constructed URL in a separate thread
            // Update the UI to show an error message if no city name is entered

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

            // Initialize a URL object from the string URL provided

            // Open a URL connection and cast it to HttpURLConnection

            // Check if the response from the connection is successful (HTTP 200 OK)
            if () {
                // Logging success message with the retrieved data

                // If successful, read the stream from the connection


                // Read the first line of data

                // Close the BufferedReader

                // Parse the JSON response into a CurrentWeatherResponse object using Gson

                // Update the global weather object

                // Update the UI to reflect the fetched data

            } else {
                // If connection failed, log the failure and response code

                // Update the UI to show an error message
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


    // TODO
    // Write a function to update Error Screen
    // Make sure you update UI on main Thread
    // Hint:  runOnUiThread {
    //            kotlin.run {
    //              Write update code
    //          }
    //        }
