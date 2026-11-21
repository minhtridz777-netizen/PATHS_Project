package com.paths.entity.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import com.paths.entity.Direction;

/** Dev 2. Vẽ nhân vật pixel-art trên lưới 16x16 và các hoạt ảnh của nhân vật. */
public class PlayerAnimator {

    private static final int FRAME_DURATION = 8;

    private static final Color SKIN = new Color(242, 199, 154);
    private static final Color HAIR = new Color(92, 58, 30);
    private static final Color TUNIC = new Color(58, 143, 92);
    private static final Color TUNIC_DARK = new Color(38, 101, 64);
    private static final Color BELT = new Color(110, 72, 40);
    private static final Color PANTS = new Color(64, 56, 82);
    private static final Color EYE = new Color(30, 30, 40);
    private static final Color SHADOW = new Color(0, 0, 0, 70);
    private static final Color HURT = new Color(230, 60, 60);
    private static final Color BLADE = new Color(222, 230, 240);
    private static final Color HILT = new Color(150, 100, 50);

    private int counter = 0;
    private int walkFrame = 0;

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

    /**
     * @param attackProgress -1 nếu không chém, 0.0 -> 1.0 nếu đang chém
     * @param dead           true thì vẽ nằm gục
     */
    public void draw(Graphics2D g2, int sx, int sy, int size, Direction dir,
                     boolean hurt, double attackProgress, boolean dead) {
        this.g2 = g2;
        this.ox = sx;
        this.oy = sy;
        this.unit = size / 16.0;
        this.hurt = hurt || dead;

        if (dead) {
            AffineTransform saved = g2.getTransform();
            g2.rotate(Math.PI / 2, sx + size / 2.0, sy + size / 2.0);
            drawBody(Direction.DOWN, 0, 0);
            g2.setTransform(saved);
            return;
        }

        boolean attacking = attackProgress >= 0;
        // Chém lên trên thì kiếm nằm SAU người -> vẽ kiếm trước rồi vẽ người đè lên
        if (attacking && dir == Direction.UP) {
            drawSword(sx, sy, size, dir, attackProgress);
        }
        int step = attacking ? 0 : switch (walkFrame) {
            case 1 -> 1;
            case 3 -> -1;
            default -> 0;
        };
        int bob = !attacking && walkFrame % 2 == 1 ? 1 : 0;
        drawBody(dir, step, bob);
        if (attacking && dir != Direction.UP) {
            drawSword(sx, sy, size, dir, attackProgress);
        }
    }

    private void drawBody(Direction dir, int step, int bob) {
        g2.setColor(SHADOW);
        g2.fillOval(ox + (int) (3 * unit), oy + (int) (13.5 * unit), (int) (10 * unit), (int) (3 * unit));

        fill(PANTS, 5, 12 + Math.max(0, step), 2, 3 - Math.max(0, step));
        fill(PANTS, 9, 12 + Math.max(0, -step), 2, 3 - Math.max(0, -step));

        fill(TUNIC, 4, 7 + bob, 8, 5);
        fill(TUNIC_DARK, 4, 11 + bob, 8, 1);
        fill(BELT, 4, 10 + bob, 8, 1);

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

    /** Vẽ lưỡi kiếm quét 1 cung 140 độ trước mặt */
    private void drawSword(int sx, int sy, int size, Direction dir, double progress) {
        double base = switch (dir) {
            case RIGHT -> 0;
            case DOWN -> Math.PI / 2;
            case LEFT -> Math.PI;
            case UP -> -Math.PI / 2;
        };
        double sweep = Math.toRadians(140);
        double angle = base - sweep / 2 + sweep * progress;

        double cx = sx + size / 2.0;
        double cy = sy + size * 0.62;
        double length = size * 0.75;

        // Vệt chém mờ dần theo thời gian
        int alpha = (int) (110 * (1 - progress));
        g2.setColor(new Color(255, 255, 255, Math.max(0, alpha)));
        double startDeg = -Math.toDegrees(base - sweep / 2);
        double extentDeg = -Math.toDegrees(sweep * progress);
        g2.fill(new Arc2D.Double(cx - length, cy - length, length * 2, length * 2, startDeg, extentDeg, Arc2D.PIE));

        Stroke saved = g2.getStroke();
        double tipX = cx + Math.cos(angle) * length;
        double tipY = cy + Math.sin(angle) * length;
        double hiltX = cx + Math.cos(angle) * size * 0.2;
        double hiltY = cy + Math.sin(angle) * size * 0.2;
        g2.setStroke(new BasicStroke((float) (unit * 1.6f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(BLADE);
        g2.drawLine((int) hiltX, (int) hiltY, (int) tipX, (int) tipY);
        g2.setColor(HILT);
        g2.drawLine((int) cx, (int) cy, (int) hiltX, (int) hiltY);
        g2.setStroke(saved);
    }

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