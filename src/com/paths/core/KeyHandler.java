package com.paths.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dev 2. Ghi nhận trạng thái bàn phím, không xử lý logic game.
 *   upPressed, attackPressed... : true suốt lúc giữ phím (di chuyển, chém)
 *   consumePress(mã phím)       : true đúng 1 lần mỗi lần bấm (E, I, H, P, ENTER, F3)
 * Phím: WASD/mũi tên đi, SPACE/J chém, SHIFT lướt, E tương tác, I túi đồ, H uống máu, P/ESC tạm dừng.
 */
public class KeyHandler implements KeyListener {

    // volatile vì phím được ghi trên luồng Swing nhưng đọc trên luồng game
    public volatile boolean upPressed, downPressed, leftPressed, rightPressed;
    public volatile boolean attackPressed;
    public volatile boolean dashPressed;

    private final Set<Integer> held = ConcurrentHashMap.newKeySet();
    private final Set<Integer> justPressed = ConcurrentHashMap.newKeySet();

    @Override
    public void keyTyped(KeyEvent e) {
        // Không dùng: keyTyped dành cho gõ chữ, không hợp với điều khiển game
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // Giữ phím thì hệ điều hành gửi keyPressed liên tục; chỉ tính lần đầu
        if (held.add(code)) {
            justPressed.add(code);
        }
        setHeld(code, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        held.remove(code);
        setHeld(code, false);
    }

    private void setHeld(int code, boolean value) {
        switch (code) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> upPressed = value;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> downPressed = value;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> leftPressed = value;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> rightPressed = value;
            case KeyEvent.VK_SPACE, KeyEvent.VK_J -> attackPressed = value;
            case KeyEvent.VK_SHIFT -> dashPressed = value;
            default -> { }
        }
    }

    /**
     * Trả về true nếu phím keyCode vừa được bấm (chỉ 1 lần cho mỗi lần bấm).
     * Ví dụ: if (keys.consumePress(KeyEvent.VK_E)) { mở rương }
     */
    public boolean consumePress(int keyCode) {
        return justPressed.remove(keyCode);
    }

    /** Xóa mọi lần bấm chưa xử lý (dùng khi chuyển màn hình để phím không bị "dính" sang màn sau) */
    public void clearPresses() {
        justPressed.clear();
    }
}
