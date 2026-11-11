package com.paths.inventory;

import java.util.ArrayList;
import java.util.List;

import com.paths.entity.object.Item;
import com.paths.entity.object.ItemType;
import com.paths.entity.player.Player;

/** Dev 5. Túi đồ có giới hạn số ô. Thêm đồ qua addItem(), dùng đồ qua useItemOfType(). */
public class Inventory {

    protected final List<Item> items = new ArrayList<>();
    protected final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
    }

    /** Thêm vật phẩm. Trả về false nếu túi đầy */
    public boolean addItem(Item item) {
        if (isFull()) {
            return false;
        }
        items.add(item);
        return true;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    /** Dùng 1 vật phẩm loại type (vd uống 1 Bình máu). Trả về true nếu dùng được */
    public boolean useItemOfType(ItemType type, Player player) {
        return false;
    }

    public int countOf(ItemType type) {
        int count = 0;
        for (Item item : items) {
            if (item.getType() == type) {
                count++;
            }
        }
        return count;
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }
}
