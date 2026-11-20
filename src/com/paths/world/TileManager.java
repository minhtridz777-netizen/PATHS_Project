package com.paths.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import com.paths.core.GamePanel;

/** Dev 3. Đọc bản đồ từ res/maps/map01.txt (mỗi số là mã loại ô) và vẽ các ô đang nằm trong màn hình. */
public class TileManager {

    protected int mapCols = 40;
    protected int mapRows = 30;
    protected int[][] mapTileNum = new int[mapCols][mapRows]; // [cột][hàng] -> loại tile

    // Mảng lưu ảnh đại diện cho từng mã loại ô (0 -> 7)
    private final BufferedImage[] tileImages = new BufferedImage[10];

    public TileManager() {
        loadTileImages();
        loadMap("/maps/map01.txt");
    }

    /** Nạp 1 ảnh đại diện cho mỗi loại địa hình cơ bản từ thư mục res/tiles/ */
    private void loadTileImages() {
        tileImages[0] = loadImage("grass_0"); // 0: Cỏ
        tileImages[1] = loadImage("wall_b");  // 1: Tường đá
        tileImages[2] = loadImage("water_c"); // 2: Nước
        tileImages[3] = loadImage("tree_0");  // 3: Cây
        tileImages[4] = loadImage("dirt_c");  // 4: Đường đất
        tileImages[5] = loadImage("sand_0");  // 5: Cát
        tileImages[6] = loadImage("floor_c"); // 6: Sàn hầm ngục
        tileImages[7] = loadImage("flower");  // 7: Hoa
    }

    /** Hàm đọc 1 file ảnh an toàn */
    private BufferedImage loadImage(String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream("/tiles/" + fileName + ".png");
            if (is != null) {
                return ImageIO.read(is);
            }
            System.err.println("[TileManager] Khong tim thay anh: /tiles/" + fileName + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Đọc file bản đồ map01.txt vào mảng mapTileNum[col][row] */
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.err.println("[TileManager] Khong tim thay file map: " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int row = 0;
            while (row < mapRows) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.trim().split("\\s+");
                for (int col = 0; col < mapCols && col < numbers.length; col++) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Gọi mỗi frame (dùng cho tile động như nước gợn sóng ở Giai đoạn 3) */
    public void update() {
    }

    /** Vẽ các ô địa hình đang nằm trong khung nhìn của Camera */
    public void draw(Graphics2D g2, Camera camera) {
        int tileSize = GamePanel.TILE_SIZE;

        // Tính giới hạn cột/hàng đang hiển thị trên màn hình để tối ưu hiệu năng
        int startCol = Math.max(0, camera.getX() / tileSize);
        int endCol   = Math.min(mapCols - 1, (camera.getX() + GamePanel.SCREEN_WIDTH) / tileSize + 1);
        int startRow = Math.max(0, camera.getY() / tileSize);
        int endRow   = Math.min(mapRows - 1, (camera.getY() + GamePanel.SCREEN_HEIGHT) / tileSize + 1);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int type = mapTileNum[col][row];
                int screenX = col * tileSize - camera.getX();
                int screenY = row * tileSize - camera.getY();

                // Vẽ ảnh nếu có, ngược lại dùng ô màu đại diện
                if (type >= 0 && type < tileImages.length && tileImages[type] != null) {
                    g2.drawImage(tileImages[type], screenX, screenY, tileSize, tileSize, null);
                } else {
                    g2.setColor(getTileColor(type));
                    g2.fillRect(screenX, screenY, tileSize, tileSize);
                }
            }
        }
    }

    /** Ô (col, row) có chặn đường không. Ngoài biên bản đồ luôn coi là chặn */
    public boolean isTileBlocking(int col, int row) {
        if (col < 0 || row < 0 || col >= mapCols || row >= mapRows) {
            return true;
        }
        int type = mapTileNum[col][row];
        // Tường (1), Nước (2) và Cây (3) sẽ chặn nhân vật đi qua
        return type == 1 || type == 2 || type == 3;
    }

    /** Loại tile tại ô (col, row). Ngoài biên trả về -1 */
    public int getTileType(int col, int row) {
        if (col < 0 || row < 0 || col >= mapCols || row >= mapRows) {
            return -1;
        }
        return mapTileNum[col][row];
    }

    /** Màu đại diện của 1 loại tile (để vẽ mini-map hoặc dùng khi thiếu ảnh) */
    public Color getTileColor(int type) {
        switch (type) {
            case 0:  return new Color(70, 120, 70);   // Cỏ
            case 1:  return new Color(100, 100, 110); // Tường
            case 2:  return new Color(50, 130, 210);  // Nước
            case 3:  return new Color(30, 90, 40);    // Cây
            case 4:  return new Color(150, 110, 70);  // Đường đất
            case 5:  return new Color(220, 200, 130); // Cát
            case 6:  return new Color(80, 80, 90);    // Sàn
            case 7:  return new Color(220, 90, 130);  // Hoa
            default: return new Color(70, 120, 70);
        }
    }

    public int getMapCols() { return mapCols; }
    public int getMapRows() { return mapRows; }
    public int getMapWidthPx() { return mapCols * GamePanel.TILE_SIZE; }
    public int getMapHeightPx() { return mapRows * GamePanel.TILE_SIZE; }
}
