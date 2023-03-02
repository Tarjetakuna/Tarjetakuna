package com.github.bjolidon.bootcamp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BoredActivity : AppCompatActivity() {

    lateinit var boredTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bored)

        // textview to display the activity
        boredTextView = findViewById<TextView>(R.id.bored_txtview)
        val soBoredBtn = findViewById<Button>(R.id.so_bored_btn)
        val notBoredBtn = findViewById<Button>(R.id.not_bored_btn)

        // set soBored button to get new bored information
        soBoredBtn.setOnClickListener {
            getBoredInformation()
        }

        // set notBored button to go back to main activity
        notBoredBtn.setOnClickListener {
            finish()
        }

    }

    // function to get bored information
    private fun getBoredInformation() {
        // set textview to loading
        boredTextView.text = getString(R.string.txt_getting_bored)

        // building request to API to get bored information
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val boredApi = retrofit.create(BoredApi::class.java)

        // making the request
        boredApi.getActivity().enqueue(object : Callback<DataBoredActivity> {
            override fun onResponse(call: Call<DataBoredActivity>, response: Response<DataBoredActivity>) {
                // Handle the response
                if (response.isSuccessful) {
                    val boredActivity = response.body()
                    if (boredActivity != null) {
                        boredTextView.text = "you can do : " + boredActivity.activity + "\n" +
                                "with " + boredActivity.participants + " people\n" +
                                "price " + boredActivity.price + " out of [0, 1] zero being free\n" +
                                "accessibility : " + boredActivity.accessibility + " out of [0.0-1.0] zero being the most accessible\n" +
                                "type : " + boredActivity.type + "\n" +
                                "link : " + boredActivity.link + "\n" +
                                "key : " + boredActivity.key
                    }
                    else {
                        boredTextView.text = getString(R.string.txt_error_msg, "no data")
                    }
                }
                else {
                    boredTextView.text = getString(R.string.txt_error_msg, response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<DataBoredActivity>, t: Throwable) {
                // Handle the error
                boredTextView.text = getString(R.string.txt_error_msg,t.message)
            }
        })

    }

}

// {"activity":"Back up important computer files","type":"busywork","participants":1,"price":0.2,"link":"","key":"9081214","accessibility":0.2}
data class DataBoredActivity(
    val activity: String,
    val accessibility: Double,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String,
)

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<DataBoredActivity>
}