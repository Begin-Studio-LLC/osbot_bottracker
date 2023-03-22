import org.osbot.rs07.script.MethodProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiService {

    private static String baseUrl = "https://dev.mybottracker.com";

    public static void post(String payload, String apiFarmKey, String url, MethodProvider mp) {
        new Thread(() -> {
            HttpURLConnection a = null;
            try {
                a = createHttpConnection(baseUrl + url, apiFarmKey);
            } catch (IOException e) {
                mp.log("Unable to crate httpConnection Object");
            }
            execute(a, payload);
            try {
                mp.log("Message received response from server " + a.getResponseCode());
            } catch (IOException e) {
                mp.log("Unable to get response from server");
            }
        }).start();
    }


    private static HttpURLConnection createHttpConnection(final String url, String apiFarmKey) throws IOException {
        final HttpURLConnection httpURLConnection;
        (httpURLConnection = (HttpURLConnection) new URL(url).openConnection()).setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("apiFarmKey", apiFarmKey);
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
        return httpURLConnection;
    }

    private static String execute(final HttpURLConnection httpURLConnection, final String s) {
        String string = null;
        try {
            final OutputStream outputStream = httpURLConnection.getOutputStream();
            final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line.trim());
            }
            string = sb.toString();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        return string;
    }
}
