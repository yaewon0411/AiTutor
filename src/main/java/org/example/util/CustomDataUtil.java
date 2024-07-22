package org.example.util;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class CustomDataUtil {

    public static String getDataFromResponseEntityObject(ResponseEntity<Object> response, String key){
        JSONObject object = new JSONObject(Objects.requireNonNull(response.getBody()));

        System.out.println("런 아이디 = " + object.getString(key));
        return object.getString(key);
    }
}
