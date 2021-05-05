package com.github.llmaximll.gym.fragments.otherfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

class WebPolicyFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_web_policy, container, false)

        webView = view.findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://dailyworkoutapps.com/privacy-policy.html")

        return view
    }

    companion object {
        fun newInstance(): WebPolicyFragment = WebPolicyFragment()
    }
}