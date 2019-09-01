package com.myspy.myspyandroid.functions;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Miroslav Murin on 04.01.2017.
 */

public final class HTTPWork {

    private HTTPWork()
    {

    }


    /**
     * Convert string to URL format
     * @param url not formated url
     * @return String url formated url
     */
    public static String ConvertString(String url)
    {
        url = url.replace(" ","%20");
        url = url.replace("!","%21");
        url = url.replace("\"","%22");
        url = url.replace("#","%23");
        url = url.replace("$","%24");
        url = url.replace("'","%27");
        url = url.replace("(","%28");
        url = url.replace(")","%29");
        return url;
    }



    /**
     * Get from url
     * @param Url URL from you want to receive
     * @return String
     */
    public static String GET(String Url) {

        HttpURLConnection urlConnection = null;
        try {
            Url = ConvertString(Url);
            URL url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }

            return total.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }


    /**
     * Get from url
     * @param Url URL from you want to receive
     * @param LineSize (Int) Count of lines you want to read
     * @return String
     */
    public static String GET(String Url, int LineSize) {

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        try {
            int linec = 0;
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null && linec <LineSize) {
                total.append(line).append('\n');
                linec++;
            }

            return total.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }


    /**
     * Get from url
     * @param Url URL from you want to receive
     * @param WhileWord Read from URL, while text will be equals to word
     * @return String
     */
    public static String GET(String Url, String WhileWord) {

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
                if(line.replace(" ","").equals(WhileWord))
                    break;
            }

            return total.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }


    /**
     * Get from url
     * @param Url URL from you want to receive
     * @param WhileWord Read from URL, while text will be equals to word
     * @param count Count of words to break cycle
     * @return String
     */
    public static String GET(String Url, String WhileWord, int count) {

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try {

            int wordsfound = 0;

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
                if(line.replace(" ","").equals(WhileWord)) {
                    wordsfound++;
                    if(wordsfound>=count)
                        break;
                }
            }

            return total.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return null;
    }


}
