package org.ethan.xobot.handlers;

import org.ethan.xobot.data.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XOBotScriptList {

    public Map<Integer, String> scriptMap(String user, String password, Server server) {
        Map<Integer, String> map = new HashMap<>();
        try {
            URL url = new URL("http://xobot.org/scripts/output.php");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("u", user);
            params.put("p", password);
            params.put("s", server.getName());

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7,ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            conn.setRequestProperty("Referer", "SA51dwqw884121312v");
            conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5,en-us,en;q=0.5");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            String response = sb.toString();
            Pattern p = Pattern.compile(".*<id> *(.*) *</id>.*");
            Pattern p1 = Pattern.compile(".*<name> *(.*) *</name>.*");
            Matcher m = p.matcher(response);
            Matcher m1 = p1.matcher(response);
            List<Integer> ids = new ArrayList<>();
            while (m.find()) {
                String text = m.group(1);
                ids.add(Integer.parseInt(text));
            }
            int i = 0;
            while (m1.find()) {
                String text = m1.group(1);
                map.put(ids.get(i), text);
                i++;
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
