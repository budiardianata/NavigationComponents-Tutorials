package com.smarttoolfactory.tutorial7_2bnv_viewpager2_complexarchitecture

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smarttoolfactory.tutorial7_2bnv_viewpager2_complexarchitecture.databinding.ActivityMainBinding
import com.smarttoolfactory.tutorial7_2bnv_viewpager2_complexarchitecture.util.Event
import com.smarttoolfactory.tutorial7_2bnv_viewpager2_complexarchitecture.viewmodel.AppbarViewModel

class MainActivity : AppCompatActivity() {

    private val appbarViewModel by viewModels<AppbarViewModel>()

    private lateinit var dataBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            val fragments = supportFragmentManager.fragments
            val fragmentCount = fragments.size


            Toast.makeText(
                this,
                "MainActivity backStackEntryCount: $backStackEntryCount, fragmentCount: $fragmentCount, fragments: $fragments",
                Toast.LENGTH_SHORT
            ).show()
        }


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(
            R.navigation.nav_graph_view_pager,
            R.navigation.nav_graph_dashboard,
            R.navigation.nav_graph_notification
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            val appBarConfig = AppBarConfiguration(navController.graph)
            dataBinding.toolbar.setupWithNavController(navController, appBarConfig)
        })

        appbarViewModel.currentNavController.observe(this, Observer { it ->

            it?.let { event: Event<NavController> ->
                event.getContentIfNotHandled()?.let { navController ->
                    val appBarConfig = AppBarConfiguration(navController.graph)
                    dataBinding.toolbar.setupWithNavController(navController, appBarConfig)
                }
            }
        })
    }

}
