package com.inventar.domain;

public class Asset {

    private long id;
    private String name;
    private String invNumb;
    private String serNumb;
    private String assetType;
    private String assetDescr;
    private long employeeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvNumb() {
        return invNumb;
    }

    public void setInvNumb(String invNumb) {
        this.invNumb = invNumb;
    }

    public String getSerNumb() {
        return serNumb;
    }

    public void setSerNumb(String serNumb) {
        this.serNumb = serNumb;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getAssetDescr() {
        return assetDescr;
    }

    public void setAssetDescr(String assetDescr) {
        this.assetDescr = assetDescr;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}
