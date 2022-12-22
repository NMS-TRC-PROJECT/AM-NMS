package com.project.study.common.util;

//import org.json.simple.JSONObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    // Http Get
    public static String sendGet(String url, int connTimeout, int readTimeout) throws Exception {
        String result = "";

        // 문자열 url이 지정하는 자원에 대한 URL 객체 생성
        URL obj = new URL(url);

        // HttpURLConnection : 외부 서버와 통신하기 위함
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Optional default is GET
        con.setRequestMethod("GET");
        con.setConnectTimeout(connTimeout); // 연결 타임아웃 값 설정(단위 : 밀리초)
        con.setReadTimeout(readTimeout);    // 읽기 타임아웃 값 설정(단위 : 밀리초)
        int responseCode = con.getResponseCode();   // 서버에서 보낸 HTTP 상태 코드 반환

        // URL 읽기!
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer sb = new StringBuffer();   // StringBuilder는 동기화 지원X (멀티쓰레드 환경에서 적합X)

        while((inputLine = br.readLine()) != null){
            sb.append(inputLine);
            System.out.println("HTTPUtils sb : " + sb);
        }
        br.close();

        // sb로 만들어 놓은 결과 값 반환
        result = sb.toString();

        return result;
    }

    // HTTP Post
    public static String sendPost(String url, JSONObject jsonObject) throws Exception {
        String result = "";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header
        con.setRequestMethod("POST");
        con.setDoOutput(true);  // urlconnection이 서버에 데이터를 보내는데 사용할 수 있는지 여부
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.connect();

        // send post request
        byte[] outputBytes = jsonObject.toString().getBytes("UTF-8");
        OutputStream os = con.getOutputStream();
        os.write(outputBytes);
        os.flush();
        os.close();
        outputBytes = null;

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null ){
            response.append(inputLine);
        }
        in.close();

        //print result
        result = response.toString();
        return result;
    }

    // HTTP Request
    public static String sendRequest(String url, String method) throws IOException {
        String result = "";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header
        con.setRequestMethod(method);
        con.setDoOutput(true); // urlconnection이 서버에 데이터를 보내는데 사용할 수 있는지 여부
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.connect();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(),"UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        result = response.toString();

        return result;

    }
}
