package com.paths.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import com.paths.interfaces.IDamageable;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Vẽ thanh máu dựa trên BẤT KỲ đối tượng nào implements IDamageable - không chỉ
 * riêng Player. Nhờ lập trình dựa trên interface (IDamageable) thay vì dựa trên
 * class cụ thể (Player), class này còn tái sử dụng được để vẽ máu Boss (thanh máu to
 * ở đầu màn hình khi đánh Boss) mà không cần sửa code (đây là lợi ích thực tế của
 * TÍNH TRỪU TƯỢNG khi thiết kế UI).
 *
 * TODO (Dev 5):
 *  1. Vẽ khung viền (màu đen/xám) cố định ở góc trên trái màn hình (vd tọa độ 20,20).
 *  2. Vẽ phần máu hiện tại: chiều rộng = (currentHp / maxHp) * chiều rộng tối đa của thanh máu.
 *  3. Đổi màu thanh máu theo % còn lại (vd xanh lá > 50%, vàng 20-50%, đỏ < 20%).
 */
public class HealthBar {

    private final int x, y, width, height;

    public HealthBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2, IDamageable target) {
        double hpPercent = (double) target.getCurrentHp() / target.getMaxHp();

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x, y, width, height); // Nền thanh máu (phần máu đã mất)

        // TODO (Dev 5): đổi màu theo hpPercent như mô tả ở trên
        g2.setColor(Color.RED);
        g2.fillRect(x, y, (int) (width * hpPercent), height);

        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, width, height); // Viền ngoài
    }
}
