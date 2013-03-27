package edu.lastcow.hids.db.model;

/**
 * Created with IntelliJ IDEA.
 * User: lastcow
 * Date: 3/21/13
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Device {

    private String deviceSerial;

    private String codeName;

    private String deviceLabel;

    private String deviceModel;

    private String deviceOwner;

    private String gcmRegistrationId;

    private String hardware;

    private String industrialDesign;

    private String manufacturer;

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getDeviceLabel() {
        return deviceLabel;
    }

    public void setDeviceLabel(String deviceLabel) {
        this.deviceLabel = deviceLabel;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getIndustrialDesign() {
        return industrialDesign;
    }

    public void setIndustrialDesign(String industrialDesign) {
        this.industrialDesign = industrialDesign;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
