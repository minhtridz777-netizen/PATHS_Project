package com.paths.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 1 (thiết kế nền tảng), TOÀN BỘ TEAM kế thừa từ đây.
 *
 * Đây là class CHA CAO NHẤT của mọi vật thể xuất hiện trong thế giới game:
 * Player, Monster, Item, Chest... tất cả đều LÀ MỘT (is-a) GameObject.
 * Vì vậy các thuộc tính chung nhất (vị trí, kích thước, sprite) được đặt tại đây
 * để tránh lặp code (nguyên tắc DRY - Don't Repeat Yourself).
 *
 * ĐÂY LÀ VÍ DỤ VỀ TÍNH TRỪU TƯỢNG (Abstraction):
 * GameObject là abstract class - không ai new GameObject() trực tiếp cả, nó chỉ định
 * nghĩa "khung sườn" chung. Các method update()/draw() được khai báo abstract vì
 * MỖI loại vật thể con sẽ tự quyết định cách cập nhật/vẽ khác nhau hoàn toàn.
 *
 * dùng kiểu double cho x,y (thay vì int) để hỗ trợ di chuyển mượt (sub-pixel movement)
 * khi nhân vật đi chéo hoặc tốc độ lẻ (vd 2.5 px/frame).
 */
public abstract class GameObject {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected BufferedImage sprite; // Ảnh hiện tại đang được vẽ (frame animation hiện hành)

    protected GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Cập nhật logic của vật thể mỗi frame (di chuyển, đếm cooldown, đổi frame animation...).
     * Method abstract - bắt buộc mọi class con phải tự định nghĩa (TÍNH TRỪU TƯỢNG).
     */
    public abstract void update();

    /**
     * Vẽ vật thể lên màn hình. Nhận vào Camera để tính toán tọa độ hiển thị thực tế
     * = tọa độ thế giới (world coordinate) trừ đi độ lệch camera (Dev 3 cung cấp Camera).
     */
    public abstract void draw(Graphics2D g2, Camera camera);

    /**
     * Trả về "hộp va chạm" (hitbox) của vật thể dùng cho CollisionChecker (Dev 3).
     * Mặc định dùng toàn bộ width/height, class con có thể override để thu nhỏ hitbox
     * (vd chỉ lấy phần chân nhân vật để cảm giác va chạm tự nhiên hơn).
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public BufferedImage getSprite() { return sprite; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
