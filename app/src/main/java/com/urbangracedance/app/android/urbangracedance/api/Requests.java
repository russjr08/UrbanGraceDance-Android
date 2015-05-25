package com.urbangracedance.app.android.urbangracedance.api;

import com.google.gson.Gson;
import com.urbangracedance.app.android.urbangracedance.api.interfaces.DanceService;
import com.urbangracedance.app.android.urbangracedance.api.models.Login;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * @author russjr08
 */
public class Requests {

    private final String DEFAULT_API_BASE = "http://192.168.1.72:3000/api/v1/";
    private final String HTTP_PREFIX = "http://";

    private String token;
    private String api_base = DEFAULT_API_BASE;

    private DanceService service;

    public Requests() {}

    public Requests(String token) {
        this.token = token;

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(api_base)
                .setRequestInterceptor(createRequestInterceptor())
                .build();

        service = adapter.create(DanceService.class);


    }


    public void login(String username, String password) throws Exception {
        URL url;
        HttpURLConnection connection = null;

        String urlParameters = "username=" + URLEncoder.encode(username)
                + "&password=" + URLEncoder.encode(password);

        try {
            //Create connection
            url = new URL(getEndpoint("login"));
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            try {
                Login login = new Gson().fromJson(response.toString(), Login.class);

                this.token = login.token;

                RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(api_base)
                        .setRequestInterceptor(createRequestInterceptor())
                        .build();

                service = adapter.create(DanceService.class);
            } catch(Exception e) {
                throw e;
            }




        } catch (Exception e) {

            e.printStackTrace();
            return;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

    public void getSelf(Callback<User> callback) {
        service.getSelf(callback);
    }

    public RequestInterceptor createRequestInterceptor() {
        RequestInterceptor interceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", "Urban Grace Dance Android App");
                request.addHeader("X-Access-Token", token);
            }
        };

        return interceptor;
    }

    public String getEndpoint(String endpoint) {
        return api_base + endpoint + "/";
    }


    public void setApiBase(String base) {
        if(!base.endsWith("/")) {
            base = base + "/";
        }

        if(!base.startsWith("http://")) {
            base = HTTP_PREFIX + base;
        }

        this.api_base = base;
    }


    public String getToken() {
        return token;
    }

    private Thread createThread() {
        return new Thread("UrbanGraceDance - Network Thread");
    }

}
