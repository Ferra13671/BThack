package com.ferra13671.BThack;

public final class VersionInfo {
    private String newVersion = "";
    private boolean needShowAgainOneRelease = true;
    private boolean needShowAgainAllReleases = true;
    private boolean outdated = false;

    public VersionInfo() {
    }

    void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public void setNeedShowAgainOneRelease(boolean needShowAgainOneRelease) {
        this.needShowAgainOneRelease = needShowAgainOneRelease;
    }

    public void setNeedShowAgainAllReleases(boolean needShowAgainAllReleases) {
        this.needShowAgainAllReleases = needShowAgainAllReleases;
    }

    void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    public String getNewVersion() {
        return new String(newVersion);
    }

    public boolean isNeedShowAgainOneRelease() {
        return needShowAgainOneRelease;
    }

    public boolean isNeedShowAgainAllReleases() {
        return needShowAgainAllReleases;
    }

    public boolean isOutdated() {
        return outdated;
    }
}
