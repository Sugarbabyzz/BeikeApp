package com.example.beikeapp.Util.ProfileUtil;

import android.util.Log;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by m1821 on 2018/5/16.
 */

public class UploadImage {
    private static final String TAG = "UploadImageLog";
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    public static String uploadFile(String sourceFileUri) {

        int serverResponseCode = 777;

        StringBuffer response = new StringBuffer();

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name = (hour + "" + minute + "" + second + "" + day + "" + (month + 1) + "" + year);
        String tag = name + ".jpg";
        String fileName = sourceFileUri.replace(sourceFileUri, tag);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e(TAG, "Source File not exist!!");
            return null;

        } else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(GlobalConstant.URL_CHANGE_PROFILE_PHOTO +"?account=111");

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // 建立连接获取响应输入流
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Log.d("lookathere",response.toString());

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                Log.e(TAG + "Server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                Log.e(TAG + "Server2", "Exception : " + e.getMessage(), e);
            }
            return response.toString();

        }
    }
}

















//    /**
//     * android上传文件到服务器
//     * @param file  需要上传的文件
//     * @param RequestURL  请求的rul
//     * @return  返回响应的内容
//     */
//    public static String uploadFile(File file, String RequestURL){
//        String result = null;
//
//        StringBuffer response = null;
//
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
//
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
//            conn.setDoInput(true);  //允许输入流
//            conn.setDoOutput(true); //允许输出流
//            conn.setUseCaches(false);  //不允许使用缓存
//            conn.setRequestMethod("POST");  //请求方式
//            conn.setRequestProperty("Charset", CHARSET);  //设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            //conn.setRequestProperty("action", "upload");
//            conn.connect();
//
//            if(file!=null){
//                /**
//                 * 当文件不为空，把文件包装并且上传
//                 */
//                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                /**
//                 * 这里重点注意：
//                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
//                 * filename是文件的名字，包含后缀名的   比如:abc.png
//                 */
//
//                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\""+LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
//                sb.append(LINE_END);
//
//                Log.d(TAG + "SB",sb.toString());
//
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=is.read(bytes))!=-1){
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//
//                Log.d(TAG + "DOS",dos.toString());
//
//                dos.flush();
//
//
//                /**
//                 * 获取响应码  200=成功
//                 * 当响应成功，获取响应的流
//                 */
//                int res = conn.getResponseCode();
//                Log.d(TAG + "code",res+"");
//
//
//                response = new StringBuffer();
//                InputStream in = conn.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//                Log.d(TAG + "RES",response.toString());
//
//
//
////                if(res==200){
////                    InputStream input =  conn.getInputStream();
////                    StringBuffer sb1= new StringBuffer();
////                    int ss ;
////                    while((ss=input.read())!=-1){
////                        sb1.append((char)ss);
////                    }
////                    result = sb1.toString();
////                    Log.d(TAG,result);
////                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response.toString();
//    }
