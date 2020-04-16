package com.caseytmorris.sparkdirector.menus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.caseytmorris.sparkdirector.R

class SetupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View? = inflater.inflate(R.layout.fragment_setup, container, false)

        val goButton: Button? = view?.findViewById(R.id.go_button)
        goButton?.setOnClickListener { goToURL("http://caseytmorris.com/sparkdirector/setup") }

        return view
    }

    private fun goToURL(url:String) {
        val uriURL = Uri.parse(url)
        val  launchBrowser = Intent(Intent.ACTION_VIEW,uriURL)
        startActivity(launchBrowser)
    }


}