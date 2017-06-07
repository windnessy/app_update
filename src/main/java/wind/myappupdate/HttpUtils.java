package wind.myappupdate;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hasee on 2017/5/30.
 */

public class HttpUtils {
    public static String getJsonContent(String path, Context context){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try{
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if(code == 200 ){
                //Toast.makeText(context,"连接成功！",Toast.LENGTH_SHORT).show();
                return changeInputStream(connection.getInputStream());
            }else{
                Toast.makeText(context,"状态码不是200，是:"+ code,Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String changeInputStream(InputStream inputStream){
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try{
            while((len = inputStream.read(data)) != -1)
                outputStream.write(data, 0, len);
            jsonString = new String(outputStream.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
