package org.sustech.project.dto;

public class TopicFrequencyDTO {
    private String tag;
    private double frequency;

    public TopicFrequencyDTO(String tag, double frequency) {
        this.tag = tag;
        this.frequency = frequency;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
