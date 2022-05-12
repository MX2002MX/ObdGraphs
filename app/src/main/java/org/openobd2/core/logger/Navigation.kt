package org.openobd2.core.logger

import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.openobd2.core.logger.bl.datalogger.DataLoggerService
import org.openobd2.core.logger.ui.preferences.PREFERENCE_SCREEN_KEY

internal fun MainActivity.setupNavigationBar() {
    val navView: BottomNavigationView = findViewById(R.id.nav_view)
    val navController = navController()

    val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.navigation_gauge,
            R.id.navigation_graph,
            R.id.navigation_dashboard,
            R.id.navigation_debug,
            R.id.navigation_metrics,
            R.id.navigation_preferences
        )
    )

    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)

    val mainActivityPreferences = getMainActivityPreferences()
    findViewById<BottomNavigationView>(R.id.nav_view).menu.run {
        findItem(R.id.navigation_debug)?.isVisible =
            mainActivityPreferences.showDebugView

        findItem(R.id.navigation_dashboard).isVisible =
            mainActivityPreferences.showDashView

        findItem(R.id.navigation_gauge).isVisible =
            mainActivityPreferences.showGaugeView

        findItem(R.id.navigation_metrics).isVisible =
            mainActivityPreferences.showMetricsView

        findItem(R.id.navigation_graph).isVisible =
            mainActivityPreferences.showGraphView
    }

    navView.selectedItemId = R.id.navigation_gauge
}

internal fun MainActivity.setupNavigationBarButtons() {

    val btnStart: FloatingActionButton = findViewById(R.id.connect_btn)
    btnStart.setOnClickListener {
        Log.i(ACTIVITY_LOGGER_TAG, "Start data logging")
        DataLoggerService.startAction(this)
    }

    val menuButton: FloatingActionButton = findViewById(R.id.menu_btn)
    menuButton.setOnClickListener {
        val pm = PopupMenu(this, menuButton)
        pm.menuInflater.inflate(R.menu.context_menu, pm.menu)

        pm.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.ctx_menu_pids_to_query -> {
                    this.navController()
                        .navigate(
                            R.id.navigation_preferences,
                            bundleOf(PREFERENCE_SCREEN_KEY to "pref.pids.query")
                        )
                }

                R.id.ctx_menu_view_profiles -> {
                    this.navController()
                        .navigate(
                            R.id.navigation_preferences,
                            bundleOf(PREFERENCE_SCREEN_KEY to "pref.profiles")
                        )
                }
                R.id.ctx_menu_view_configuration -> {

                    val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
                    val selectedItemId: Int = bottomNavigationView.selectedItemId
                    val currentView: MenuItem =
                        bottomNavigationView.menu.findItem(selectedItemId)

                    val keyToNavigate = when (currentView.itemId) {
                        R.id.navigation_dashboard -> "pref.dashboard"
                        R.id.navigation_gauge -> "pref.gauge"
                        R.id.navigation_graph -> "pref.graph"
                        R.id.navigation_metrics -> "pref.metrics"
                        else -> "pref.root"
                    }

                    this.navController()
                        .navigate(
                            R.id.navigation_preferences,
                            bundleOf(PREFERENCE_SCREEN_KEY to keyToNavigate)
                        )
                }
            }

            true
        }
        pm.show()
    }
}