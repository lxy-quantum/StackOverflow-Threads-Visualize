package org.sustech.project.dto;

public class AnswerQualityDTO {
    private String interval;
    private double acceptRate;
    private double avgUpVotes;
    private int count;

    public AnswerQualityDTO(String interval, double acceptRate, double avgUpVotes, int count) {
        this.interval = interval;
        this.acceptRate = acceptRate;
        this.avgUpVotes = avgUpVotes;
        this.count = count;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public double getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(double acceptRate) {
        this.acceptRate = acceptRate;
    }

    public double getAvgUpVotes() {
        return avgUpVotes;
    }

    public void setAvgUpVotes(double avgUpVotes) {
        this.avgUpVotes = avgUpVotes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
