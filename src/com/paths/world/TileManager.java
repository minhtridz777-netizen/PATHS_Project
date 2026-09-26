package com.paths.world;

import java.awt.Graphics2D;
import com.paths.core.GamePanel;

/**
 * PHỤ TRÁCH: Dev 3
 *
 * Chịu trách nhiệm nạp bản đồ (map) và vẽ toàn bộ tilemap ra màn hình mỗi frame.
 * Bản đồ được lưu dưới dạng file .txt hoặc .csv đơn giản, mỗi số tương ứng 1 loại Tile,
 * đặt trong thư mục res/maps/ (vd map01.txt).
 *
 * TODO (Dev 3) - việc cần làm tiếp theo:
 *  1. Khởi tạo mảng tile[] trong constructor: load ảnh cho từng loại tile
 *     (grass=0, water=1, wall=2...) và đánh dấu collision = true cho tile chặn đường.
 *  2. Viết loadMap(String filePath): đọc file text, mỗi dòng là 1 hàng, mỗi ký tự/số
 *     cách nhau bởi dấu cách là 1 tile -> đổ vào mapTileNum[col][row].
 *  3. Viết draw(): chỉ vẽ những tile đang thực sự nằm trong khung nhìn camera (culling)
 *     để tối ưu hiệu năng, KHÔNG vẽ toàn bộ map nếu map rất lớn.
 */
public class TileManager {

    private static final int MAX_MAP_COL = 50;
    private static final int MAX_MAP_ROW = 50;

    private Tile[] tile; // Bảng các LOẠI tile (index = mã tile)
    private int[][] mapTileNum = new int[MAX_MAP_COL][MAX_MAP_ROW]; // Bản đồ thực tế: ô nào dùng loại tile nào

    public TileManager(GamePanel gamePanel) {
        // TODO (Dev 3): khởi tạo tile = new Tile[SO_LUONG_LOAI_TILE]; rồi getTileImage() cho từng loại.
        // TODO (Dev 3): gọi loadMap("/maps/map01.txt");
    }

    /** Đọc file bản đồ dạng text và đổ dữ liệu vào mapTileNum */
    public void loadMap(String filePath) {
        // TODO (Dev 3): dùng BufferedReader đọc từng dòng trong file resource,
        // split(" ") để lấy từng số, Integer.parseInt() rồi gán vào mapTileNum[col][row].
    }

    public void draw(Graphics2D g2, Camera camera) {
        // TODO (Dev 3): duyệt qua mapTileNum, với mỗi ô tính screenX/screenY dựa vào
        // camera.getX()/getY(), CHỈ vẽ nếu ô đó nằm trong vùng nhìn thấy của màn hình
        // (world x/y trong khoảng [camera.getX() - TILE_SIZE, camera.getX() + SCREEN_WIDTH + TILE_SIZE]).
    }

    /** Kiểm tra 1 tile tại vị trí cột/hàng có chặn đường không - dùng cho CollisionChecker */
    public boolean isTileBlocking(int col, int row) {
        if (col < 0 || col >= MAX_MAP_COL || row < 0 || row >= MAX_MAP_ROW) {
            return true; // Ngoài biên bản đồ -> coi như tường chặn lại
        }
        int tileNum = mapTileNum[col][row];
        return tile != null && tile[tileNum] != null && tile[tileNum].collision;
    }
}
