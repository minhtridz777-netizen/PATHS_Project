package com.paths.interfaces;

import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 1 (thiết kế), dùng chung bởi Dev 4 (Chest, NPC) và Dev 5 (bục nhận quest...)
 *
 * Interface đại diện cho "bất kỳ vật thể nào người chơi có thể bấm phím tương tác (E) để dùng".
 * Ví dụ: Chest (rương kho báu), NPC (nhận nhiệm vụ), cổng dịch chuyển, bia đá chỉ dẫn...
 *
 * TÍNH TRỪU TƯỢNG: PlayState (Dev 1) khi phát hiện player đứng gần 1 object và bấm E,
 * KHÔNG cần biết object đó là Chest hay NPC, chỉ cần ép kiểu về IInteractable rồi gọi:
 *
 *      if (nearbyObject instanceof IInteractable target) {
 *          target.onInteract(player);
 *      }
 *
 * ĐA HÌNH: Chest.onInteract() sẽ mở nắp + rớt item, còn NPC.onInteract() (nếu làm thêm)
 * sẽ mở hội thoại/giao nhiệm vụ - cùng 1 lời gọi nhưng 2 hành vi khác nhau.
 */
public interface IInteractable {

    /** Được gọi khi Player đứng đủ gần và bấm phím tương tác */
    void onInteract(Player player);

    /** Dòng chữ gợi ý hiển thị trên UI, vd "Nhấn [E] để mở rương" (Dev 5 dùng để vẽ HUD) */
    String getInteractionPrompt();
}
