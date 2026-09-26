package com.paths.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * PHỤ TRÁCH: Dev 2
 *
 * Lắng nghe sự kiện bàn phím từ Swing và lưu lại trạng thái "đang giữ phím" dưới
 * dạng các biến boolean. Class Player sẽ ĐỌC các biến boolean này mỗi frame trong
 * update() để quyết định di chuyển/tấn công - KHÔNG xử lý logic di chuyển ở đây,
 * KeyHandler chỉ có nhiệm vụ "ghi nhận trạng thái phím", tách biệt rõ Input ra khỏi Logic.
 *
 * TODO (Dev 2):
 *  1. Thêm phím tương tác (vd phím E hoặc F) để mở rương / nói chuyện NPC -> cờ interactPressed.
 *  2. Thêm phím mở Inventory (vd phím I) -> báo cho UIManager (Dev 5) biết để mở/đóng bảng túi đồ.
 *  3. Cân nhắc dùng KeyBindings thay cho KeyListener nếu gặp vấn đề mất focus.
 */
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean attackPressed;
    public boolean interactPressed; // TODO (Dev 2): gán true khi nhấn phím tương tác (vd E)

    @Override
    public void keyTyped(KeyEvent e) {
        // Không dùng - keyTyped chỉ bắt ký tự (text input), không phù hợp cho điều khiển game
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_SPACE -> attackPressed = true;
            case KeyEvent.VK_E -> interactPressed = true;
            default -> { /* Phím khác chưa xử lý */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_SPACE -> attackPressed = false;
            case KeyEvent.VK_E -> interactPressed = false;
            default -> { /* Phím khác chưa xử lý */ }
        }
    }
}
