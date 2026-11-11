package com.paths.quest;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/** Dev 5. NPC Già làng: nói chuyện để nhận nhiệm vụ và nhận thưởng. Lời thoại theo biến stage. */
public class QuestGiverNpc extends GameObject implements IInteractable {

    protected final World world;

    public QuestGiverNpc(World world, double x, double y) {
        super(x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.world = world;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        g2.setColor(new Color(120, 140, 230));
        g2.fillRect(screenX(camera) + 8, screenY(camera) + 4, width - 16, height - 8);
    }

    @Override
    public void onInteract(Player player) {
    }

    @Override
    public String getInteractionPrompt() {
        return "Nhấn [E] để nói chuyện";
    }
}
