package com.example.pfemini;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MyWebViewClient extends WebViewClient {

    // List of allowed domains
    private List<String> allowedDomains = Arrays.asList("example.com", "otherdomain.com");

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        // Check if the request is for a domain in the allowedDomains list
        String url = request.getUrl().toString();
        if (isRequestAllowed(url)) {
            try {
                // Create a connection to the requested URL
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setUseCaches(false);
                connection.connect();

                // Get the input stream from the connection
                InputStream inputStream = connection.getInputStream();

                // Create a WebResourceResponse with the input stream and the content type
                WebResourceResponse response = new WebResourceResponse("text/html", "UTF-8", inputStream);
                return response;
            } catch (IOException e) {
                // Handle exceptions
                e.printStackTrace();
            }
        }
        // Return null if the request is not allowed
        return null;
    }

    // Helper method to check if the request is for an allowed domain
    private boolean isRequestAllowed(String url) {
        for (String domain : allowedDomains) {
            if (url.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false; // Allow loading resources from other domains
    }




}
