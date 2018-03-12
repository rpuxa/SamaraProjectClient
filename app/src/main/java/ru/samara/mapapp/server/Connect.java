package ru.samara.mapapp.server;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public final class Connect extends AsyncTask<String, Void, JSONObject> {

    private static final String ADDRESS = "http://54.38.186.12/";

    private Connect() {
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        return getAnswer(strings[0], strings);
    }

    public JSONObject getAnswer(String command, String... args) {
        JSONObject object = null;
        String stringArgs = "?";
        for (int i = 1; i < args.length; i += 2) {
            stringArgs += args[i] + "=" + args[i + 1] + "&";
        }
        stringArgs = stringArgs.substring(0, stringArgs.length() - 1);
        try {
            URL url = new URL(ADDRESS + command + stringArgs);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf8"));
                String answer = "";
                String line;

                while ((line = reader.readLine()) != null) {
                    answer += line;
                }
                reader.close();
                object = new JSONObject(answer);
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONObject send(String... args) {
        try {
            return new Connect().execute(args).get();
        } catch (InterruptedException | ExecutionException ignored) {
        }
        throw new RuntimeException("Fail!");
    }
}
