package com.sorokin.dev.smallgame;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;

public class Music {
    private double mInterval = 0.200;
    private int mSampleRate = 8000;
    private byte[] generatedSnd;
    Boolean stop = false;

    private final double mStandardFreq = 440;

    public byte[] onCreateMelody(int noteIndex, int timesToRepeat, double mInterval) {
        byte[] tonByteNote = new byte[0];
        byte[] tmp = new byte[0];
        for (int i = 0; i < timesToRepeat; i++) {
            double note = getNoteFrequencies(noteIndex);
            tmp = getTone(mInterval, mSampleRate, note);
            tonByteNote = concat(tonByteNote, tmp);
        }
        return tonByteNote;
    }

    private AudioTrack audioTrack;

    public byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    private double getNoteFrequencies(int index) {
        return mStandardFreq * Math.pow(2, (double) index / 12.0d);
    }

    private byte[] getTone(double duration, int rate, double frequencies) {

        int maxLength = (int) (duration * rate);
        byte generatedTone[] = new byte[2 * maxLength];

        double[] sample = new double[maxLength];
        int idx = 0;

        for (int x = 0; x < maxLength; x++) {
            sample[x] = sine(x, frequencies / rate);
        }


        for (final double dVal : sample) {

            final short val = (short) ((dVal * 32767));

            // in 16 bit wav PCM, first byte is the low order byte
            generatedTone[idx++] = (byte) (val & 0x00ff);
            generatedTone[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

        return generatedTone;
    }

    private AudioTrack getAudioTrack(int length) {

        if (audioTrack == null)
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    mSampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, length,
                    AudioTrack.MODE_STATIC);

        return audioTrack;
    }

    private double sine(int x, double frequencies) {
        return Math.sin(2 * Math.PI * x * frequencies);
    }

    public void playTrack(byte[] generatedSnd) {
        getAudioTrack(generatedSnd.length)
                .write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();

    }

    public byte[] musicToPlay() {
        byte[] tempByte = new byte[0];
        byte[] tonByteNote = onCreateMelody(3, 3, 0.400);
        tempByte = concat(tonByteNote, tempByte);

        tonByteNote = onCreateMelody(1, 4, 0.400);
        tempByte = concat(tempByte, tonByteNote);

        tonByteNote = onCreateMelody(4, 3, 0.400);
        tempByte = concat(tempByte, tonByteNote);

        tonByteNote = onCreateMelody(3, 3, 0.400);
        tempByte = concat(tempByte, tonByteNote);


        generatedSnd = tempByte;

        return generatedSnd;
    }

    public byte[] musicToLoose() {
        byte[] tempByte = new byte[0];

        byte[] tonByteNote = onCreateMelody(6, 1, 0.400);

        tempByte = concat(tempByte, tonByteNote);
        tonByteNote = onCreateMelody(5, 1, 0.400);

        tempByte = concat(tempByte, tonByteNote);
        tonByteNote = onCreateMelody(4, 1, 0.400);

        tempByte = concat(tempByte, tonByteNote);


        generatedSnd = tempByte;

        return generatedSnd;
    }
}
