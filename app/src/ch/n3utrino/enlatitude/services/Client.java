package ch.n3utrino.enlatitude.services;

import android.util.Log;
import com.google.gson.Gson;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class Client<T> {

    private Gson myGson;
    private URL resourceUrl;
    private Object body;
    private Class responseType;

    public static final String FAKE_BROWSER = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.4) Gecko/20100611 Firefox/3.6.4 ( .NET CLR 3.5.30729; .NET4.0C)";


    public Client(String resourceUrl, Class responseType) throws MalformedURLException {
        this.myGson = new Gson();
        this.resourceUrl = new URL(resourceUrl);
        this.responseType = responseType;

    }

    public T connect(Object body) {

        return (T)myGson.fromJson(connect(resourceUrl, body),responseType);
    }

    private String connect(URL resourceUrl, Object body) {

        GZIPInputStream in = null;
        BufferedOutputStream out = null;
        Log.d("RestClient", resourceUrl.toString());
        try {

            URLConnection urlConnection = resourceUrl.openConnection();
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setRequestProperty("User-Agent", FAKE_BROWSER);
            if(body != null){
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                out.write(myGson.toJson(body).getBytes());

                out.flush();
                out.close();

                urlConnection.connect();
            }


            in = new GZIPInputStream(urlConnection.getInputStream());
            return readStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignore) {
            }
        }

        return "{status:\"fehler\"}";
    }


    public static String readStream(GZIPInputStream in) throws IOException {

        StringBuilder builder = new StringBuilder();

        BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        while ((inputLine = inReader.readLine()) != null)
            builder.append(inputLine);

        Log.d("RestClient", builder.toString());

        return builder.toString();
    }

}
