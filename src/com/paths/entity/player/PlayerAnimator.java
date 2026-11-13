package com.paths.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;
import com.paths.entity.Direction;

/** Dev 2. Vẽ nhân vật pixel-art trên lưới 16x16 và các hoạt ảnh của nhân vật. */
public class PlayerAnimator {

    private static final int FRAME_DURATION = 8; // 8 frame game = 1 khung hoạt ảnh (~7.5 khung/giây)

    // Bảng màu nhân vật
    private static final Color SKIN = new Color(242, 199, 154);
    private static final Color HAIR = new Color(92, 58, 30);
    private static final Color TUNIC = new Color(58, 143, 92);
    private static final Color TUNIC_DARK = new Color(38, 101, 64);
    private static final Color BELT = new Color(110, 72, 40);
    private static final Color PANTS = new Color(64, 56, 82);
    private static final Color EYE = new Color(30, 30, 40);
    private static final Color SHADOW = new Color(0, 0, 0, 70);
    private static final Color HURT = new Color(230, 60, 60);

    private int counter = 0;
    private int walkFrame = 0;

    // Biến tạm cho lần vẽ hiện tại, để các hàm fill() ngắn gọn
    private Graphics2D g2;
    private int ox, oy;
    private double unit;
    private boolean hurt;

    public void update(boolean moving) {
        if (moving) {
            counter++;
            if (counter >= FRAME_DURATION) {
                counter = 0;
                walkFrame = (walkFrame + 1) % 4;
            }
        } else {
            counter = 0;
            walkFrame = 0;
        }
    }

    /** Vẽ nhân vật tại tọa độ màn hình (sx, sy), kích thước size x size */
    public void draw(Graphics2D g2, int sx, int sy, int size, Direction dir, boolean hurt) {
        this.g2 = g2;
        this.ox = sx;
        this.oy = sy;
        this.unit = size / 16.0;
        this.hurt = hurt;

        int step = switch (walkFrame) {
            case 1 -> 1;
            case 3 -> -1;
            default -> 0;
        };
        int bob = walkFrame % 2 == 1 ? 1 : 0; // Người hơi nhún xuống khi bước

        // Bóng dưới chân
        g2.setColor(SHADOW);
        g2.fillOval(ox + (int) (3 * unit), oy + (int) (13.5 * unit), (int) (10 * unit), (int) (3 * unit));

        // Chân (2 chân lệch nhau khi bước)
        fill(PANTS, 5, 12 + Math.max(0, step), 2, 3 - Math.max(0, step));
        fill(PANTS, 9, 12 + Math.max(0, -step), 2, 3 - Math.max(0, -step));

        // Thân + thắt lưng
        fill(TUNIC, 4, 7 + bob, 8, 5);
        fill(TUNIC_DARK, 4, 11 + bob, 8, 1);
        fill(BELT, 4, 10 + bob, 8, 1);

        // Tay vung ngược chiều chân
        if (dir == Direction.LEFT || dir == Direction.RIGHT) {
            int handX = dir == Direction.LEFT ? 6 : 8;
            fill(SKIN, handX + step, 8 + bob, 2, 3);
        } else {
            fill(SKIN, 3, 8 + bob + Math.max(0, -step), 1, 3);
            fill(SKIN, 12, 8 + bob + Math.max(0, step), 1, 3);
        }

        drawHead(dir, bob);
    }

    private void drawHead(Direction dir, int bob) {
        fill(SKIN, 4, 1 + bob, 8, 6);
        switch (dir) {
            case DOWN -> {
                fill(HAIR, 4, 1 + bob, 8, 2);
                fill(HAIR, 4, 3 + bob, 1, 2);
                fill(HAIR, 11, 3 + bob, 1, 2);
                fill(EYE, 6, 4 + bob, 1, 1);
                fill(EYE, 9, 4 + bob, 1, 1);
            }
            case UP -> fill(HAIR, 4, 1 + bob, 8, 6);
            case LEFT -> {
                fill(HAIR, 4, 1 + bob, 8, 2);
                fill(HAIR, 8, 3 + bob, 4, 3);
                fill(EYE, 5, 4 + bob, 1, 1);
            }
            case RIGHT -> {
                fill(HAIR, 4, 1 + bob, 8, 2);
                fill(HAIR, 4, 3 + bob, 4, 3);
                fill(EYE, 10, 4 + bob, 1, 1);
            }
        }
    }

    /** Tô 1 khối trên lưới 16x16. Khi bị thương thì mọi màu đều ngả sang đỏ */
    private void fill(Color color, double gx, double gy, double gw, double gh) {
        g2.setColor(hurt ? blend(color, HURT) : color);
        int x0 = ox + (int) Math.round(gx * unit);
        int y0 = oy + (int) Math.round(gy * unit);
        int x1 = ox + (int) Math.round((gx + gw) * unit);
        int y1 = oy + (int) Math.round((gy + gh) * unit);
        g2.fillRect(x0, y0, x1 - x0, y1 - y0);
    }

    private static Color blend(Color a, Color b) {
        return new Color((a.getRed() + b.getRed()) / 2, (a.getGreen() + b.getGreen()) / 2,
                (a.getBlue() + b.getBlue()) / 2, a.getAlpha());
    }
}