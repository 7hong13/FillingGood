package com.example.fillinggood.Boundary.personal_calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class EveryTimeXMLParser {

    public String everytime(String ETID,String ETPWD,String Year,String sem) throws IOException {



        URL url = new URL("http://py.takesup.io:8003/?id=" + ETID + "&pwd=" +
                URLEncoder.encode(ETPWD, "UTF-8") + "&year=" + Year + "&semester=" + sem);

        // 문자열로 URL 표현
        System.out.println("URL :" + url.toExternalForm());

        // HTTP Connection 구하기
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 요청 방식 설정 ( GET or POST or .. 별도로 설정하지않으면 GET 방식 )
        conn.setRequestMethod("GET");

        // 연결 타임아웃 설정
        conn.setConnectTimeout(4000); // 3초
        // 읽기 타임아웃 설정
        conn.setReadTimeout(4000); // 3초




        byte[] data = null;

        // 응답 내용(BODY) 구하기
        try{
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            // System.out.println(new String(out.toByteArray(), "UTF-8"));

            data = out.toByteArray();
            // 접속 해제
            conn.disconnect();
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println(new String(data));
        String d = getXml(new ByteArrayInputStream(data));
        return d;
    }


    private String getXml (InputStream is){
        List<String> cls = new ArrayList<String>();
        List<ArrayList<String>> day = new ArrayList<ArrayList<String>>();
        List<String> StartTime = new ArrayList<String>();
        List<String> EndTime = new ArrayList<String>();
        List<String> Place = new ArrayList<String>();
        ArrayList<String> datas = null;
        int n = 1;


        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(new InputStreamReader(is));

            int eventType = xmlPullParser.getEventType();


            while (eventType != xmlPullParser.END_DOCUMENT) {



                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT: // 문서 시작
                        break;
                    case XmlPullParser.START_TAG: // 태그의 시작
                        String tagName = xmlPullParser.getName();

                        System.out.println("START_TAG : " + tagName);

                        if ("name".equals(tagName)) {
                            cls.add(xmlPullParser.getAttributeValue(0));
                        } else if ("data".contentEquals(tagName)) {

                           if(n==1){
                               datas = new ArrayList<String>();
                               datas.add(xmlPullParser.getAttributeValue(0));

                               n+=1;
                           }
                           if(n==2 && xmlPullParser.getAttributeValue(0)!= datas.get(0)){
                               datas.add(xmlPullParser.getAttributeValue(0));
                               day.add(datas);
                               StartTime.add(xmlPullParser.getAttributeValue(1));
                               EndTime.add(xmlPullParser.getAttributeValue(2));
                               Place.add(xmlPullParser.getAttributeValue(3));
                               n=1;
                           }

                        }
                        if (xmlPullParser.getName().equals("b")) {
                            System.out.println("xmlPullParser.getName().equals(\"b\")");
                        }
                        int countParam = xmlPullParser.getAttributeCount();
                        System.out.println("getAttributeCount() : " + countParam);

                        for (int i = 0; i < countParam; i++) {
                            System.out.println(i + " param getAttributeName() : " + xmlPullParser.getAttributeName(i));
                            System.out.println(i + " param getAttributeValue() : " + xmlPullParser.getAttributeValue(i));
                        }
                        break;
                    case XmlPullParser.END_TAG: // 태그의 끝
                        System.out.println("END_TAG : " + xmlPullParser.getName());
                        break;
                    case XmlPullParser.TEXT: // TEXT 인경우, "> 에서 < 사이"
                        System.out.println("TEXT : " + xmlPullParser.getText());
                        break;
                }
                eventType = xmlPullParser.next(); // 다음 parse 가르키기
            }

            System.out.println();
            System.out.println();
            System.out.println("-------------여기 아래에 이름이랑 시간 값 출력-------------");
            System.out.println();
            for(int i=0;i<cls.size(); i++) {
                System.out.println("수업이름 : " + cls.get(i));
                System.out.println("수업 날짜 : " + day.get(i));
                System.out.println("시작 시간 : " + StartTime.get(i));
                System.out.println("끝나는 시간 : " + EndTime.get(i));
                System.out.println("강의 : " + Place.get(i));




            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

}

