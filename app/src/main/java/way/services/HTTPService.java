package way.services;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPService {
    private final String serverUrl = "http://192.168.43.91:3131";

    public String connectToServer() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(serverUrl + "/connect");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        return result.toString();
    }

    public String sendVRPhoto(String photo) throws Exception {
        java.net.URL obj = new URL(serverUrl + "/add/vrphoto");
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("photo=" + photo);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        Log.i("POST", "\nSending 'POST' request to URL : " + serverUrl + "/add/vrphoto");
        Log.i("POST", "Post parameters : " + "photo=" + photo);
        Log.i("POST", "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.i("POST", response.toString());

        return response.toString();
    }

    public String sendCurrentLocation(String location) throws Exception {
        java.net.URL obj = new URL(serverUrl + "/location");
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("location=" + location);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        Log.i("POST", "\nSending 'POST' request to URL : " + serverUrl + "/location");
        Log.i("POST", "Post parameters : " + "location=" + location);
        Log.i("POST", "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Log.i("POST", response.toString());

        return response.toString();
    }
}
