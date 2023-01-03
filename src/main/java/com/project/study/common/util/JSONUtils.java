package com.project.study.common.util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    // 내가 배열을 넣으면 알아서 JSON 형식으로 변환해주는 Util을 만들면 좋을 듯!
    public JSONObject getJSON (List<List<String>> attributes) {
        JSONObject resultObject = null;
//        Map<String, Object> map = new HashMap<String, Object>();
//        attributes
//                .stream()
//                .reduce(attr -> attr[0] attr[1]);

        resultObject = new JSONObject(attributes);
        return resultObject;
    }
}
