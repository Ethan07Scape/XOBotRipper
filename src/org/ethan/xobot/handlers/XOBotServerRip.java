package org.ethan.xobot.handlers;


import org.ethan.xobot.utils.Utilities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class XOBotServerRip {

    private HashMap<String, byte[]> classBytes = new HashMap<>();
    private HashMap<String, byte[]> resourceBytes = new HashMap<>();
    private String user, pass, serverName, serverHash, output;

    public XOBotServerRip(String user, String pass, String serverHash, String serverName, String output) {
        this.user = user;
        this.pass = pass;
        this.serverHash = serverHash;
        this.serverName = serverName;
        this.output = output;
        getServerJar();
    }

    private synchronized Object getServerJar() {
        try {
            byte[] bytes = getServerBytes();
            if (bytes != null && bytes.length > 0) {
                System.out.println("Byte Length: " + bytes.length);
            }
            final byte[] array = new byte[1024];
            final JarInputStream jarInputStream = new JarInputStream(new ByteArrayInputStream(bytes));
            ZipEntry nextEntry;
            while ((nextEntry = jarInputStream.getNextEntry()) != null) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JarInputStream jarInputStream2 = jarInputStream;
                int read;
                while ((read = jarInputStream2.read(array, 0, array.length)) != -1) {
                    jarInputStream2 = jarInputStream;
                    byteArrayOutputStream.write(array, 0, read);
                }
                if (nextEntry.getName().endsWith(".class")) {
                    classBytes.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                } else {
                    System.out.println(nextEntry.getName());
                    resourceBytes.put(nextEntry.getName(), byteArrayOutputStream.toByteArray());
                }
            }
            Utilities.dumpJar(new File(output + File.separator + serverName + ".jar"), classBytes, resourceBytes);
            System.out.println("Finished ripping: " + serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getServerBytes() {
        try {
            URL url = new URL("http://xobot.org/scripts/load.php");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("u", user);
            params.put("p", pass);
            String s = new XOBotLogin().login(user, pass);
            System.out.println("Login hash: " + s);
            params.put("i", s);
            params.put("s", serverHash);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7,ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            httpConn.setRequestProperty("Referer", "84125d56*/ewf48324re$6*");
            httpConn.setRequestProperty("Accept-Language", "en-us,en;q=0.5,en-us,en;q=0.5");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            httpConn.setDoOutput(true);
            final DataOutputStream dataOutputStream = new DataOutputStream(httpConn.getOutputStream());
            dataOutputStream.writeBytes(postData.toString());
            dataOutputStream.flush();
            dataOutputStream.close();
            System.out.println("CODE: " + httpConn.getResponseCode() + " || Server: " + serverName);
            if (httpConn.getResponseCode() == 200) {
                final DataInputStream dataInputStream = new DataInputStream(httpConn.getInputStream());
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataInputStream dataInputStream2 = dataInputStream;
                int read;
                while ((read = dataInputStream2.read()) != -1) {
                    dataInputStream2 = dataInputStream;
                    byteArrayOutputStream.write(read);
                }
                dataInputStream.close();
                final ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                httpConn.disconnect();
                return byteArrayOutputStream2.toByteArray();
            }
            httpConn.disconnect();
        } catch (Exception ex) {
        }
        return null;
    }

}