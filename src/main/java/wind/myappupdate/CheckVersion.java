package wind.myappupdate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Hasee on 2017/5/30.
 */

public class CheckVersion {
    @SuppressWarnings("unused")
    JSONObject update_server_info_json, update_basic_info;
    UpdateAppInfo update_info = new UpdateAppInfo();
    private AlertDialog.Builder mDialog;
    /**
     *获得服务器json对象和提取版本号
     */
    public int check(Context context){
        String jsonString = HttpUtils.getJsonContent("http://192.168.31.158:8081", context);
        if(jsonString == null){
            Toast.makeText(context,"无法连接服务器获取最新版本",Toast.LENGTH_SHORT).show();
            return -1;
        }
        try{
            update_server_info_json = new JSONObject(jsonString);
            update_basic_info = update_server_info_json.getJSONObject("data");
            update_info.data.setAppname(update_basic_info.getString("appname"));
            update_info.data.setServerVersion(update_basic_info.getString("serverVersion"));
            update_info.data.setServerFlag(update_basic_info.getString("serverFlag"));
            update_info.data.setLastForce(update_basic_info.getString("force"));
            update_info.data.setUpdateurl(update_basic_info.getString("updateurl"));
            update_info.data.setUpgradeinfo(update_basic_info.getString("upgradeinfo"));
            update_info.setError_code(update_server_info_json.getInt("error_code"));
            update_info.setError_msg(update_server_info_json.getString("error_msg"));
            //Toast.makeText(context,"新版本号为"+ update_info.data.getServerVersion(),Toast.LENGTH_SHORT).show();
        }catch(JSONException e){
            e.printStackTrace();
        }
        String now_version = getVersion(context);
        String appName = update_info.getData().getAppname();
        String downUrl = update_info.getData().getUpdateurl();
        String updateInfo = update_info.getData().getUpgradeinfo();
        if(VersionComparison(update_info.data.getServerVersion(), now_version) > 0){
            if(update_info.data.getLastForce().equals("1") && !TextUtils.isEmpty(update_info.getData().getUpgradeinfo())){
                forceUpdate(context,appName ,downUrl, updateInfo);
            }else{
                normalUpdate(context,appName ,downUrl, updateInfo);
            }
        }
        else{
            Toast.makeText(context,"当前软件已是最新版本",Toast.LENGTH_SHORT).show();
        }
        return 0;
    }
    /**
     * forceUpdate
     * 强制升级
     */
    public int forceUpdate(final Context context,final String appName, final String downUrl,final String updateinfo){
        final boolean flag = true;
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName + "升级啦！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState(context)) {
                    showDownloadSetting(context);
                    return;
                }
                DownLoadApk.download(context,downUrl,updateinfo,appName, flag);
            }
        }).setCancelable(false).create().show();
        return 0;
    }
    /**
     * normalUpdate
     * 不强制升级
     */
    public int normalUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        final boolean flag = false;
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle(appName+"又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState(context)) {
                    showDownloadSetting(context);
                    return;
                }
                DownLoadApk.download(context,downUrl,updateinfo,appName, flag);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
        return 0;
    }
    /**
     * 获取本地版本号，默认1.0.0
     */
    public String getVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 比较版本号，
     * @param versionServer
     * @param versionLocal
     * @return 大于0，服务器版本号大，更新；<=0 不更新
     */
    public int VersionComparison(String versionServer, String versionLocal){
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");
        int index1 = 0, index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            int[] number2 = getValue(version2, index2);
            if (number1[0] < number2[0]){
                return -1;
            }
            else if (number1[0] > number2[0]){
                return 1;
            }
            else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if (index1 < version1.length())
            return 1;
        else
            return -1;
    }
    public int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    /**
     * 判断下载状态
     * @param context
     * @return
     */
    private boolean canDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 显示下载设置
     */
    private void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent, context)) {
            context.startActivity(intent);
        }
    }
    private boolean intentAvailable(Intent intent, Context context) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
