package com.paths.ui;

import java.awt.Graphics2D;
import java.util.List;
import com.paths.core.World;

/**
 * Dev 5. Toàn bộ giao diện trên màn hình (HUD), vẽ theo tọa độ màn hình cố định.
 * Nơi khác chỉ cần gọi showMessage(). Khi túi đồ hoặc hội thoại đang mở, isBlockingGameplay() trả về true.
 */
public class UIManager {

    protected final World world;

    public UIManager(World world) {
        this.world = world;
    }

    public void update() {
    }

    public void draw(Graphics2D g2) {
    }

    /** Hiện 1 dòng thông báo ngắn ở góc màn hình vài giây */
    public void showMessage(String text) {
    }

    public boolean isInventoryOpen() {
        return false;
    }

    /** Hiện khung hội thoại. lines là các câu thoại, bấm E/ENTER để sang câu tiếp */
    public void showDialogue(String speaker, List<String> lines) {
    }

    public boolean isDialogueOpen() {
        return false;
    }

    /** true khi giao diện đang chiếm màn hình (túi đồ, hội thoại) -> thế giới game tạm đứng yên */
    public boolean isBlockingGameplay() {
        return isInventoryOpen() || isDialogueOpen();
    }
}
