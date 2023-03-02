package com.github.bjolidon.bootcamp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class BoredActivity : AppCompatActivity() {

    lateinit var boredTextView: TextView
    lateinit var db: BoredActivityDatabase
    lateinit var boredActivityDao: BoredActivityDao

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

        // starting the db
        db = Room.databaseBuilder(
            applicationContext,
            BoredActivityDatabase::class.java, "boredActivityDatabase"
        ).build()
        boredActivityDao = db.boredActivityDao()
    }

    // function to get bored information
    private fun getBoredInformationWEB() {
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
                        displayBoredActivity(boredActivity)
                        saveBoredActivity(boredActivity)
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

    private fun saveBoredActivity(boredActivity: DataBoredActivity) {
        runBlocking { launch(Dispatchers.IO) { saveBoredActivityDB(boredActivity) }}
    }

    private suspend fun saveBoredActivityDB(boredActivity: DataBoredActivity) {
        // save response to db
        boredActivityDao.insert(
            BoredActivityEntity(
                boredActivity.key,
                boredActivity.activity,
                boredActivity.accessibility,
                boredActivity.type,
                boredActivity.participants,
                boredActivity.price,
                boredActivity.link
            )
        )
    }

    private fun displayBoredActivity(boredActivity: DataBoredActivity, fromDB: Boolean = false) {
        val fromDbTxt = if (fromDB) "No internet but \n\n" else ""
        boredTextView.text = fromDbTxt + "you can do : " + boredActivity.activity + "\n" +
                "with " + boredActivity.participants + " people\n" +
                "price " + boredActivity.price + " out of [0, 1] zero being free\n" +
                "accessibility : " + boredActivity.accessibility + " out of [0.0-1.0] zero being the most accessible\n" +
                "type : " + boredActivity.type + "\n" +
                "link : " + boredActivity.link.ifEmpty { "no link" } + "\n" +
                "key : " + boredActivity.key
    }

    private fun getBoredInformationCached(){
        // set textview to loading
        boredTextView.text = getString(R.string.txt_getting_bored)

        runBlocking { launch(Dispatchers.IO) { getBoredInformationDB() }}

    }

    private suspend fun getBoredInformationDB() {
        // get data from db
        val boredActivities = boredActivityDao.getAll()
        val boredActivity = boredActivities.random()

        // display response
        boredTextView.text = "No internet but \n\nyou can do : " + boredActivity.activity + "\n" +
                "with " + boredActivity.participants + " people\n" +
                "price " + boredActivity.price + " out of [0, 1] zero being free\n" +
                "accessibility : " + boredActivity.accessibility + " out of [0.0-1.0] zero being the most accessible\n" +
                "type : " + boredActivity.type + "\n" +
                "link : " + boredActivity.link?.ifEmpty { "no link" } + "\n" +
                "key : " + boredActivity.key
    }

    private fun getBoredInformation() {
        // check if network is available
        if (isNetworkAvailable(this)) {
            getBoredInformationWEB()
        }
        else {
            getBoredInformationCached()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device that are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

}

// Webapi: https://www.boredapi.com/ dataclass and api interface
// {"activity":"Back up important computer files","type":"busywork","participants":1,"price":0.2,"link":"","key":"9081214","accessibility":0.2}
data class DataBoredActivity(
    val activity: String,
    val accessibility: Double,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: Int,
)

interface BoredApi {
    @GET("activity")
    fun getActivity(): Call<DataBoredActivity>
}

// Room database
@Entity
data class BoredActivityEntity(
    @PrimaryKey val key: Int,
    @ColumnInfo(name = "activity") val activity: String?,
    @ColumnInfo(name = "accessibility") val accessibility: Double?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "participants") val participants: Int?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "link") val link: String?
)

@Dao
interface BoredActivityDao {
    @Query("SELECT * FROM BoredActivityEntity")
    fun getAll(): List<BoredActivityEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg boredActivityEntity: BoredActivityEntity)

    @Delete
    fun delete(boredActivityEntity: BoredActivityEntity)
}

@Database(entities = [BoredActivityEntity::class], version = 1)
abstract class BoredActivityDatabase : RoomDatabase() {
    abstract fun boredActivityDao(): BoredActivityDao
}
