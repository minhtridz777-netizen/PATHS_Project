package com.paths.ui;

import java.awt.Graphics2D;
import com.paths.entity.player.Player;
import com.paths.inventory.Inventory;
import com.paths.quest.QuestManager;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Điểm tổng hợp toàn bộ giao diện (HUD) của Dev 5: thanh máu, mini-map, bảng
 * inventory, bảng quest log... GamePanel (Dev 1) CHỈ gọi duy nhất uiManager.draw(g2)
 * mỗi frame, không cần biết bên trong có bao nhiêu thành phần UI - đây cũng là ví dụ
 * của tính ĐÓNG GÓI (Encapsulation): chi tiết hiển thị HUD được giấu kín bên trong
 * UIManager, các module khác chỉ thấy 1 method draw() duy nhất.
 *
 * TODO (Dev 5):
 *  1. Khởi tạo healthBar, miniMap ở vị trí cố định trên màn hình (góc trên trái/phải).
 *  2. Thêm cờ boolean isInventoryOpen; khi keyHandler báo phím "I" được bấm (phối hợp
 *     Dev 2) thì đảo trạng thái cờ này để mở/đóng bảng túi đồ.
 *  3. Vẽ thêm khung thoại (dialog box) khi đứng gần NPC hoặc rương (dùng
 *     getInteractionPrompt() từ IInteractable - phối hợp Dev 1/Dev 4).
 */
public class UIManager {

    private final Player player;
    private final HealthBar healthBar;
    private final MiniMap miniMap;
    private final Inventory inventory; // Dev 5
    private final QuestManager questManager; // Dev 5

    public UIManager(Player player) {
        this.player = player;
        this.healthBar = new HealthBar(20, 20, 200, 20);
        this.miniMap = new MiniMap(600, 20, 150, 150);
        this.inventory = new Inventory(20);
        this.questManager = new QuestManager();
    }

    public void draw(Graphics2D g2) {
        healthBar.draw(g2, player); // Player implements IDamageable nên truyền thẳng được vào đây
        miniMap.draw(g2, player);

        // TODO (Dev 5): nếu isInventoryOpen thì vẽ thêm bảng inventory ở giữa màn hình.
        // TODO (Dev 5): vẽ danh sách quest đang active ở góc màn hình (vd góc dưới trái).
    }

    public Inventory getInventory() {
        return inventory;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }
}
