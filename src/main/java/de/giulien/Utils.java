package de.giulien;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String ConvertParams(String method, HashMap<String, ?> optionalParams) {
        return "{\"id\":\"ID\",\"method\":\"" + method + "\",\"jsonrpc\":\"2.0\",\"params\":{" + getFormDataAsString(optionalParams) + "}}";
    }

    public static String getFormDataAsString(HashMap<String, ?> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, ?> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append(",");
            }
            String value = String.format("\"%s\"",singleEntry.getValue());
            if(value.contains("[") || value.contains("{"))
                value = singleEntry.getValue().toString();
            formBodyBuilder.append(String.format("\"%s\":%s",singleEntry.getKey(),value));
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
