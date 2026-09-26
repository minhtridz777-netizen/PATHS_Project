package com.paths.inventory;

import java.util.ArrayList;
import java.util.List;
import com.paths.entity.object.Item;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Túi đồ của người chơi: danh sách các Item đã nhặt được, có giới hạn số ô (capacity).
 * Được Item.onInteract() (Dev 4) gọi tới khi người chơi nhặt đồ, và được UIManager
 * (Dev 5) đọc để vẽ ra màn hình dạng lưới ô vuông.
 *
 * TODO (Dev 5):
 *  1. addItem(): nếu inventory đầy (items.size() >= capacity) thì không cho thêm, có thể
 *     hiện thông báo "Túi đồ đã đầy" trên UI.
 *  2. useItem(index): dùng vật phẩm tại vị trí index (vd uống potion hồi máu cho Player),
 *     sau đó removeItem tương ứng.
 *  3. (Nâng cao) gộp các item cùng loại lại thành 1 ô kèm số lượng (stack) thay vì
 *     mỗi item chiếm riêng 1 ô.
 */
public class Inventory {

    private final List<Item> items = new ArrayList<>();
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    public boolean addItem(Item item) {
        if (items.size() >= capacity) {
            return false; // Túi đồ đầy
        }
        return items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }
}
