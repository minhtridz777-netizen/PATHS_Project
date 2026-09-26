package com.paths.entity;

/**
 * PHỤ TRÁCH: Dev 1 (thiết kế), dùng chung bởi Dev 2 (Player) và Dev 4 (Monster)
 *
 * Enum đại diện cho hướng nhìn/di chuyển hiện tại của một Entity.
 * Dùng enum thay vì String ("up","down"...) hoặc int (0,1,2,3) để:
 *  - Tránh gõ nhầm chính tả (compiler sẽ báo lỗi ngay nếu gõ sai).
 *  - Code dễ đọc hơn: Direction.UP rõ ràng hơn hẳn số 0.
 * Dùng để chọn đúng bộ sprite animation tương ứng (vd sprite quay lưng khi UP).
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT
}
