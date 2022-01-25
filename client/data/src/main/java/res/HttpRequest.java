package res;

import helper.Tuple;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        connection.setUseCaches(false);

        if(sendAuth){
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        }

        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.write(postData);
        }

        return getHttpTuple(connection);
    }

    public Tuple<Integer, String> sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int status = connection.getResponseCode();

        return getHttpTuple(connection);
    }

    private Tuple<Integer, String> getHttpTuple(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        String inputLine;
        StringBuilder content = new StringBuilder();
        BufferedReader in;

        if (status == 200) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        connection.disconnect();

        return new Tuple<>(status, content.toString());
    }
}
