package com.paths.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.paths.entity.GameObject;
import com.paths.entity.monster.Boss;
import com.paths.entity.monster.Monster;
import com.paths.entity.object.Chest;
import com.paths.entity.object.Item;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.quest.QuestGiverNpc;
import com.paths.quest.QuestManager;
import com.paths.ui.UIManager;
import com.paths.world.Camera;
import com.paths.world.CollisionChecker;
import com.paths.world.LightingEffect;
import com.paths.world.TileManager;

/**
 * Dev 1. Toàn bộ thế giới của 1 ván chơi: bản đồ, camera, Player, quái, vật phẩm, rương, NPC, nhiệm vụ, UI.
 * Các module gọi nhau qua World (world.getPlayer(), world.addItem()...) thay vì gọi thẳng nhau.
 * Chỉ Dev 1 sửa file này.
 */
public class World {

    private final KeyHandler keys;

    private final TileManager tileManager;          // Dev 3
    private final CollisionChecker collisionChecker; // Dev 3
    private final Camera camera;                    // Dev 3
    private final LightingEffect lighting;          // Dev 3

    private final Player player;                    // Dev 2

    private final List<Monster> monsters = new ArrayList<>(); // Dev 4
    private final List<Item> items = new ArrayList<>();       // Dev 4
    private final List<Chest> chests = new ArrayList<>();     // Dev 4
    private final List<QuestGiverNpc> npcs = new ArrayList<>(); // Dev 5

    private final QuestManager questManager;        // Dev 5
    private final UIManager uiManager;              // Dev 5

    private boolean bossDefeated = false;

    public World(KeyHandler keys) {
        this.keys = keys;
        // Thứ tự quan trọng: bản đồ trước, rồi những thứ cần đến bản đồ
        tileManager = new TileManager();
        collisionChecker = new CollisionChecker(tileManager);
        camera = new Camera(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        lighting = new LightingEffect(tileManager);
        player = new Player(this, keys);
        questManager = new QuestManager(this);
        uiManager = new UIManager(this);
    }

    /**
     * Đặt người chơi, quái, rương, NPC lên bản đồ. Tọa độ tính theo Ô (tile), dùng hàm tile(n).
     * TODO (Dev 1): mỗi giai đoạn thêm dần nội dung (xem file hướng dẫn của Leader).
     */
    public void spawnLevel() {
        player.setPosition(tile(6), tile(6));
    }

    /** Cập nhật cả thế giới mỗi frame. Thứ tự: bản đồ -> UI -> người chơi -> quái -> đồ vật -> camera */
    public void update() {
        tileManager.update();
        uiManager.update();

        // Khi đang mở túi đồ hoặc đang nói chuyện với NPC thì thế giới đứng yên
        if (uiManager.isBlockingGameplay()) {
            return;
        }

        player.update();

        // Duyệt trên BẢN SAO danh sách: trong lúc update, quái có thể chết và rơi đồ
        // (thêm phần tử vào items), duyệt bản sao thì không bị lỗi ConcurrentModificationException
        for (Monster m : new ArrayList<>(monsters)) {
            m.update();
        }
        for (Item item : new ArrayList<>(items)) {
            item.update();
        }
        for (Chest chest : new ArrayList<>(chests)) {
            chest.update();
        }
        for (QuestGiverNpc npc : npcs) {
            npc.update();
        }

        monsters.removeIf(Monster::isRemovable);
        items.removeIf(Item::isPickedUp);

        questManager.update();
        camera.update(player, tileManager.getMapWidthPx(), tileManager.getMapHeightPx());
    }

    /** Vẽ theo lớp (layer): vẽ sau sẽ đè lên vẽ trước. UI luôn vẽ cuối cùng. */
    public void render(Graphics2D g2) {
        tileManager.draw(g2, camera);
        for (Chest chest : chests) {
            chest.draw(g2, camera);
        }
        for (Item item : items) {
            item.draw(g2, camera);
        }
        for (QuestGiverNpc npc : npcs) {
            npc.draw(g2, camera);
        }
        for (Monster m : monsters) {
            m.draw(g2, camera);
        }
        player.draw(g2, camera);
        lighting.draw(g2, player, camera);
        uiManager.draw(g2);
    }

    // ===================== SỰ KIỆN (các Dev khác gọi vào) =====================

    /** Monster (Dev 4) gọi khi 1 con quái vừa chết */
    public void onMonsterKilled(Monster monster) {
        questManager.onMonsterKilled(monster.getKind());
        if (monster instanceof Boss) {
            bossDefeated = true;
        }
    }

    /** Chest (Dev 4) gọi khi người chơi vừa mở 1 rương */
    public void onChestOpened(Chest chest) {
        questManager.onChestOpened();
    }

    /**
     * Tìm vật thể tương tác được (rương, NPC) gần nhất trong bán kính maxDistance (px).
     * Player (Dev 2) dùng khi bấm E, UIManager (Dev 5) dùng để hiện dòng gợi ý "Nhấn [E]...".
     * Trả về null nếu không có gì ở gần.
     */
    public IInteractable findNearestInteractable(GameObject from, double maxDistance) {
        IInteractable best = null;
        double bestDist = maxDistance;
        List<GameObject> candidates = new ArrayList<>();
        candidates.addAll(chests);
        candidates.addAll(npcs);
        for (GameObject obj : candidates) {
            IInteractable target = (IInteractable) obj;
            double d = from.distanceTo(obj);
            if (target.canInteract() && d <= bestDist) {
                best = target;
                bestDist = d;
            }
        }
        return best;
    }

    // ===================== THÊM ĐỐI TƯỢNG =====================

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    /** Thêm vật phẩm lên bản đồ (vd quái chết rơi đồ, rương mở ra đồ) */
    public void addItem(Item item) {
        items.add(item);
    }

    public void addChest(Chest chest) {
        chests.add(chest);
    }

    public void addNpc(QuestGiverNpc npc) {
        npcs.add(npc);
    }

    // ===================== GETTER =====================

    public KeyHandler getKeyHandler() { return keys; }
    public TileManager getTileManager() { return tileManager; }
    public CollisionChecker getCollisionChecker() { return collisionChecker; }
    public Camera getCamera() { return camera; }
    public Player getPlayer() { return player; }
    public List<Monster> getMonsters() { return monsters; }
    public List<Item> getItems() { return items; }
    public List<Chest> getChests() { return chests; }
    public List<QuestGiverNpc> getNpcs() { return npcs; }
    public QuestManager getQuestManager() { return questManager; }
    public UIManager getUIManager() { return uiManager; }

    /** Boss còn sống trên bản đồ (null nếu không có hoặc đã bị xóa) */
    public Boss getBoss() {
        for (Monster m : monsters) {
            if (m instanceof Boss boss) {
                return boss;
            }
        }
        return null;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    /** Đổi số Ô sang pixel. Ví dụ tile(5) = 240 */
    public static int tile(int n) {
        return n * GamePanel.TILE_SIZE;
    }
}
