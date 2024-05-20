package com.qa.utils;

import com.qa.base.BaseSetup;
import com.qa.base.DriverManager;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoManager extends BaseSetup {
    public void startRecording() {
        logger().info("video recording starting...");
        ((CanRecordScreen) new DriverManager().getDriver()).startRecordingScreen();
    }

    public byte[] stopRecording(String dirPath, String fileName) {
        byte[] videoBytes = null;
        logger().info("video recording stopping...");
        String media = ((CanRecordScreen) new DriverManager().getDriver()).stopRecordingScreen();
        File videoDir = new File(dirPath);
        synchronized (videoDir) {
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(videoDir + fileName);
            videoBytes = (Base64.decodeBase64(media));
            stream.write(videoBytes);
            stream.close();
            logger().info("Video path: " + videoDir + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger().error("Video capturing failure 1: " + e.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger().error("Video capturing failure 2: " + e.getMessage());
                }
            }
        }
        return videoBytes;
    }
}
