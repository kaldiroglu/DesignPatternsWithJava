package dev.kaldiroglu.dp.creational.ch01.employee.better;

public class WorkInfo {
    private String workLocation;
    private boolean isRemoteWorker;
    private String timeZone;

    public WorkInfo(String workLocation, boolean isRemoteWorker, String timeZone) {
        this.workLocation = (workLocation == null) ? "" : workLocation;
        this.isRemoteWorker = isRemoteWorker;
        this.timeZone = (timeZone == null) ? "EST" : timeZone;
    }

    public static WorkInfo remote(String timeZone) {
        return new WorkInfo("", true, timeZone);
    }

    public String getWorkLocation() { return workLocation; }
    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }

    public boolean isRemoteWorker() { return isRemoteWorker; }
    public void setRemoteWorker(boolean remoteWorker) { this.isRemoteWorker = remoteWorker; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }
}