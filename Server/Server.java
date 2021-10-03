//package com.socketexample.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("Server started");
        ServerSocket ss = new ServerSocket(4999);
        while (true) {
            try {
                Socket s = ss.accept();
                System.out.println("client connected");
                InputStreamReader inputStreamReader = new InputStreamReader(s.getInputStream());
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(s.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                while (true) {
                    String url = bufferedReader.readLine();
                    System.out.println("Client: " + url);

                    if (url.equalsIgnoreCase("exit")) {
                        System.out.println("Connection closed");
                        bufferedWriter.write("Closed");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        break;
                    }
                    else {
                        bufferedWriter.write(getData(url));
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }
                s.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                bufferedReader.close();
                bufferedWriter.close();
                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        System.out.println(getData("https://reqres.in/api/products/3"));

    }

    private static String getData(String urlFromClient) throws Exception {
        String res;
        try {
            URL url = new URL(urlFromClient);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            res = "Response Code: " + status + "; ";
            InputStreamReader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
                BufferedReader in = new BufferedReader(streamReader);
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                res += "Error: " + content;
                in.close();
                streamReader.close();
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
                BufferedReader in = new BufferedReader(streamReader);
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                res += "Content: " + content;
                in.close();
                streamReader.close();
            }

            con.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Get data failed - " + ex.toString();
        }
        return res;
    }
}
