package com.paths.world;

import java.awt.Color;
import java.awt.Graphics2D;
import com.paths.core.GamePanel;

/** Dev 3. Đọc bản đồ từ res/maps/map01.txt (mỗi số là mã loại ô) và vẽ các ô đang nằm trong màn hình. */
public class TileManager {

    protected int mapCols = 40;
    protected int mapRows = 30;
    protected int[][] mapTileNum = new int[mapCols][mapRows]; // [cột][hàng] -> loại tile

    public TileManager() {
    }

    /** Gọi mỗi frame (dùng cho tile động như nước gợn sóng ở Giai đoạn 3) */
    public void update() {
    }

    public void draw(Graphics2D g2, Camera camera) {
    }

    /** Ô (col, row) có chặn đường không. Ngoài biên bản đồ luôn coi là chặn */
    public boolean isTileBlocking(int col, int row) {
        return col < 0 || row < 0 || col >= mapCols || row >= mapRows;
    }

    /** Loại tile tại ô (col, row). Ngoài biên trả về -1 */
    public int getTileType(int col, int row) {
        if (col < 0 || row < 0 || col >= mapCols || row >= mapRows) {
            return -1;
        }
        return mapTileNum[col][row];
    }

    /** Màu đại diện của 1 loại tile (để vẽ mini-map) */
    public Color getTileColor(int type) {
        return new Color(70, 120, 70);
    }

    public int getMapCols() { return mapCols; }
    public int getMapRows() { return mapRows; }
    public int getMapWidthPx() { return mapCols * GamePanel.TILE_SIZE; }
    public int getMapHeightPx() { return mapRows * GamePanel.TILE_SIZE; }
}
