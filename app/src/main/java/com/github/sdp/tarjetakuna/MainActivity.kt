package com.github.sdp.tarjetakuna

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.sdp.tarjetakuna.databinding.ActivityDrawerBinding
import com.github.sdp.tarjetakuna.extra.ExportCollection
import com.github.sdp.tarjetakuna.extra.Location
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.github.sdp.tarjetakuna.ui.scanner.ScannerFragment
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys.shared_pref_name
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDrawerBinding
    private val sharedPrefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == SharedPreferencesKeys.user_name || key == SharedPreferencesKeys.user_description) {
            updateHeader()
        }
    }

    //To be removed
    private val testCollection = listOf(
        MagicCard(
            "MagicCard",
            "A beautiful card",
            MagicLayout.NORMAL,
            7.0,
            "{5}{W}{W}",
            MagicSet("M15", "Magic 2015"),
            56,
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
        ),
        MagicCard(
            "BestMagicCard",
            "An even more beautiful card",
            MagicLayout.NORMAL,
            7.0,
            "{7}{W}{W}",
            MagicSet("M15", "Magic 2015"),
            56,
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the view
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarDrawer.toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = getNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_browser,
                R.id.nav_scanner,
                R.id.nav_webapi,
                R.id.nav_chats
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Display profile fragment when clicking on the profile icon
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<ImageView>(R.id.authentication_profile_icon).setOnClickListener {
            changeFragment(R.id.nav_profile)
            binding.drawerLayout.closeDrawer(binding.navView)
        }

        // Update the header when the user changes their name or description
        val sharedPref = getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE)
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener)

        updateHeader()
        // ask for location permission + get current location if already granted
        Location.captureCurrentLocation(this)
    }

    /**
     * Change the fragment displayed in the drawer
     */
    fun changeFragment(fragment: Int, args: Bundle? = null) {
        getNavController().navigate(fragment, args)
    }

    /**
     * Navigate up in the navigation drawer
     */
    fun navigateUp() {
        getNavController().navigateUp()
    }

    /**
     * Update the header of the navigation drawer to display the user's name and description
     */
    private fun updateHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val sharedPref = getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE)
        headerView.findViewById<TextView>(R.id.nav_header_name_textview).text =
            sharedPref.getString(
                SharedPreferencesKeys.user_name, getString(R.string.name_entry_hint)
            )
        headerView.findViewById<TextView>(R.id.navHeaderDescriptionText).text =
            sharedPref.getString(
                SharedPreferencesKeys.user_description, getString(R.string.description_entry_hint)
            )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController().navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Get the navigation controller of the drawer
     */
    private fun getNavController(): NavController =
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_drawer) as NavHostFragment).navController

    /**
     * Hide the keyboard if it is visible
     */
    fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

    fun onClick(item: MenuItem) {
        when (item.itemId) {
            R.id.action_export_collection -> {
                ExportCollection.exportCollection(
                    findViewById(R.id.nav_host_fragment_content_drawer),
                    testCollection
                )
            }
        }
    }

    /**
     * When the user grants or denies permissions, the result is passed to the fragment that might have requested them
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle location permission result, if granted, get current location
        Location.captureCurrentLocation(this)

        // handle permission for scanner fragment
        if (requestCode == ScannerFragment.REQUEST_CODE_PERMISSIONS) {
            supportFragmentManager.fragments.forEach {
                it.childFragmentManager.fragments.forEach { it2 ->
                    if (it2 is ScannerFragment) {
                        it2.requestPermissionsResult(
                            requestCode,
                            permissions,
                            grantResults
                        )
                    }
                }
            }
        }
    }

    /**
     * Companion for static variables
     */
    companion object {
        /**
         * Tag for logging
         */
        private const val TAG = "MainActivity"
    }
    
}
