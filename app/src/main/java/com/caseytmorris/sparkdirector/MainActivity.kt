package com.caseytmorris.sparkdirector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.caseytmorris.sparkdirector.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    companion object {
        const val RC_SIGN_IN = 622
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()


        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        authStateListener = FirebaseAuth.AuthStateListener { authFirebase ->
            val user = authFirebase.currentUser
            if (user != null) {
                //user is signed in
                onSignedInInitialize(user)

            } else {
                //user is not signed in
                onSignedOutCleanup()
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls("http://caseytmorris.com/sparkdirector/terms-of-service.html",
                            "http://caseytmorris.com/sparkdirector/privacy-policy.html")
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                            Arrays.asList<AuthUI.IdpConfig>(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )


            }
        }
    }

    private fun onSignedOutCleanup() {
        //unset username if saved
        //detach listener
        Log.i("Casey","Cleanup on signed out please")
    }

    private fun onSignedInInitialize(currentUser: FirebaseUser) {
        Log.i("Casey","User Uid: ${currentUser.uid}")
        Log.i("Casey","User Display Name: ${currentUser.displayName}")

        //This is when you would attach the event listeners to the firebase stuff
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this@MainActivity, "Sign In Result OK", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this@MainActivity, "Sign In Cancelled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signout_menu_option) {
            signout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun signout() {
        AuthUI.getInstance().signOut(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}