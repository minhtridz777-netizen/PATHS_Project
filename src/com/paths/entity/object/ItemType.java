package com.paths.entity.object;

/** Dev 4. Các loại vật phẩm. storable = cất vào túi đồ; loại còn lại nhặt là cộng vàng ngay. */
public enum ItemType {
    POTION("Bình máu", "Hồi 30 máu. Nhấn H để uống.", true),
    GOLD("Túi vàng", "+10 vàng", false),
    GEM("Ngọc quý", "+50 vàng", false),
    SWORD("Kiếm sắt", "+5 sức tấn công", true);

    private final String displayName;
    private final String description;
    private final boolean storable;

    ItemType(String displayName, String description, boolean storable) {
        this.displayName = displayName;
        this.description = description;
        this.storable = storable;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public boolean isStorable() { return storable; }
}
