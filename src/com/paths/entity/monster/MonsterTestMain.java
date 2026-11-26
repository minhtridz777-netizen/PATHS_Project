package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.object.Chest;
import com.paths.entity.object.Item;
import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 4 — BẢN GIAI ĐOẠN 3: thử rơi đồ, rương, nhặt đồ.
 * Chuột phải vào file -> Run 'MonsterTestMain.main()'
 *
 * Player đã chém được (Dev 2 làm ở Giai đoạn 2). Phím E và tự nhặt đồ là việc Dev 2 đang làm
 * song song, nên ở đây dùng phím riêng để thử:
 *   SPACE: chém quái -> xem rơi đồ    O: mở rương gần nhất    P: nhặt mọi đồ trong bán kính 2 ô
 */
public class MonsterTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(6), World.tile(11));

        world.addMonster(new Slime(world, World.tile(9), World.tile(11)));
        world.addMonster(new Slime(world, World.tile(10), World.tile(12)));
        world.addMonster(new Skeleton(world, World.tile(12), World.tile(16)));
        world.addChest(new Chest(world, World.tile(4), World.tile(10), false));
        world.addChest(new Chest(world, World.tile(8), World.tile(9), true));

        TestWindow.open("Quái vật - rơi đồ & rương", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_O)) {
                        for (Chest c : world.getChests()) {
                            if (!c.isOpen() && c.distanceTo(player) < World.tile(3)) {
                                c.onInteract(player);
                                break;
                            }
                        }
                    }
                    if (keys.consumePress(KeyEvent.VK_P)) {
                        for (Item item : new ArrayList<>(world.getItems())) {
                            if (item.distanceTo(player) < World.tile(2)) {
                                item.onInteract(player);
                            }
                        }
                    }
                    world.update();
                },
                g2 -> {
                    world.render(g2);
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRect(0, GamePanel.SCREEN_HEIGHT - 26, GamePanel.SCREEN_WIDTH, 26);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    g2.drawString("SPACE: chém  O: mở rương  P: nhặt đồ  |  Vật phẩm trên đất: "
                            + world.getItems().size() + "  Túi đồ: " + player.getInventory().getItems().size()
                            + "  Vàng: " + player.getGold(), 10, GamePanel.SCREEN_HEIGHT - 8);
                });
    }
}