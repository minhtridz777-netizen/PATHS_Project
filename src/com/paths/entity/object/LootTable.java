package com.paths.entity.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dev 4. Bảng rơi đồ: mỗi dòng (loại, tỉ lệ) được tung xúc xắc độc lập.
 * Ví dụ: new LootTable().add(ItemType.GOLD, 0.6).add(ItemType.POTION, 0.25)
 */
public class LootTable {

    private static final Random RANDOM = new Random();

    /** record: cách viết gọn 1 class chỉ chứa dữ liệu (Java 16+), tự có constructor và getter */
    private record Entry(ItemType type, double chance) {
    }

    private final List<Entry> entries = new ArrayList<>();

    /** chance từ 0.0 (không bao giờ) đến 1.0 (luôn luôn) */
    public LootTable add(ItemType type, double chance) {
        entries.add(new Entry(type, chance));
        return this;
    }

    /** Tung xúc xắc cho từng dòng, trả về các vật phẩm trúng */
    public List<ItemType> roll() {
        List<ItemType> result = new ArrayList<>();
        for (Entry e : entries) {
            if (RANDOM.nextDouble() < e.chance()) {
                result.add(e.type());
            }
        }
        return result;
    }
}
