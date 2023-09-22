package coding.faizal.mytasklist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import coding.faizal.mytasklist.databinding.ActivityTransactionBinding

class TransactionActivity : AppCompatActivity() {

    private var _binding : ActivityTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        setupWebView(url ?: "https://github.com/Faizalilham")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url : String){
       binding.apply {
           webView.webViewClient = WebViewClient()
           webView.loadUrl(url)
           webView.settings.javaScriptEnabled = true
           webView.webViewClient = object : WebViewClient() {
               @Deprecated("Deprecated in Java")
               override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                   view?.loadUrl(url.toString())
                   return true
               }
           }
       }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}