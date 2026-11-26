package com.paths.entity.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.object.Chest;
import com.paths.entity.object.Item;
import com.paths.entity.object.ItemType;
import com.paths.interfaces.IInteractable;

/**
 * PHỤ TRÁCH: Dev 2 — BẢN GIAI ĐOẠN 3: thử bấm E và tự nhặt đồ.
 * Chuột phải vào file -> Run 'PlayerTestMain.main()'
 *
 * Rương và vật phẩm ở nhánh của Dev 2 vẫn là BẢN KHUNG (Dev 4 đang làm bản đẹp song song):
 * rương là ô nâu, vật phẩm là ô vàng. Chỉ cần kiểm tra:
 *   - Đứng gần rương thấy dòng "Gần: ..." ở dưới, bấm E thì rương chuyển màu tối (đã mở).
 *   - Đi chạm ô vàng thì ô biến mất (đã nhặt).......
 */
public class PlayerTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(6), World.tile(11));

        world.addChest(new Chest(world, World.tile(9), World.tile(11), false));
        world.addChest(new Chest(world, World.tile(4), World.tile(12), true));
        world.addItem(new Item(World.tile(7), World.tile(13), ItemType.GOLD));
        world.addItem(new Item(World.tile(9), World.tile(13), ItemType.POTION));
        world.addItem(new Item(World.tile(11), World.tile(13), ItemType.SWORD));

        TestWindow.open("Player - tương tác", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_U)) player.addAttackBonus(5); // Giả lập nhặt Kiếm sắt
                    world.update();
                },
                g2 -> {
                    world.render(g2);
                    drawInfo(g2, world, player);
                });
    }

    private static void drawInfo(Graphics2D g2, World world, Player p) {
        IInteractable near = world.findNearestInteractable(p, GamePanel.TILE_SIZE * 1.3);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, GamePanel.SCREEN_HEIGHT - 26, GamePanel.SCREEN_WIDTH, 26);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("E: tương tác   U: giả lập nhặt kiếm   |   Gần: " + (near == null ? "(không có)" : near.getInteractionPrompt())
                        + "   |   Vật phẩm còn trên đất: " + world.getItems().size(),
                10, GamePanel.SCREEN_HEIGHT - 8);
    }
}