package com.descope.androiddemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.descope.Descope
import com.descope.types.RevokeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    // The simple API layer resides directly in the Activity for brevity and readability
    private val api = Api()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        // ensure the user is logged in
        if (ensureSession()) {
            launchLoginActivity()
            return
        }

        val textView: TextView = findViewById(R.id.text)
        val apiOutput: TextView = findViewById(R.id.api_output)
        val apiButton: Button = findViewById(R.id.api_button)

        textView.text = "Welcome, ${Descope.sessionManager.session!!.user.loginIds.first()}"

        // This code snippet represents a simplified protected API call.
        // It uses the GlobalScope and resides in the activity for brevity
        // and code readability. It is only an example of an operation you
        // might have in a ViewModel or an equivalent layer in your app.
        apiButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                apiOutput.text = ""
                try {
                    val result = withContext(Dispatchers.IO) {
                        try {
                            api.myApiCall()
                        } catch (e: SessionExpired) {
                            throw e
                        } catch (e: Exception) {
                            // handle other errors
                            println("Request failed: $e")
                            false
                        }
                    }
                    apiOutput.text = if (result) "API call succeeded" else "API call failed"
                } catch (e: Exception) {
                    apiOutput.text = "The session has expired - the user needs to re-authenticate"
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_option -> {
                val refreshJwt = Descope.sessionManager.session!!.refreshJwt
                Descope.sessionManager.clearSession()
                GlobalScope.launch {
                    try {
                        Descope.auth.revokeSessions(RevokeType.CurrentSession, refreshJwt)
                    } catch (ignored: Exception) {}
                }
                launchLoginActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Internal

    // Make sure that we have a valid session. This function will
    // return true if the session needs to be renewed, false otherwise
    private fun ensureSession(): Boolean {
        val session = Descope.sessionManager.session
        return when {
            session == null -> true
            session.refreshToken.isExpired -> {
                Descope.sessionManager.clearSession()
                true
            }
            else -> false
        }
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
