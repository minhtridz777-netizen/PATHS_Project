package com.paths.core;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Dev 1. Phát âm thanh từ res/sounds/<tên>.wav (Java thuần chỉ đọc được WAV).
 * Thiếu file hoặc máy không có loa thì im lặng, không báo lỗi.
 *   Sound.play("attack")  : phát 1 lần (hiệu ứng)
 *   Sound.loop("music")   : phát lặp lại (nhạc nền), Sound.stopMusic() để dừng
 */
public final class Sound {

    private static final Map<String, Clip> CLIPS = new HashMap<>();
    private static final Set<String> MISSING = new HashSet<>();
    private static Clip music;
    private static String musicName;

    private Sound() {
    }

    public static synchronized void play(String name) {
        Clip clip = clip(name);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Tua về đầu để phát lại được ngay cả khi lần trước chưa hết
            clip.start();
        }
    }

    public static synchronized void loop(String name) {
        if (name.equals(musicName)) {
            return; // Đang phát đúng bài này rồi
        }
        stopMusic();
        musicName = name;
        music = clip(name);
        if (music != null) {
            music.setFramePosition(0);
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static synchronized void stopMusic() {
        if (music != null) {
            music.stop();
        }
        music = null;
        musicName = null;
    }

    /** Nạp file 1 lần rồi giữ lại để lần sau phát ngay */
    private static Clip clip(String name) {
        if (MISSING.contains(name)) {
            return null;
        }
        Clip cached = CLIPS.get(name);
        if (cached != null) {
            return cached;
        }
        try (InputStream raw = Sound.class.getResourceAsStream("/sounds/" + name + ".wav")) {
            if (raw == null) {
                MISSING.add(name);
                return null;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(raw));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            CLIPS.put(name, clip);
            return clip;
        } catch (Exception e) {
            // Sai định dạng (không phải WAV PCM) hoặc máy không có thiết bị âm thanh
            System.err.println("[Sound] Không phát được " + name + ".wav: " + e.getMessage());
            MISSING.add(name);
            return null;
        }
    }
}
