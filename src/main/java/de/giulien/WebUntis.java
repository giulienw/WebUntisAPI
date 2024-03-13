package de.giulien;

import de.giulien.models.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

public class WebUntis {
    private WebUntisConfig _config;
    private HttpClient _client;
    private String _session;

    public WebUntis(String username, String password) {
        _config = new WebUntisConfig("hepta.webuntis.com", "besselgym-minden", username, password);
        try {
            _client = HttpClient.newHttpClient();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WebUntisUser Login() {
        WebUntisUser user = Request(WebUntisUser.class, "authenticate", new HashMap<String, String>() {
            {
                put("user", _config.Username);
                put("password", _config.Password);
                put("client", "Default");
            }
        });
        _session = user.SessionId;
        return user;
    }

    public List<WebUntisRoom> GetRooms() {
        if (!ValidateSession())
            return null;
        return RequestList(WebUntisRoom.class, "getRooms", new HashMap<>());
    }

    public boolean ValidateSession() {
        if (_session == null)
            return false;

        try {
            Request(String.class, "getLatestImportTime", new HashMap<>());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebUntisLesson> GetStundenplan(String id, LocalDate start, LocalDate end, ElementTyp elementTyp) {
        return RequestTimetable(id, start, end, elementTyp);
    }

    private List<WebUntisLesson> RequestTimetable(String id, LocalDate start, LocalDate end, ElementTyp elementTyp) {
        HashMap<String, ?> options = new HashMap<>() {{
            put("startDate", start.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            put("endDate", end.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            put("element", String.format("{\"type\":\"%s\", \"id\":\"%s\"}",elementTyp.toString(), id));
            put("onlyBaseTimetable", "False");
            put("showInfo","True");
            put("showSubstText", "True");
            put("showLsText", "True");
            put("showLsNumber", "True");
            put("showStudentgroup", "True");
            put("showBooking", "True");
            put("klasseFields","[\"id\", \"name\", \"longname\", \"externalkey\"]");
            put("roomFields","[\"id\", \"name\", \"longname\", \"externalkey\"]");
            put("subjectFields","[\"id\", \"name\", \"longname\", \"externalkey\"]");
            put("teacherFields","[\"id\", \"name\", \"longname\", \"externalkey\"]");
        }};
        HashMap<String, String> params = new HashMap<>();
        params.put("options", String.format("{%s}",Utils.getFormDataAsString(options)));
        try {
            String url = String.format("https://%s/WebUntis/jsonrpc.do?school=%s", _config.Server, _config.School);
            String param = Utils.ConvertParams("getTimetable", params);
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder(new URI(url))
                    .header("Cookie", "JSESSIONID=" + _session + "; school=" + _config.School)
                    .POST(HttpRequest.BodyPublishers.ofString(param))
                    .build();
            String res = _client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            if (res.contains("error"))
                APIErrorHandling(res);

            String arrs = res.substring(res.indexOf("\"result\":"))
                    .replace("\"", "").replaceAll("result:", "")
                    .replace("[", "").replace("]", "")
                    .replace("{", "")
                    .replace("}}", "");

            String[] arr = arrs.split("},id:");

            List<WebUntisLesson> response = new List();

            for (int i = 0; i < arr.length; i++) {
                HashMap<String, String> result = new HashMap<>();
                String[] current = arr[i].split(",");
                boolean inSub = false;
                String d = Arrays.toString(current);
                for (int j = 0; j < current.length; j++) {
                    String[] s = current[j].split(":");
                    if (j == 0) {
                        String idStr = Arrays.toString(s)
                                .replace("id,","")
                                .replace("]", "")
                                .replace("[","");
                        result.put("id", idStr);
                    } else if(s[0].startsWith("kl") || s[0].startsWith("su") || s[0].startsWith("ro") || s[0].startsWith("te")) {
                        inSub = true;
                        int length = d.length();
                        int startSub = d.indexOf(s[0]);
                        String subObject = d.substring(startSub);
                        int endSub = subObject.indexOf("}");
                        d = d.substring(endSub);
                        result.put(s[0], subObject
                                .substring(0,endSub)
                                .replace("}","")
                                .replace(String.format("%s:",s[0]),""));
                    } else if(current[j].contains("}")) {
                        inSub = false;
                    } else if(!inSub) {
                        result.put(s[0], s[1]);
                    }
                }
                response.append(WebUntisLesson.class.getDeclaredConstructor(HashMap.class).newInstance(result));
            }

            return response;
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

    public List<WebUntisTimegrid> GetTimegrid() {
        try {
            String url = String.format("https://%s/WebUntis/jsonrpc.do?school=%s", _config.Server, _config.School);
            String param = Utils.ConvertParams("getTimegridUnits", new HashMap<>());
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder(new URI(url))
                    .header("Cookie", "JSESSIONID=" + _session + "; school=" + _config.School)
                    .POST(HttpRequest.BodyPublishers.ofString(param))
                    .build();
            String res = _client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            if (res.contains("error"))
                APIErrorHandling(res);

            String[] arr = res.substring(res.indexOf("\"result\":"))
                    .replace("\"", "").replaceAll("result:", "")
                    .replace("[", "")
                    .replace("{", "").replace("}", "")
                    .replace("}}", "")
                    .split("]");

            List<WebUntisTimegrid> response = new List();

            for (int i = 0; i < arr.length; i++) {
                HashMap<String, String> result = new HashMap<>();
                String[] current = arr[i].split(",");
                String lastDay = "";
                for (int j = 0; j < current.length; j++) {
                    String[] s = current[j].split(":");
                    if (s[0].equals("day")) {
                        result.put(s[0], s[1]);
                        lastDay = s[1];
                    } else if(s.length == 3) {
                        String withoutDay = Arrays.toString(current).replace("[", "").replace("]", "").replace(String.format("day:%s,",lastDay), "").replace("timeUnits:", "");
                        result.put(s[0], withoutDay);
                    }
                }
                response.append(WebUntisTimegrid.class.getDeclaredConstructor(HashMap.class).newInstance(result));
            }

            return response;
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

    public List<WebUntisStudent> GetStudents() {
        return RequestList(WebUntisStudent.class, "getStudents", new HashMap<>());
    }

    private <TResponse> TResponse Request(Class<TResponse> tResponse, String method, HashMap<String, String> params) {
        try {
            String url = String.format("https://%s/WebUntis/jsonrpc.do?school=%s", _config.Server, _config.School);
            String param = Utils.ConvertParams(method, params);
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder(new URI(url))
                    .header("Cookie", "JSESSIONID=" + _session + "; school=" + _config.School)
                    .POST(HttpRequest.BodyPublishers.ofString(param))
                    .build();
            String res = _client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            if (res.contains("error"))
                APIErrorHandling(res);

            String[] arr = res.substring(res.indexOf("\"result\":"))
                    .replace("\"", "").replaceAll("result:", "")
                    .replace("{", "").replace("}", "")
                    .split(",");
            HashMap<String, String> result = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                String[] s = arr[i].split(":");
                if (s.length <= 1)
                    return (TResponse) s[0];
                result.put(s[0], s[1]);
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

    private <TResponse> List<TResponse> RequestList(Class<TResponse> tResponse, String method,
            HashMap<String, ?> params) {
        try {
            String url = String.format("https://%s/WebUntis/jsonrpc.do?school=%s", _config.Server, _config.School);
            String param = Utils.ConvertParams(method, params);
            HttpRequest request = (HttpRequest) HttpRequest.newBuilder(new URI(url))
                    .header("Cookie", "JSESSIONID=" + _session + "; school=" + _config.School)
                    .POST(HttpRequest.BodyPublishers.ofString(param))
                    .build();
            String res = _client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            if (res.contains("error"))
                APIErrorHandling(res);

            String[] arr = res.substring(res.indexOf("\"result\":"))
                    .replace("\"", "").replaceAll("result:", "")
                    .replace("[", "").replace("]", "")
                    .replace("{", "")
                    .replace("}}", "")
                    .split("},");

            List<TResponse> response = new List();

            for (int i = 0; i < arr.length; i++) {
                HashMap<String, String> result = new HashMap<>();
                String[] current = arr[i].split(",");
                for (int j = 0; j < current.length; j++) {
                    String[] s = current[j].split(":");
                    if (s.length == 1) {
                        result.put(s[0], "");
                    } else {
                        result.put(s[0], s[1]);
                    }
                }
                response.append(tResponse.getDeclaredConstructor(HashMap.class).newInstance(result));
            }

            return response;
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

    private void APIErrorHandling(String res) throws WebUntisException {
        String[] arr = res.substring(res.indexOf("\"error\":"))
                .replace("\"", "").replaceAll("error:", "")
                .replace("{", "").replace("}", "")
                .split(",");

        throw new WebUntisException(Arrays.toString(arr));
    }
}
