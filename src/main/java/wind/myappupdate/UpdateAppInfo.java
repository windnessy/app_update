package wind.myappupdate;

/**
 * Created by Hasee on 2017/5/30.
 */

public class UpdateAppInfo {
    public UpdateInfo data;
    public Integer error_code;
    public String error_msg;
    UpdateAppInfo(){
        error_msg = new String();
        data = new UpdateInfo();
    }
    public static class UpdateInfo{
        UpdateInfo(){
            appname = new String();
            serverVersion = new String();
            serverFlag = new String();
            force = new String();
            updateurl = new String();
            upgradeinfo = new String();
        }
        public String appname;
        public String serverVersion;
        public String serverFlag;
        public String force;
        public String updateurl;
        public String upgradeinfo;
        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getServerVersion() {
            return serverVersion;
        }

        public void setServerVersion(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public String getServerFlag() {
            return serverFlag;
        }

        public void setServerFlag(String serverFlag) {
            this.serverFlag = serverFlag;
        }

        public String getLastForce() {
            return force;
        }

        public void setLastForce(String lastForce) {
            this.force = lastForce;
        }

        public String getUpdateurl() {
            return updateurl;
        }

        public void setUpdateurl(String updateurl) {
            this.updateurl = updateurl;
        }

        public String getUpgradeinfo() {
            return upgradeinfo;
        }

        public void setUpgradeinfo(String upgradeinfo) {
            this.upgradeinfo = upgradeinfo;
        }
    }

    public UpdateInfo getData() {
        return data;
    }

    public void setData(UpdateInfo data) {
        this.data = data;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}