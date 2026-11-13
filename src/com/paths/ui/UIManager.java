package com.paths.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.object.ItemType;
import com.paths.entity.player.Player;

/**
 * Dev 5. Toàn bộ giao diện trên màn hình (HUD), vẽ theo tọa độ màn hình cố định.
 * Nơi khác chỉ cần gọi showMessage(). Khi túi đồ hoặc hội thoại đang mở, isBlockingGameplay() trả về true.
 */
public class UIManager {

    private static final Font HUD_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font MESSAGE_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final int MESSAGE_FRAMES = 150; // 150 frame = 2.5 giây
    private static final int MAX_MESSAGES = 4;

    protected final World world;
    private final HealthBar playerBar = new HealthBar(48, 20, 200, 16);
    private final List<Message> messages = new ArrayList<>();

    /** 1 thông báo: nội dung + số frame còn hiện. Class lồng private vì chỉ UIManager dùng */
    private static class Message {
        final String text;
        int timer = MESSAGE_FRAMES;

        Message(String text) {
            this.text = text;
        }
    }

    public UIManager(World world) {
        this.world = world;
    }

    public void update() {
        for (Message m : messages) {
            m.timer--;
        }
        messages.removeIf(m -> m.timer <= 0);
    }

    public void draw(Graphics2D g2) {
        drawStatusPanel(g2);
        drawMessages(g2);
    }

    /** Bảng góc trên trái: thanh máu, vàng, sức tấn công, số bình máu */
    private void drawStatusPanel(Graphics2D g2) {
        Player player = world.getPlayer();

        g2.setColor(new Color(16, 20, 24, 190));
        g2.fillRoundRect(10, 10, 250, 64, 12, 12);
        g2.setColor(new Color(255, 255, 255, 40));
        g2.drawRoundRect(10, 10, 250, 64, 12, 12);

        g2.setFont(HUD_FONT);
        g2.setColor(new Color(255, 120, 110));
        g2.drawString("HP", 20, 33);
        playerBar.draw(g2, player);

        // Dòng 2: biểu tượng vẽ bằng hình khối + con số
        int rowY = 58;
        drawCoin(g2, 22, rowY - 11);
        g2.setColor(new Color(250, 214, 92));
        g2.drawString(String.valueOf(player.getGold()), 40, rowY);

        drawSwordIcon(g2, 100, rowY - 12);
        g2.setColor(new Color(220, 226, 236));
        g2.drawString("ATK " + player.getAttackPower(), 118, rowY);

        drawPotionIcon(g2, 190, rowY - 13);
        g2.setColor(new Color(255, 150, 160));
        g2.drawString("x" + player.getInventory().countOf(ItemType.POTION), 206, rowY);
    }

    /** Thông báo xếp chồng ở góc dưới trái, mờ dần trong 0.5 giây cuối */
    private void drawMessages(Graphics2D g2) {
        g2.setFont(MESSAGE_FONT);
        int y = GamePanel.SCREEN_HEIGHT - 20;
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message m = messages.get(i);
            int alpha = Math.min(255, m.timer * 255 / 30);
            int w = g2.getFontMetrics().stringWidth(m.text) + 20;
            g2.setColor(new Color(16, 20, 24, Math.min(190, alpha)));
            g2.fillRoundRect(10, y - 18, w, 26, 10, 10);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.drawString(m.text, 20, y);
            y -= 32;
        }
    }

    private void drawCoin(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(214, 160, 40));
        g2.fillOval(x, y, 13, 13);
        g2.setColor(new Color(250, 214, 92));
        g2.fillOval(x + 2, y + 2, 9, 9);
    }

    private void drawSwordIcon(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(200, 210, 224));
        g2.fillRect(x + 6, y, 3, 10);
        g2.setColor(new Color(150, 100, 50));
        g2.fillRect(x + 3, y + 10, 9, 2);
        g2.fillRect(x + 6, y + 12, 3, 3);
    }

    private void drawPotionIcon(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(200, 210, 224));
        g2.fillRect(x + 4, y, 5, 4);
        g2.setColor(new Color(220, 60, 80));
        g2.fillOval(x, y + 3, 13, 12);
    }

    /** Thêm thông báo. Nếu trùng với thông báo mới nhất đang hiện thì chỉ làm mới thời gian */
    public void showMessage(String text) {
        if (!messages.isEmpty()) {
            Message last = messages.get(messages.size() - 1);
            if (last.text.equals(text)) {
                last.timer = MESSAGE_FRAMES;
                return;
            }
        }
        messages.add(new Message(text));
        if (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
    }

    public boolean isInventoryOpen() {
        return false; // Giai đoạn 3
    }

    public void showDialogue(String speaker, List<String> lines) {
        // Giai đoạn 4
    }

    public boolean isDialogueOpen() {
        return false; // Giai đoạn 4
    }

    public boolean isBlockingGameplay() {
        return isInventoryOpen() || isDialogueOpen();
    }
}