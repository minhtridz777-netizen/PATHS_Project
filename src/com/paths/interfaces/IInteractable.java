package com.paths.interfaces;

import com.paths.entity.player.Player;

/**
 * Dev 1. Bất cứ thứ gì người chơi tương tác được (vật phẩm, rương, NPC).
 * Player chỉ gọi onInteract(), mỗi class tự quyết định làm gì.
 */
public interface IInteractable {

    /** Người chơi tương tác với vật thể này */
    void onInteract(Player player);

    /** Dòng gợi ý hiện trên màn hình, vd "Nhấn [E] để mở rương" */
    String getInteractionPrompt();

    /** Còn tương tác được không (vd rương đã mở rồi thì false). Mặc định là true */
    default boolean canInteract() {
        return true;
    }
}
