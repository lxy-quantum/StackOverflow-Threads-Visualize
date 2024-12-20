package org.sustech.project.dto;

public class MistakeDTO {
    private String name;
    private int FreqScore;

    public MistakeDTO(String name, int FreqScore) {
        this.name = name;
        this.FreqScore = FreqScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFreqScore() {
        return FreqScore;
    }

    public void setFreqScore(int freqScore) {
        FreqScore = freqScore;
    }
}
