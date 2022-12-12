package com.project.study.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
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
}
