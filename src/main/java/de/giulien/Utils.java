package de.giulien;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Utils {
    public static String ConvertParams(String method, Map<String, String> optionalParams) {
        return "{\"id\":\"ID\",\"method\":\"" + method + "\",\"jsonrpc\":\"2.0\",\"params\":{" + getFormDataAsString(optionalParams) + "}}";
    }

    private static String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append(",");
            }
            formBodyBuilder.append(String.format("\"%s\":\"%s\"",singleEntry.getKey(),singleEntry.getValue()));
        }
        return formBodyBuilder.toString();
    }

    public static boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
