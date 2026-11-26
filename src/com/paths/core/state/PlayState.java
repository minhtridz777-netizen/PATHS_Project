package com.paths.core.state;

import java.awt.Graphics2D;
import com.paths.core.World;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import com.paths.core.GamePanel;
import com.paths.entity.GameObject;
import com.paths.entity.monster.Monster;
import com.paths.entity.player.Player;
import com.paths.world.Camera;

/** Dev 1. Màn hình đang chơi: tạo World khi bắt đầu ván rồi giao cho World update/vẽ mỗi frame. */
public class PlayState implements GameState {
    private static final Font DEBUG_FONT = new Font("Monospaced", Font.BOLD, 13);
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 44);
    private static final Font HINT_FONT = new Font("SansSerif", Font.PLAIN, 18);
    private static final int DEATH_DELAY = 60;
    private static final int HELP_FRAMES = 360;
    private static final String[] HELP_LINES = {
            "WASD / mũi tên : di chuyển", "SPACE : chém", "E : mở rương / nói chuyện",
            "I : túi đồ      H : uống bình máu", "F3 : chế độ debug"};
    private final GameStateManager manager;
    private World world;
    private boolean debug = false;
    private int deathTimer = 0;
    private int helpTimer = 0;

    public PlayState(GameStateManager manager) {
        this.manager = manager;
    }

    @Override
    public void onEnter() {
        if (world == null) { // Chỉ tạo World ở lần vào đầu tiên, quay lại từ Pause thì giữ nguyên
            startNewGame();
        }
    }

    private void startNewGame() {
        world = new World(manager.getGamePanel().getKeyHandler());
        world.spawnLevel();
        deathTimer = 0;
        helpTimer = HELP_FRAMES;
    }

    @Override
    public void update() {
        if (world.getKeyHandler().consumePress(KeyEvent.VK_F3)) {
            debug = !debug;
        }
        world.update();
        if (helpTimer > 0) {
            helpTimer--;
        }

        if (world.getPlayer().isDead()) {
            deathTimer++;
            if (deathTimer > DEATH_DELAY && world.getKeyHandler().consumePress(KeyEvent.VK_ENTER)) {
                startNewGame();
            }
        }
    }

    @Override
    public void render(Graphics2D g2) {
        world.render(g2);
        if (debug) {
            drawDebug(g2);
        }
        if (helpTimer > 0) {
            drawHelp(g2);
        }
        if (deathTimer > DEATH_DELAY) {
            drawDeathScreen(g2);
        }
    }

    /** Bảng phím điều khiển ở giữa phía dưới, mờ dần trong 60 frame cuối */
    private void drawHelp(Graphics2D g2) {
        int alpha = Math.min(255, helpTimer * 255 / 60);
        int w = 300;
        int h = HELP_LINES.length * 20 + 20;
        int x = 280; // Lệch phải một chút để không đè lên khung thông báo ở góc dưới trái
        int y = GamePanel.SCREEN_HEIGHT - h - 110;
        g2.setColor(new Color(16, 20, 24, Math.min(200, alpha)));
        g2.fillRoundRect(x, y, w, h, 14, 14);
        g2.setFont(HINT_FONT.deriveFont(15f));
        g2.setColor(new Color(255, 255, 255, alpha));
        for (int i = 0; i < HELP_LINES.length; i++) {
            g2.drawString(HELP_LINES[i], x + 20, y + 26 + i * 20);
        }
    }

    private void drawDeathScreen(Graphics2D g2) {
        int alpha = Math.min(170, (deathTimer - DEATH_DELAY) * 6); // Tối dần
        g2.setColor(new Color(20, 0, 0, alpha));
        g2.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        drawCentered(g2, "Chúc bạn may mắn lần sau :3", TITLE_FONT, new Color(230, 80, 70), 260);
        drawCentered(g2, "Nhấn ENTER để chơi lại", HINT_FONT, Color.WHITE, 310);
    }

    private void drawCentered(Graphics2D g2, String text, Font font, Color color, int y) {
        g2.setFont(font);
        int w = g2.getFontMetrics().stringWidth(text);
        g2.setColor(color);
        g2.drawString(text, (GamePanel.SCREEN_WIDTH - w) / 2, y);
    }

    private void drawDebug(Graphics2D g2) {
        Camera cam = world.getCamera();
        Player p = world.getPlayer();

        // Khung va chạm: xanh = Player, đỏ = quái
        g2.setStroke(new BasicStroke(1.5f));
        drawHitbox(g2, p, cam, new Color(80, 255, 160));
        for (Monster m : world.getMonsters()) {
            drawHitbox(g2, m, cam, new Color(255, 90, 90));
        }
        Rectangle attack = p.getAttackBox();
        if (p.isAttacking() && !attack.isEmpty()) {
            g2.setColor(new Color(255, 230, 80));
            g2.drawRect(attack.x - cam.getX(), attack.y - cam.getY(), attack.width, attack.height);
        }

        // Bảng thông số
        int col = (int) (p.getCenterX() / GamePanel.TILE_SIZE);
        int row = (int) (p.getCenterY() / GamePanel.TILE_SIZE);
        String[] lines = {
                "FPS     : " + manager.getGamePanel().getCurrentFps(),
                String.format("Player  : x=%.0f y=%.0f  ô(%d,%d)", p.getX(), p.getY(), col, row),
                "Loại ô  : " + world.getTileManager().getTileType(col, row),
                "HP      : " + p.getCurrentHp() + "/" + p.getMaxHp(),
                "Quái    : " + world.getMonsters().size(),
                "Vật phẩm: " + world.getItems().size(),
        };
        int boxY = GamePanel.SCREEN_HEIGHT - 20 - lines.length * 16;
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(GamePanel.SCREEN_WIDTH - 290, boxY - 16, 280, lines.length * 16 + 12);
        g2.setFont(DEBUG_FONT);
        g2.setColor(new Color(160, 255, 180));
        for (int i = 0; i < lines.length; i++) {
            g2.drawString(lines[i], GamePanel.SCREEN_WIDTH - 280, boxY + i * 16);
        }
    }

    private void drawHitbox(Graphics2D g2, GameObject obj, Camera cam, Color color) {
        Rectangle b = obj.getBounds();
        g2.setColor(color);
        g2.drawRect(b.x - cam.getX(), b.y - cam.getY(), b.width, b.height);
    }

    @Override
    public void onExit() {
    }

    public World getWorld() {
        return world;
    }
}
