package com.example.intentserviceszomato;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    // Base URL for Books API.
    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String queryString){
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String bookJsonString = null;
        try{
            Uri uri = Uri.parse(BOOK_BASE_URL).buildUpon().
                    appendQueryParameter(QUERY_PARAM,queryString).
                    appendQueryParameter(MAX_RESULTS,"10").
                    appendQueryParameter(PRINT_TYPE,"books").build();
            URL requestUrl = new URL(uri.toString());
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String text;
            while((text = bufferedReader.readLine()) != null){
                stringBuilder.append(text);
            }
            if(stringBuilder.length() == 0){
                Log.e(LOG_TAG,"String from Builder is of zero length");
            }

            bookJsonString = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, bookJsonString);
        return bookJsonString;
    }
}
