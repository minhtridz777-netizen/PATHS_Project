package com.paths.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.paths.world.Camera;

/**
 * Dev 1. Lớp cha của mọi vật thể: vị trí, kích thước, update(), draw(), hitbox.
 * x, y là tọa độ thế giới (pixel, góc trên trái). Vẽ lên màn hình thì dùng screenX/screenY(camera).
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected BufferedImage sprite; // Ảnh (tùy chọn). Không có ảnh thì class con tự vẽ bằng hình khối

    protected GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** Cập nhật logic mỗi frame */
    public abstract void update();

    /** Vẽ lên màn hình mỗi frame */
    public abstract void draw(Graphics2D g2, Camera camera);

    /**
     * Khung va chạm (hitbox). Mặc định là cả khung ảnh.
     * Class con có thể override để thu nhỏ (vd Player chỉ lấy phần thân, cảm giác va chạm tự nhiên hơn).
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /** Vị trí trên màn hình = vị trí thế giới - vị trí camera */
    public int screenX(Camera camera) {
        return (int) Math.round(x) - camera.getX();
    }

    public int screenY(Camera camera) {
        return (int) Math.round(y) - camera.getY();
    }

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }

    /** Khoảng cách giữa tâm 2 vật thể (pixel) */
    public double distanceTo(GameObject other) {
        return Math.hypot(other.getCenterX() - getCenterX(), other.getCenterY() - getCenterY());
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public BufferedImage getSprite() { return sprite; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
