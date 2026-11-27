package com.paths.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.paths.core.World;
import com.paths.entity.GameObject;
import com.paths.entity.monster.Boss;
import com.paths.entity.monster.Monster;
import com.paths.entity.object.Chest;
import com.paths.world.TileManager;

/** Dev 5. Bản đồ nhỏ: vẽ sẵn bản đồ 1 lần vào ảnh 1 pixel mỗi ô, mỗi frame chỉ phóng to và chấm vị trí. */
public class MiniMap {

    private final World world;
    private final int x, y, width, height;
    private BufferedImage mapImage;
    private int blink = 0;

    public MiniMap(World world, int x, int y, int width) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        TileManager tm = world.getTileManager();
        this.height = width * tm.getMapRows() / tm.getMapCols(); // Giữ đúng tỉ lệ bản đồ
    }

    /** Vẽ sẵn bản đồ vào ảnh nhỏ, mỗi ô 1 pixel */
    private void buildMapImage() {
        TileManager tm = world.getTileManager();
        mapImage = new BufferedImage(tm.getMapCols(), tm.getMapRows(), BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < tm.getMapRows(); row++) {
            for (int col = 0; col < tm.getMapCols(); col++) {
                mapImage.setRGB(col, row, tm.getTileColor(tm.getTileType(col, row)).getRGB());
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (mapImage == null) {
            buildMapImage();
        }
        blink++;

        g2.setColor(new Color(16, 20, 24, 200));
        g2.fillRoundRect(x - 5, y - 5, width + 10, height + 10, 10, 10);
        g2.drawImage(mapImage, x, y, width, height, null); // Phóng to ảnh 40x30 -> width x height
        g2.setColor(new Color(0, 0, 0, 60));                // Phủ tối nhẹ để các chấm nổi bật
        g2.fillRect(x, y, width, height);

        for (Chest chest : world.getChests()) {
            if (!chest.isOpen()) {
                dot(g2, chest, new Color(250, 214, 92), 4);
            }
        }
        for (Monster m : world.getMonsters()) {
            if (!m.isDead()) {
                if (m instanceof Boss) {
                    dot(g2, m, new Color(190, 110, 255), 7);
                } else {
                    dot(g2, m, new Color(240, 80, 70), 3);
                }
            }
        }
        if ((blink / 15) % 2 == 0) { // Chấm Player nhấp nháy cho dễ tìm
            dot(g2, world.getPlayer(), Color.WHITE, 5);
        }
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawRect(x, y, width, height);
    }

    private void dot(Graphics2D g2, GameObject obj, Color color, int size) {
        TileManager tm = world.getTileManager();
        int px = x + (int) (obj.getCenterX() * width / tm.getMapWidthPx());
        int py = y + (int) (obj.getCenterY() * height / tm.getMapHeightPx());
        g2.setColor(color);
        g2.fillRect(px - size / 2, py - size / 2, size, size);
    }
}