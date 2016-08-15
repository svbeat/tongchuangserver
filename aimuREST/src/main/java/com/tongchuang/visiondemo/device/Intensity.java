package com.tongchuang.visiondemo.device;

public class Intensity {
    public static final Intensity MAX_INTENSITY = new Intensity(0, 1f, 0, 255, 0, 0);
    private int     sensitivityDB;
    private float   screenBrightness;
    private int     stimulusAlpha;
    private int     stimulusGreyscale;
    private int     backgroundAlpha;
    private int     backgroundGreyscale;

    public Intensity(int sensitivityDB, float screenBrightness, int stimulusAlpha, int stimulusGreyscale, int backgroundAlpha, int backgroundGreyscale) {
        this.sensitivityDB = sensitivityDB;
        this.screenBrightness = screenBrightness;
        this.stimulusAlpha = stimulusAlpha;
        this.stimulusGreyscale = stimulusGreyscale;
        this.backgroundAlpha = backgroundAlpha;
        this.backgroundGreyscale = backgroundGreyscale;
    }

    public int getSensitivityDB() {
        return sensitivityDB;
    }

    public float getScreenBrightness() {
        return screenBrightness;
    }

    public int getStimulusAlpha() {
        return stimulusAlpha;
    }

    public int getStimulusGreyscale() {
        return stimulusGreyscale;
    }

    public int getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public int getBackgroundGreyscale() {
        return backgroundGreyscale;
    }
}
