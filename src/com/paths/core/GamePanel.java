package com.paths.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import com.paths.core.state.GameStateManager;
import com.paths.entity.player.Player;
import com.paths.world.TileManager;
import com.paths.world.Camera;
import com.paths.world.CollisionChecker;
import com.paths.ui.UIManager;

/**
 * PHỤ TRÁCH: Dev 1 (Leader)
 *
 * Đây là class QUAN TRỌNG NHẤT của toàn bộ project - nơi "ráp" tất cả module
 * của 5 Dev lại với nhau. GamePanel implements Runnable để chạy Game Loop
 * trên một Thread riêng biệt, tách khỏi luồng vẽ UI mặc định của Swing (EDT).
 *
 * KIẾN TRÚC GAME LOOP (chuẩn Delta Time - chạy ổn định 60 FPS bất kể máy mạnh/yếu):
 *   while (gameThread != null) {
 *       tính thời gian trôi qua kể từ frame trước (delta)
 *       nếu delta đã tích đủ 1/60 giây -> gọi update() rồi gọi repaint()
 *   }
 *
 * CÁC MODULE ĐƯỢC TÍCH HỢP TẠI ĐÂY (mỗi Dev tự code phần của mình, Dev 1 chỉ gọi ra):
 *   - keyHandler      -> Dev 2 (input bàn phím)
 *   - player          -> Dev 2 (nhân vật chính)
 *   - tileManager     -> Dev 3 (bản đồ / tilemap)
 *   - camera          -> Dev 3 (camera bám nhân vật)
 *   - collisionChecker-> Dev 3 (va chạm)
 *   - uiManager       -> Dev 5 (HUD, inventory, quest)
 *   - stateManager    -> Dev 1 (quản lý Menu/Playing/Pause/GameOver)
 */
public class GamePanel extends JPanel implements Runnable {

    // ====== CẤU HÌNH MÀN HÌNH (Dev 1 quyết định, các Dev khác dùng chung hằng số này) ======
    public static final int ORIGINAL_TILE_SIZE = 16; // 1 ô tile gốc trong file ảnh là 16x16 px
    public static final int SCALE = 3;                // Phóng to 3 lần cho dễ nhìn
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // = 48px/tile lúc hiển thị

    public static final int MAX_SCREEN_COL = 16; // Số cột tile hiển thị trên màn hình
    public static final int MAX_SCREEN_ROW = 12; // Số hàng tile hiển thị trên màn hình
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;   // 768px
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;  // 576px

    private static final int FPS = 60; // Tốc độ khung hình mục tiêu

    // ====== CÁC MODULE CỦA TỪNG DEV (khởi tạo trong constructor) ======
    private Thread gameThread;

    private final KeyHandler keyHandler = new KeyHandler();                 // Dev 2
    private final AssetManager assetManager = AssetManager.getInstance();   // Dev 1 (dùng chung)

    private TileManager tileManager;          // Dev 3 - TODO: khởi tạo khi Dev 3 hoàn thành class
    private Camera camera;                    // Dev 3
    private CollisionChecker collisionChecker; // Dev 3

    private Player player;                    // Dev 2 - TODO: khởi tạo khi Dev 2 hoàn thành class Player

    private UIManager uiManager;              // Dev 5 - TODO: khởi tạo khi Dev 5 hoàn thành class

    private final GameStateManager stateManager = new GameStateManager(); // Dev 1

    public GamePanel() {
        this.setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(java.awt.Color.black);
        this.setDoubleBuffered(true); // Chống giật/xé hình khi vẽ (double buffering của Swing)
        this.addKeyListener(keyHandler); // Gắn KeyHandler (Dev 2) để panel nhận sự kiện bàn phím
        this.setFocusable(true); // Bắt buộc để panel có thể "nghe" phím bấm

        // TODO (Dev 1 + cả team): Khi các class dưới đây đã được code xong,
        // bỏ comment và khởi tạo đúng thứ tự: Map trước -> Player sau (vì Player
        // cần biết vị trí spawn hợp lệ trên map) -> Camera -> UI.
        //
        // tileManager = new TileManager(this);
        // collisionChecker = new CollisionChecker(tileManager);
        // player = new Player(this, keyHandler, collisionChecker);
        // camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT);
        // uiManager = new UIManager(player);
    }

    /** Bắt đầu Game Loop trên Thread riêng. Được gọi 1 lần duy nhất từ Main.java */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Vòng lặp chính của game (Game Loop) - trái tim của mọi game real-time.
     * Sử dụng kỹ thuật "delta time" để đảm bảo game luôn chạy đúng 60 FPS,
     * không phụ thuộc vào tốc độ xử lý của máy người chơi.
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; // Số nano-giây cho mỗi frame (~16.67ms)
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();        // 1. Cập nhật logic (di chuyển, va chạm, AI, hoạt ảnh...)
                repaint();       // 2. Yêu cầu Swing gọi lại paintComponent() để vẽ frame mới
                delta--;
            }
        }
    }

    /**
     * Cập nhật logic mỗi frame. Đây là nơi Dev 1 gọi update() của TỪNG module.
     * Thứ tự cực kỳ quan trọng: Input -> Logic nhân vật/quái -> Va chạm -> Camera -> UI.
     */
    public void update() {
        // TODO (Dev 1): Ủy quyền update() theo State hiện tại (Menu / Playing / Pause).
        // Ví dụ khi đang PlayState:
        //   player.update();                 // Dev 2: xử lý di chuyển + tấn công dựa trên keyHandler
        //   monsterManager.updateAll();       // Dev 4: cập nhật AI của tất cả quái
        //   camera.update(player);            // Dev 3: camera bám theo player
        //   questManager.checkConditions();   // Dev 5: kiểm tra điều kiện hoàn thành quest
        stateManager.update();
    }

    /**
     * Vẽ toàn bộ game mỗi frame. KHÔNG được xử lý logic ở đây (chỉ vẽ!).
     * Thứ tự vẽ (layer) rất quan trọng để không bị đè hình sai lớp:
     *   1. Tilemap (nền)  -> Dev 3
     *   2. Vật phẩm/Chest -> Dev 4
     *   3. Quái vật       -> Dev 4
     *   4. Player         -> Dev 2
     *   5. UI/HUD (luôn vẽ trên cùng, KHÔNG bị camera ảnh hưởng) -> Dev 5
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // TODO (Dev 1): Gọi render theo State hiện tại, ví dụ:
        // tileManager.draw(g2, camera);
        // for (Item item : items) item.draw(g2, camera);
        // for (Chest chest : chests) chest.draw(g2, camera);
        // for (Monster m : monsters) m.draw(g2, camera);
        // player.draw(g2, camera);
        // uiManager.draw(g2); // UI vẽ theo tọa độ màn hình cố định, KHÔNG trừ camera offset

        g2.dispose(); // Giải phóng tài nguyên Graphics2D sau khi vẽ xong (tránh leak bộ nhớ)
    }

    // Getter để các class khác (Player, Monster...) truy cập được panel khi cần (vd lấy KeyHandler)
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
}
