package com.descope.androiddemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.descope.Descope
import com.descope.android.DescopeFlow
import com.descope.android.DescopeFlowView
import com.descope.session.DescopeSession
import com.descope.types.AuthenticationResponse
import com.descope.types.DescopeException

class LoginActivity : AppCompatActivity(), DescopeFlowView.Listener {

    private lateinit var flowContainer: View
    private lateinit var loadingIndicator: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        flowContainer = findViewById(R.id.flow_container)
        loadingIndicator = findViewById(R.id.loading)
        val flowView: DescopeFlowView = findViewById(R.id.flow_view)
        val loginButton: View = findViewById(R.id.login_button)
        val closeFlowButton: View = findViewById(R.id.close_button)

        hideFlowContainer()

        flowView.listener = this
        val descopeFlow = DescopeFlow(Uri.parse("https://api.descope.com/login/$descopeProjectId?flow=sign-up-or-in&shadow=false")) // This should be wherever you host your flow
        loginButton.setOnClickListener {
            loadingIndicator.visibility = VISIBLE
            flowView.run(descopeFlow)
        }

        closeFlowButton.setOnClickListener {
            hideFlowContainer(animated = true)
        }
    }

    // DescopeFlowView.Listener

    override fun onReady() {
        loadingIndicator.visibility = GONE
        showFlowContainer()
    }

    override fun onSuccess(response: AuthenticationResponse) {
        // Manage the session
        Descope.sessionManager.manageSession(DescopeSession(response))
        launchMainActivity()
    }

    override fun onError(exception: DescopeException) {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
    }

    // internal

    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Animations

    private fun hideFlowContainer(animated: Boolean = false) {
        if (animated) {
            flowContainer.animate().alpha(0f).setDuration(350L).withEndAction { flowContainer.visibility = GONE }.start()
        } else {
            flowContainer.visibility = GONE
            flowContainer.alpha = 0f
        }
    }

    private fun showFlowContainer() {
        flowContainer.visibility = VISIBLE
        flowContainer.animate().alpha(1f).setDuration(350L).start()
    }
}
