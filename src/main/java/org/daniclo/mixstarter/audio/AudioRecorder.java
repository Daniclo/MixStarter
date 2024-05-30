package org.daniclo.mixstarter.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private Mixer inputDevice;
    public TargetDataLine inputDataLine;

    public void recordSound(Line.Info info, Mixer device, String path, String name) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        inputDevice = device;
        inputDataLine = (TargetDataLine) inputDevice.getLine(info);
        inputDataLine.open();

        inputDataLine.start();
        AudioInputStream recordingStream = new AudioInputStream(inputDataLine);
        File fileOut = new File(path + "\\" + name + ".wav");
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        if (!AudioSystem.isFileTypeSupported(fileType,recordingStream)){
            System.err.println("Unsupported file type");
            return;
        }
        Thread t = new Thread(() ->{
            try {
                System.out.println("Recording now");
                AudioSystem.write(recordingStream,fileType,fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
    }
}