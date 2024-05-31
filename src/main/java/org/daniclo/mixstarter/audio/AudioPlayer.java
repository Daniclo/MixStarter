package org.daniclo.mixstarter.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AudioPlayer {
    private Mixer outputDevice;
    private Clip clip;

    public void playSoundByClip(File file, Line.Info info, Mixer device){
        Thread t = new Thread(() -> {
            try{
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                outputDevice = device;
                clip = (Clip) outputDevice.getLine(info);
                clip.open(audioStream);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                clip.close();
                audioStream.close();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | IllegalArgumentException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void stopSound(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            if (clip != null){
                clip.close();
            }
        });
        service.shutdown();
    }
    public boolean isPlaying(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Boolean> value = service.submit(() -> {
            if (clip != null) return clip.isOpen();
            else return false;
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    public void test(){
        //Controls are Gain 0, Mute 1, Balance 2 y Pan 3
        Control[] controls = clip.getControls();
        System.out.println(Arrays.toString(controls));
    }
    public FloatControl getVolumeControl(){
        return (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }
    public List<Mixer.Info> getDevices(){
        Mixer.Info[] info = AudioSystem.getMixerInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
    public Mixer getDevice(Mixer.Info info){
        return AudioSystem.getMixer(info);
    }

    public List<Line.Info> getInputLines(Mixer mixer){
        Line.Info[] info = mixer.getTargetLineInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
    public List<Line.Info> getOutputLines(Mixer mixer){
        Line.Info[] info = mixer.getSourceLineInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
}