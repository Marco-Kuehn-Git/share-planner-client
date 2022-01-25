//Marc Beyer//
package res;

import helper.Tuple;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpRequest {
    public static String TOKEN = "";

    public Tuple<Integer, String> sendPostRequest(String urlString, String urlParameters, boolean sendAuth) throws Exception {
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        /*
        URL url = new URL("http://test.de:8080/event/add");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Authorization", "Bearer {token}");
        http.setRequestProperty("Content-Type", "");
        http.setRequestProperty("Content-Length", "0");

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
         */
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        con.setUseCaches(false);

        if(sendAuth){
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + TOKEN);
        }

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }

        int status = con.getResponseCode();
        String inputLine;
        StringBuilder content = new StringBuilder();
        BufferedReader in;

        if (status == 200) {
             in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
             in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        return new Tuple<>(status, content.toString());
    }

    public String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();
            return content.toString();

        } else {
            con.disconnect();
            throw new Exception("Status: " + status);
        }
    }
}
