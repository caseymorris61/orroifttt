package com.caseytmorris.sparkdirector.menus


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.caseytmorris.sparkdirector.R

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_about, container, false)

        val learnMoreButton: Button? = view?.findViewById(R.id.learn_more_button)
        learnMoreButton?.setOnClickListener { goToURL("http://caseytmorris.com/sparkdirector") }

        return view
    }

    private fun goToURL(url:String) {
        val uriURL = Uri.parse(url)
        val  launchBrowser = Intent(Intent.ACTION_VIEW,uriURL)
        startActivity(launchBrowser)
    }
}
