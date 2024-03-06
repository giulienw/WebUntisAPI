package de.giulien;

import de.giulien.models.IWebUntisResponse;
import de.giulien.models.WebUntisConfig;
import de.giulien.models.WebUntisException;
import de.giulien.models.WebUntisUser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebUntis {
    private WebUntisConfig _config;
    private HttpClient _client ;
    private String _session;

    public WebUntis(String username, String password) {
        _config = new WebUntisConfig("hepta.webuntis.com","besselgym-minden", username, password);
        try {
            _client = HttpClient.newHttpClient();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WebUntisUser Login() {
        WebUntisUser user = Request(WebUntisUser.class,"authenticate", new HashMap<String, String>(){{
            put("user",_config.Username);
            put("password", _config.Password);
            put("client","Default");
        }});
        _session = user.SessionId;
        return user;
    }

    public String GetRooms() {
        if(!ValidateSession()) return null;
        return Request(String.class, "getRooms", new HashMap<>());
    }

    public boolean ValidateSession() {
        if(_session == null) return false;

        try {
            Request(String.class,"getLatestImportTime",new HashMap<>());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private <TResponse> TResponse Request(Class<TResponse> tResponse, String method, Map<String, String> params) {
        try {
            String url = String.format("https://%s/WebUntis/jsonrpc.do?school=%s",_config.Server,_config.School);
            String param = Utils.ConvertParams(method,params);
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder(new URI(url))
                    .header("Cookie", "JSESSIONID=" + _session + "; school=" + _config.School)
                    .POST(HttpRequest.BodyPublishers.ofString(param))
                    .build();
            String res = _client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            if(res.contains("error")) APIErrorHandling(res);
            String[] arr = res.substring(res.indexOf("\"result\":"))
                    .replace("\"","").replaceAll("result:","")
                    .replace("{", "").replace("}", "")
                    .split(",");
            HashMap<String, String> result = new HashMap<>();
            for(int i=0;i<arr.length;i++) {
                String[] s = arr[i].split(":");
                if(s.length <= 1)  return (TResponse) s[0];
                result.put(s[0],s[1]);
            }
            return tResponse.getDeclaredConstructor(HashMap.class).newInstance(result);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (WebUntisException e) {
            throw new RuntimeException(e);
        }
    }

    private void HandleListResponse(String res) {

    }

    private void APIErrorHandling(String res) throws WebUntisException {
        String[] arr = res.substring(res.indexOf("\"error\":"))
                .replace("\"","").replaceAll("error:","")
                .replace("{", "").replace("}", "")
                .split(",");

        throw new WebUntisException(Arrays.toString(arr));
    }
}
