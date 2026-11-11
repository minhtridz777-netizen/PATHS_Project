package com.paths.entity.object;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/** Dev 4. Rương kho báu: bấm E để mở, vật phẩm văng ra xung quanh. Rương vàng (golden) chắc chắn có Kiếm sắt. */
public class Chest extends GameObject implements IInteractable {

    protected final World world;
    private final boolean golden;
    private boolean open = false;

    public Chest(World world, double x, double y, boolean golden) {
        super(x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.world = world;
        this.golden = golden;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        g2.setColor(open ? new Color(90, 60, 30) : new Color(150, 100, 40));
        g2.fillRect(screenX(camera) + 6, screenY(camera) + 12, width - 12, height - 18);
    }

    @Override
    public void onInteract(Player player) {
        open = true; // Bản khung: chỉ đánh dấu là đã mở
    }

    @Override
    public String getInteractionPrompt() {
        return "Nhấn [E] để mở rương";
    }

    @Override
    public boolean canInteract() {
        return !open;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isGolden() {
        return golden;
    }
}
