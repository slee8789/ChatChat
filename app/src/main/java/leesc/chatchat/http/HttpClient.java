package leesc.chatchat.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.Data;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by hmlee
 */
public class HttpClient {
    private final String BASE_HOST;
    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final HttpRequestFactory requestFactory;
    private final Gson gson;

    public HttpClient(Context context, String baseHost, KeyStore mapKeystore) {

        HttpTransport tempHttpTransport = null;
        try {
            if (mapKeystore != null) {
                HostnameVerifier verifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                tempHttpTransport = new NetHttpTransport.Builder().trustCertificates(mapKeystore)
                        .setHostnameVerifier(verifier).build();
            } else {
                tempHttpTransport = new NetHttpTransport();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            httpTransport = tempHttpTransport;
        }

        requestFactory = httpTransport.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.setParser(new JsonObjectParser(jsonFactory));
            }
        });

//        baseHost = context.getApplicationContext().getText(R.string.basehost).toString();
        jsonFactory = AndroidJsonFactory.getDefaultInstance();
        gson = new Gson();
        BASE_HOST = baseHost;
    }

    private String _cookie = "";

    private <T> T parseResponse(HttpResponse response, HttpRequest request, Class<T> resClass) throws IOException {
        if (response.isSuccessStatusCode() == false) {
            throw new IOException("Http Response Code=" + response.getStatusCode());
        }
        getCookie(response.getHeaders());
        return parseResponseFromString(response.parseAsString(), resClass);
    }

    private void getCookie(HttpHeaders headers) {
        Object obj = headers.get("Set-Cookie");
        if (obj != null) {
            _cookie = (String) ((ArrayList<?>) obj).get(0);
        }
    }

    private void setCookie(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        if (TextUtils.isEmpty(headers.getCookie())) {
            headers.setCookie(_cookie);
        }
        headers.setAccept("application/json");
    }

    private <T> T sendEmptyBody(String _url, Class<?> reqClass, Class<T> resClass) throws IOException {
        GenericUrl url = new GenericUrl(BASE_HOST + _url);
//        HttpContent content = new JsonHttpContent(jsonFactory, Data.nullOf(reqClass))
//                .setWrapperKey(firstLowerCase(reqClass.getSimpleName()));
        HttpContent content = new JsonHttpContent(jsonFactory, Data.nullOf(reqClass));
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        return executeHttpRequest(request, resClass);
    }

    public <T> T sendRequest(String _url, Class<?> reqClass, Class<T> resClass, Object req) throws IOException {
        GenericUrl url = null;

        if(_url.startsWith("http")){
            url = new GenericUrl(_url);
        }else{
            url = new GenericUrl(BASE_HOST + _url);
        }

        HttpContent content = new JsonHttpContent(jsonFactory, req);
//        HttpContent content = new JsonHttpContent(jsonFactory, req)
//                .setWrapperKey(firstLowerCase(reqClass.getSimpleName()));
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        return executeHttpRequest(request, resClass);
    }

    private <T> T executeHttpRequest(HttpRequest request, Class<T> resClass) throws IOException {
        setCookie(request);
        HttpResponse response = null;
        try {
            response = request.execute();
//            if(response != null && response.getContent() != null)
//                System.out.println(getStringFromInputStream(response.getContent()));

        } catch (HttpResponseException e) {
            int statusCode = e.getStatusCode();
            throw e;
//            if (statusCode == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED || statusCode == HttpStatusCodes.STATUS_CODE_NOT_FOUND
//                    || statusCode == HttpStatusCodes.STATUS_CODE_FORBIDDEN){
//                request.getHeaders().setCookie(null);
//                setCookie(request);
//                response = request.execute();
//            } else {
//                throw new IOException("Http Response Code=" + statusCode);
//            }
        } catch (Exception e){
            throw e;
//            throw new IOException(e.getResultMessage());
        }
        return parseResponse(response, request, resClass);
    }

    private <T> T parseResponseFromString(String json, Class<T> resClass) throws IOException {
        Log.e("HttpClient", "response = " + json);

        JsonObject jsonObject = (JsonObject) gson.fromJson(json, JsonElement.class);
        T t = gson.fromJson(jsonObject, resClass);
        return t;
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private String firstLowerCase(String orgStr) {
        char c = Character.toLowerCase(orgStr.charAt(0));
        return c + orgStr.substring(1);
    }
}
