package edu.lastcow.hids.db.model;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/30/13
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class App {

    private int id;
    private String appName;
    private String actionType;
    private int count;
    private String scanFileName;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getScanFileName() {
        return scanFileName;
    }

    public void setScanFileName(String scanFileName) {
        this.scanFileName = scanFileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
