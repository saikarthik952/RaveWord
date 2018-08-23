package official.karthik.raveword.raveword;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebsiteContent extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_content);
        WebView mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Bundle b= getIntent().getExtras();

        if (b != null) {
            mWebView.loadUrl(b.getString("web"));
        }
    }
}
