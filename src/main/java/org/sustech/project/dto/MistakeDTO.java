package org.sustech.project.dto;

public class MistakeDTO {
    private String name;
    private long FreqScore;

    public MistakeDTO(String name, long FreqScore) {
        this.name = name;
        this.FreqScore = FreqScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFreqScore() {
        return FreqScore;
    }

    public void setFreqScore(int freqScore) {
        FreqScore = freqScore;
    }
}
