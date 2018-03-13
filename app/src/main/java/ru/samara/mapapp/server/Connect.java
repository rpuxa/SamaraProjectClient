package ru.samara.mapapp.server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public final class Connect extends AsyncTask<String, Void, String> {

    private static final String ADDRESS = "http://54.38.186.12/";

    private Connect() {
    }

    @Override
    protected String doInBackground(String... strings) {
        return getAnswer(strings[0], strings);
    }

    private String getAnswer(String command, String... args) {
        StringBuilder stringArgs = new StringBuilder("?");
        for (int i = 1; i < args.length; i += 2) {
            stringArgs.append(args[i]).append("=").append(args[i + 1]).append("&");
        }
        stringArgs = new StringBuilder(stringArgs.substring(0, stringArgs.length() - 1));
        try {
            URL url = new URL(ADDRESS + command + stringArgs);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf8"));
                StringBuilder answer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    answer.append(line);
                }
                reader.close();
                return answer.toString();
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String send(String... args) {
        try {
            return new Connect().execute(args).get();
        } catch (InterruptedException | ExecutionException ignored) {
        }
        throw new RuntimeException("Fail!");
    }

    public static JSONObject sendToJSONObject(String... args) {
        try {
            return new JSONObject(send(args));
        } catch (JSONException ignored) {
        }
        throw new RuntimeException("Fail!");
    }

    public static JSONArray sendToJSONArray(String... args) {
        try {
            return new JSONArray(send(args));
        } catch (JSONException ignored) {
        }
        throw new RuntimeException("Fail!");
    }
}
