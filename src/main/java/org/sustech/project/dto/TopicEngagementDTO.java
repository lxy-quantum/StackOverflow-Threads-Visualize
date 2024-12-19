package org.sustech.project.dto;

public class TopicEngagementDTO {
    private String tag;
    private long engagementScore;

    public TopicEngagementDTO(String tag, long engagementScore) {
        this.tag = tag;
        this.engagementScore = engagementScore;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(long engagementScore) {
        this.engagementScore = engagementScore;
    }
}
