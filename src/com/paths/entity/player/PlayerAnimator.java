package com.paths.entity.player;

import java.awt.image.BufferedImage;
import com.paths.core.AssetManager;
import com.paths.entity.Direction;

/**
 * PHỤ TRÁCH: Dev 2
 *
 * Quản lý hoạt ảnh (animation) đi bộ của Player: đổi qua lại giữa các frame
 * sprite (vd chân trái/phải bước) theo 1 chu kỳ đếm thời gian, dựa trên
 * hướng di chuyển hiện tại.
 *
 * TODO (Dev 2):
 *  1. Chuẩn bị sprite sheet hoặc các file ảnh riêng lẻ đặt trong res/player/
 *     (vd player_down_1.png, player_down_2.png, player_up_1.png...).
 *  2. Load toàn bộ ảnh này trong constructor bằng AssetManager.getInstance().getImage(...).
 *  3. Trong update(), nếu isMoving == true thì tăng spriteCounter, khi đạt ngưỡng
 *     (vd 10 frame) thì đổi spriteNum (1 -> 2 -> 3 -> 1...) để tạo cảm giác bước chân.
 *  4. Nếu !isMoving, luôn trả về frame đứng yên (idle) theo đúng hướng.
 */
public class PlayerAnimator {

    private int spriteCounter = 0;
    private int spriteNum = 1; // Hiện đang ở frame thứ mấy trong chu kỳ đi bộ (vd 1,2,3)
    private static final int ANIMATION_SPEED = 10; // Số frame game giữ nguyên 1 sprite trước khi đổi

    private BufferedImage currentFrame;

    public PlayerAnimator() {
        // TODO (Dev 2): load sẵn các ảnh cần thiết ở đây, ví dụ:
        // AssetManager am = AssetManager.getInstance();
        // downIdle = am.getImage("/player/player_down_1.png");
        // ...
    }

    public void update(boolean isMoving, Direction direction) {
        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > ANIMATION_SPEED) {
                spriteNum = (spriteNum % 3) + 1; // Xoay vòng 1 -> 2 -> 3 -> 1
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // Đứng yên -> luôn dùng frame idle
        }

        // TODO (Dev 2): dựa vào (direction, spriteNum) chọn đúng BufferedImage đã load
        // và gán vào currentFrame. Hiện tại để null tạm để không lỗi compile.
        currentFrame = null;
    }

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }
}
