package uq.deco2800.pyramidscheme.settings;

/**
 * A sound object for holding sound settings (sfx volume, background volume, global mute)
 * <p>
 * Created by billy on 6/10/16.
 */
public class Sound {

    private boolean muted;
    private double sfxVolume;
    private double backgroundVolume;

    public Sound(boolean muted, double sfxVolume, double backgroundVolume) {
        this.muted = muted;
        this.sfxVolume = Math.max(Math.min(1.0, sfxVolume), 0);
        this.backgroundVolume = Math.max(Math.min(1.0, backgroundVolume), 0);
    }

    /**
     * Get if sound is muted
     *
     * @return Boolean is sound muted
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Get the volume for sound effects
     *
     * @return double volume for sound effects
     */
    public double getSFXVolume() {
        return sfxVolume;
    }

    /**
     * Get the volume for background music
     *
     * @return double volume for background music
     */
    public double getBackgroundVolume() {
        return backgroundVolume;
    }
}
