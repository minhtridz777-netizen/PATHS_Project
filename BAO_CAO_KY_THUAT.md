# PATHS — Tài liệu Kỹ thuật & Kế hoạch Dự án
**2D Top-down Action RPG bằng Java thuần (Swing/AWT) — Nhóm 5 người**

---

## PHẦN 1: Hướng dẫn Setup Project trên IntelliJ IDEA

### 1.1. Cài đặt JDK

- Cài **JDK 17** (LTS, ổn định, có `switch` biểu thức mà skeleton code trong project này dùng) hoặc mới hơn. Có thể tải qua IntelliJ luôn (xem bước 1.2).
- Kiểm tra bằng terminal: `java -version` và `javac -version`.

### 1.2. Khởi tạo Project — chọn loại Project nào?

Vì đề bài yêu cầu **Java thuần, không dùng Game Engine**, và nhóm chỉ cần thư viện chuẩn (Swing/AWT có sẵn trong JDK), **KHÔNG cần Maven/Gradle** — dùng project Java thuần để đơn giản hóa việc setup cho cả 5 thành viên (không ai phải lo `pom.xml`/`build.gradle`).

**Các bước trên IntelliJ IDEA (Leader thực hiện rồi đẩy lên Git cho cả nhóm clone về):**

1. Mở IntelliJ IDEA → **New Project**.
2. Ở khung bên trái chọn **Java** (KHÔNG chọn Maven, KHÔNG chọn Gradle, KHÔNG chọn Kotlin).
3. Đặt tên project: `PATHS`.
4. Mục **JDK**: bấm dropdown → **Download JDK...** nếu chưa có → chọn **Version 17 (LTS)**, vendor Eclipse Temurin hoặc Oracle OpenJDK đều được.
5. **Add sample code**: bỏ tick (không cần IntelliJ tự sinh `Main.java` mẫu, nhóm sẽ dùng cấu trúc riêng bên dưới).
6. Bấm **Create**.
7. IntelliJ sẽ tạo sẵn thư mục `src/`. Đây chính là **Source Root** — mọi package Java sẽ nằm trong đây.
8. Chuột phải vào project → **New → Directory**, tạo thêm 1 thư mục tên **`res`** ở NGANG HÀNG với `src` (không nằm trong `src`). Đây là nơi chứa toàn bộ hình ảnh (sprite), âm thanh, file bản đồ.
9. Chuột phải vào thư mục `res` → **Mark Directory as → Resources Root**. Bước này giúp `AssetManager.getImage("/player/...")` (đường dẫn bắt đầu bằng `/`) tìm thấy file đúng, vì IntelliJ sẽ copy nội dung Resources Root vào cùng cấp với `.class` khi build.
10. Vào **File → Project Structure → Project → Language Level**: chọn tương ứng JDK 17 (hoặc mới hơn) để dùng được cú pháp `switch` biểu thức trong skeleton code.

> Lưu ý quan trọng để tránh conflict: **Cả 5 người phải dùng chung 1 tên package gốc** (`com.paths`) và cấu trúc thư mục y hệt nhau (mục 1.3). Nếu ai đó tự đặt tên package khác, khi merge Git sẽ vỡ toàn bộ import.

### 1.3. Cấu trúc Package (Package Structure) chuẩn

Nguyên tắc chia: **chia theo MODULE/CHỨC NĂNG** (không chia theo "loại file" như tất cả `.java` nằm 1 chỗ), để mỗi Dev chỉ động vào thư mục riêng của mình, giảm tối đa xung đột file khi merge Git.

```
PATHS/                              (Thư mục gốc project)
├── .gitignore
├── res/                            (Resources Root — ảnh, âm thanh, bản đồ)
│   ├── player/                     → Dev 2 quản lý (sprite nhân vật)
│   ├── monster/                    → Dev 4 quản lý (sprite quái, boss)
│   ├── tiles/                      → Dev 3 quản lý (ảnh tile: cỏ, tường, nước...)
│   ├── maps/                       → Dev 3 quản lý (file .txt/.csv bản đồ)
│   └── sounds/                     → dùng chung (nhạc nền, âm thanh hiệu ứng)
│
└── src/
    └── com/paths/
        ├── main/
        │   └── Main.java                       → Dev 1 (entry point)
        │
        ├── core/                                → Dev 1 (Leader) sở hữu, KHÔNG ai khác sửa
        │   ├── GamePanel.java                   (Game Loop 60FPS, nơi ráp mọi module)
        │   ├── AssetManager.java                (Singleton load/cache ảnh)
        │   ├── KeyHandler.java                  → Dev 2 sở hữu (dù nằm trong package core)
        │   └── state/
        │       ├── GameState.java               (interface)
        │       ├── GameStateManager.java
        │       ├── PlayState.java
        │       ├── MenuState.java               (Dev 1 tự thêm khi cần)
        │       └── PauseState.java              (Dev 1 tự thêm khi cần)
        │
        ├── interfaces/                          → Dev 1 thiết kế, cả team dùng chung
        │   ├── IDamageable.java
        │   ├── IInteractable.java
        │   └── IMovable.java                    (tùy chọn, thêm nếu cần)
        │
        ├── entity/                              → Dev 1 sở hữu class nền (GameObject, Entity)
        │   ├── GameObject.java                  (abstract — cha của TẤT CẢ vật thể)
        │   ├── Entity.java                       (abstract — cha của Player, Monster, Boss)
        │   ├── Direction.java                    (enum UP/DOWN/LEFT/RIGHT)
        │   │
        │   ├── player/                           → DEV 2 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │   │   ├── Player.java
        │   │   └── PlayerAnimator.java
        │   │
        │   ├── monster/                          → DEV 4 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │   │   ├── Monster.java
        │   │   ├── Boss.java
        │   │   └── ai/
        │   │       ├── AIState.java              (interface)
        │   │       ├── PatrolBehavior.java
        │   │       └── ChaseBehavior.java
        │   │
        │   └── object/                           → DEV 4 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │       ├── Item.java
        │       └── Chest.java
        │
        ├── world/                                → DEV 3 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │   ├── Tile.java
        │   ├── TileManager.java
        │   ├── Camera.java
        │   └── CollisionChecker.java
        │
        ├── ui/                                   → DEV 5 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │   ├── UIManager.java
        │   ├── HealthBar.java
        │   └── MiniMap.java
        │
        ├── inventory/                            → DEV 5 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
        │   └── Inventory.java
        │
        └── quest/                                → DEV 5 SỞ HỮU TOÀN BỘ THƯ MỤC NÀY
            ├── Quest.java
            └── QuestManager.java
```

**Vì sao chia như vậy giúp tránh conflict?** Vì mỗi Dev có 1 hoặc vài package riêng biệt không ai khác đụng vào (`entity.player` chỉ Dev 2 sửa, `entity.monster` + `entity.object` chỉ Dev 4 sửa, `world` chỉ Dev 3, `ui` + `inventory` + `quest` chỉ Dev 5). Chỉ có `core`, `entity` (2 file gốc), `interfaces` là dùng chung — và những file này do **Dev 1 giữ quyền chỉnh sửa cuối cùng**, các Dev khác chỉ đọc/dùng, hạn chế tối đa việc 2 người cùng sửa 1 file cùng lúc.

Toàn bộ 31 file skeleton ở cấu trúc trên đã được chuẩn bị sẵn kèm theo tài liệu này và **đã compile thành công** bằng `javac`, sẵn sàng để cả nhóm copy thẳng vào IntelliJ và bắt đầu code tiếp.

### 1.4. Quy ước code chung (Coding Convention)

- Tên class: `PascalCase` (vd `PlayerAnimator`). Tên biến/method: `camelCase` (vd `takeDamage`).
- Mỗi class = 1 file, tên file trùng tên class.
- Hằng số dùng chung (kích thước tile, FPS...) đặt trong `GamePanel` dạng `public static final`, KHÔNG hard-code số ma thuật (magic number) rải rác ở nhiều file.
- Bắt buộc viết comment Javadoc `/** ... */` phía trên mỗi class/method public giải thích mục đích — skeleton code đính kèm đã làm mẫu theo đúng convention này.

---

## PHẦN 2: Bản kế hoạch chi tiết (Master Plan & Sprints)

Tổng thời gian giả định: **4 Sprint x 1 tuần = 4 tuần** (điều chỉnh số ngày theo deadline thực tế môn học). Dùng bảng này làm **Timeline báo cáo tiến độ** hàng tuần.

### Sprint 0 — Khởi tạo (trước Sprint 1, làm trong 1–2 ngày đầu)

| Người phụ trách | Việc cần làm | File/Module liên quan |
|---|---|---|
| Dev 1 (Leader) | Tạo project IntelliJ theo Phần 1, tạo repo Git, tạo cấu trúc package, commit "khung xương" ban đầu (31 file skeleton), tạo branch `develop` | Toàn bộ `src/com/paths/**` |
| Cả 5 Dev | Clone repo, tự build thử (`javac`/Run trong IntelliJ) để chắc chắn môi trường ai cũng chạy được, đọc kỹ toàn bộ comment TODO trong file mình phụ trách | — |
| Dev 3 | Chuẩn bị/tìm sprite tile + vẽ thử 1 file `map01.txt` mẫu (bản đồ nhỏ 20x15 ô để test sớm) | `res/tiles/`, `res/maps/map01.txt` |
| Dev 2 | Chuẩn bị sprite nhân vật (đi 4 hướng, tối thiểu 3 frame/hướng) | `res/player/` |
| Dev 4 | Chuẩn bị sprite quái thường + 1 boss + item/chest | `res/monster/` |

**Definition of Done Sprint 0:** Cả nhóm build được project rỗng, thấy cửa sổ game đen 768x576 hiện lên, không lỗi compile.

---

### Sprint 1 — Nền tảng di chuyển & Bản đồ (Core Movement)

**Mục tiêu:** Nhân vật đi lại được trên bản đồ tĩnh, camera bám theo, có va chạm cơ bản.

| Dev | Nhiệm vụ cụ thể | File cần hoàn thành |
|---|---|---|
| **Dev 1** | Hoàn thiện `GameStateManager` + `PlayState`; nối `GamePanel` gọi đúng thứ tự update/draw của Player, TileManager, Camera; hoàn thiện `AssetManager.getImage()` | `core/GamePanel.java`, `core/state/*.java`, `core/AssetManager.java` |
| **Dev 2** | Hoàn thiện `KeyHandler` (đã có sẵn WASD, bổ sung phím tương tác E); implement di chuyển trong `Player.update()`; hoàn thiện `PlayerAnimator` load sprite 4 hướng | `core/KeyHandler.java`, `entity/player/Player.java`, `entity/player/PlayerAnimator.java` |
| **Dev 3** | Implement `TileManager.loadMap()` đọc file `.txt`; implement `TileManager.draw()` có culling theo camera; implement `Camera.update()` bám player có clamp biên | `world/TileManager.java`, `world/Tile.java`, `world/Camera.java` |
| **Dev 4** | Chuẩn bị class `Monster` ở trạng thái TĨNH (đứng yên, có sprite, có hitbox) để Dev 3 test va chạm cùng, CHƯA cần AI | `entity/monster/Monster.java` (bản rút gọn) |
| **Dev 5** | Dựng khung `UIManager` + `HealthBar` hiển thị máu Player cố định (test với giá trị máu giả) | `ui/UIManager.java`, `ui/HealthBar.java` |

**Điểm tích hợp cuối Sprint 1 (Dev 1 chủ trì merge):** Nhân vật đi WASD trên bản đồ thật, không xuyên tường, camera mượt, thanh máu hiện trên góc màn hình.

**Definition of Done:** Chạy được demo "đi dạo" quanh 1 bản đồ nhỏ không lỗi, không giật hình.

---

### Sprint 2 — Combat & Entity nâng cao (Combat & AI)

**Mục tiêu:** Đánh nhau được với quái, quái có AI tuần tra/rượt đuổi, va chạm hoàn chỉnh.

| Dev | Nhiệm vụ cụ thể | File cần hoàn thành |
|---|---|---|
| **Dev 1** | Điều phối tích hợp combat: đảm bảo `PlayState` giữ danh sách `List<Monster>` và gọi update/draw đúng cho từng con; review code chung `Entity.java` nếu 2 bên (Dev2, Dev4) cần chỉnh sửa | `core/state/PlayState.java`, `entity/Entity.java` |
| **Dev 2** | Implement `Player.attack()`: tạo hitbox tấn công theo hướng, quét danh sách Monster gây damage qua `IDamageable`; thêm cooldown, animation tấn công | `entity/player/Player.java`, `entity/player/PlayerAnimator.java` |
| **Dev 3** | Hoàn thiện `CollisionChecker.canMoveToTile()` đầy đủ 4 góc hitbox; cung cấp API va chạm Entity-Entity cho Dev 2/Dev 4 dùng chung | `world/CollisionChecker.java` |
| **Dev 4** | Implement `Monster.attack()`; implement `PatrolBehavior` + `ChaseBehavior` (AI tuần tra/rượt đuổi dựa trên `detectionRange`); implement `Boss.java` với chiêu đặc biệt/phase | `entity/monster/Monster.java`, `entity/monster/Boss.java`, `entity/monster/ai/*.java` |
| **Dev 5** | `HealthBar` hiển thị máu thật của Player (không còn giá trị giả); thêm thanh máu nhỏ phía trên đầu Monster/Boss | `ui/HealthBar.java` |

**Definition of Done:** Player và quái đánh nhau, máu trừ đúng, quái chết biến mất, quái biết đuổi theo khi Player lại gần, Boss có ít nhất 1 chiêu khác quái thường.

---

### Sprint 3 — Item, Kho báu, Inventory, Quest (Content & Systems)

**Mục tiêu:** Hoàn thiện vòng lặp gameplay "đánh quái → rớt đồ → nhặt đồ → nhận/hoàn thành nhiệm vụ".

| Dev | Nhiệm vụ cụ thể | File cần hoàn thành |
|---|---|---|
| **Dev 1** | Quản lý danh sách `Item`/`Chest` active trong `PlayState`, xử lý spawn/despawn item sau khi nhặt hoặc mở rương | `core/state/PlayState.java` |
| **Dev 2** | Thêm phím tương tác E gọi `onInteract()` lên `IInteractable` gần nhất (Item/Chest); hiển thị icon nhắc "Nhấn E" khi đứng gần | `core/KeyHandler.java`, phối hợp Dev 5 |
| **Dev 3** | Vẽ thêm 1-2 bản đồ mới (map02, map03) hoặc khu vực mở rộng; tối ưu hiệu năng vẽ tile nếu map lớn | `res/maps/`, `world/TileManager.java` |
| **Dev 4** | Implement hệ thống rớt đồ (loot table) trong `Monster` khi chết; hoàn thiện `Chest.onInteract()` sinh vật phẩm; hoàn thiện `Item.onInteract()` thêm vào Inventory | `entity/monster/Monster.java`, `entity/object/Item.java`, `entity/object/Chest.java` |
| **Dev 5** | Hoàn thiện `Inventory` (thêm/xóa/dùng item) + vẽ UI lưới túi đồ; hoàn thiện `Quest`/`QuestManager` (nhận quest, cập nhật tiến độ, hiển thị quest log) | `inventory/Inventory.java`, `quest/Quest.java`, `quest/QuestManager.java`, `ui/UIManager.java` |

**Definition of Done:** Giết quái rớt đồ → nhặt được → hiện trong túi đồ; mở rương ra vật phẩm; có ít nhất 1 quest hoàn chỉnh từ nhận đến hoàn thành.

---

### Sprint 4 — Hoàn thiện, Mini-map, Polish & Testing

**Mục tiêu:** Game chơi được trọn vẹn từ đầu đến cuối, sửa lỗi, làm đẹp UI, chuẩn bị báo cáo/demo.

| Dev | Nhiệm vụ cụ thể | File/Công việc |
|---|---|---|
| **Dev 1** | Thêm `MenuState` (màn hình chính), `PauseState` (tạm dừng); kiểm tra toàn bộ luồng State; fix bug tích hợp cuối cùng; chuẩn bị file build/export (JAR chạy được) | `core/state/MenuState.java`, `core/state/PauseState.java` |
| **Dev 2** | Polish animation Player (thêm animation tấn công mượt, hiệu ứng khi bị thương); cân bằng lại tốc độ/damage | `entity/player/*.java` |
| **Dev 3** | Hoàn thiện `MiniMap` phối hợp với Dev 5; polish tilemap (thêm vật cản trang trí); test kỹ va chạm ở mọi bản đồ | `world/*.java` |
| **Dev 4** | Cân bằng độ khó quái/boss (máu, damage, tốc độ); test kỹ AI không bị kẹt góc tường | `entity/monster/**` |
| **Dev 5** | Hoàn thiện `MiniMap.draw()`; polish toàn bộ UI (font, màu sắc, bố cục); viết màn hình Game Over/Chiến thắng | `ui/*.java` |
| **Cả nhóm** | Chơi test chéo (mỗi người test module người khác), tổng hợp bug vào 1 danh sách chung, fix trước deadline | — |

**Definition of Done:** Build ra file chạy được (Run configuration hoặc export JAR), chơi được trọn vẹn 1 lượt: mở game → đi khám phá → đánh quái → nhặt đồ → hoàn thành quest → gặp/đánh boss → màn hình kết thúc.

---

## PHẦN 3: Kiến trúc OOP & Skeleton Code

### 3.1. Sơ đồ quan hệ Class cốt lõi

```
                         ┌───────────────────┐
                         │   GameObject      │  (abstract)
                         │ x, y, w, h, sprite│
                         │ +update() [abs]   │
                         │ +draw()   [abs]   │
                         │ +getBounds()      │
                         └─────────┬─────────┘
                                   │ extends
              ┌────────────────────┼─────────────────────┐
              │                    │                      │
    ┌─────────▼─────────┐  ┌───────▼────────┐   ┌─────────▼────────┐
    │      Entity        │  │  entity.object.Item │ entity.object.Chest│
    │ implements          │  │ implements IInteractable │
    │   IDamageable        │  └────────────────┘   └──────────────────┘
    │ hp, speed, direction │
    │ +attack() [abstract] │
    │ +takeDamage()        │
    └─────────┬────────────┘
              │ extends
     ┌────────┴─────────┐
┌────▼─────┐      ┌──────▼──────┐
│  Player   │      │   Monster    │
│ (Dev 2)   │      │  (Dev 4)     │
└───────────┘      └──────┬───────┘
                           │ extends
                    ┌──────▼──────┐
                    │    Boss      │
                    │  (Dev 4)     │
                    └──────────────┘
```

**Interfaces dùng xuyên suốt:**
- `IDamageable` — bất kỳ ai "có máu, có thể bị đánh" (Player, Monster, Boss).
- `IInteractable` — bất kỳ vật thể nào "bấm E để tương tác được" (Item, Chest, sau này có thể thêm NPC).

### 3.2. Tính Trừu tượng (Abstraction) và Đa hình (Polymorphism) thể hiện ở đâu?

- **Trừu tượng:** `GameObject` và `Entity` là `abstract class` — không object nào được khởi tạo trực tiếp từ 2 class này (`new GameObject()` là bất hợp lệ). Chúng chỉ định nghĩa "khung sườn" chung (có vị trí, có thể update/draw) và ép buộc mọi lớp con phải tự hiện thực chi tiết. Tương tự, `IDamageable`/`IInteractable` cho phép các module (combat, UI, tương tác) thao tác với "khái niệm" (có thể bị đánh / có thể tương tác) mà không cần biết class cụ thể đằng sau nó là gì.
- **Đa hình:** Thể hiện rõ nhất ở `Entity.attack()` (abstract) — `Player.attack()` vung vũ khí, `Monster.attack()` cắn/lao vào, `Boss.attack()` (kế thừa từ `Monster` nhưng override lại) tung chiêu đặc biệt. Khi `PlayState` duyệt qua `List<Monster>` và gọi `m.attack()`, Java tự động chạy đúng phiên bản `attack()` của object thực sự tại runtime (dynamic dispatch) — dù code gọi ra chỉ viết đúng 1 dòng.
- Toàn bộ giải thích chi tiết hơn (kèm ví dụ cụ thể từng dòng) đã được viết ngay trong Javadoc của từng file `.java` — xem trực tiếp trong source code đính kèm.

### 3.3. Danh sách skeleton code đã cung cấp (đính kèm cùng tài liệu này)

Toàn bộ 31 file dưới đây đã được viết sẵn, **đã compile thành công**, và có comment tiếng Việt chi tiết theo đúng format: *class/method này làm gì → ai phụ trách viết tiếp (TODO) → logic cần triển khai là gì*.

| Nhóm | File |
|---|---|
| Entry point & Game Loop (Dev 1) | `main/Main.java`, `core/GamePanel.java` |
| State Management (Dev 1) | `core/state/GameState.java`, `core/state/GameStateManager.java`, `core/state/PlayState.java` |
| Asset & Input (Dev 1 / Dev 2) | `core/AssetManager.java`, `core/KeyHandler.java` |
| Interfaces nền tảng (Dev 1) | `interfaces/IDamageable.java`, `interfaces/IInteractable.java` |
| Class nền OOP (Dev 1) | `entity/GameObject.java`, `entity/Entity.java`, `entity/Direction.java` |
| Player (Dev 2) | `entity/player/Player.java`, `entity/player/PlayerAnimator.java` |
| Tilemap/Camera/Collision (Dev 3) | `world/Tile.java`, `world/TileManager.java`, `world/Camera.java`, `world/CollisionChecker.java` |
| Monster/Boss/AI/Item/Chest (Dev 4) | `entity/monster/Monster.java`, `entity/monster/Boss.java`, `entity/monster/ai/AIState.java`, `entity/monster/ai/PatrolBehavior.java`, `entity/monster/ai/ChaseBehavior.java`, `entity/object/Item.java`, `entity/object/Chest.java` |
| UI/Inventory/Quest (Dev 5) | `ui/UIManager.java`, `ui/HealthBar.java`, `ui/MiniMap.java`, `inventory/Inventory.java`, `quest/Quest.java`, `quest/QuestManager.java` |

> Mở trực tiếp từng file trong thư mục `src/com/paths/` đính kèm để xem đầy đủ code + comment. Mỗi Dev nên đọc kỹ TOÀN BỘ comment `TODO` trong các file thuộc phần mình phụ trách trước khi bắt tay code Sprint 1.

---

## PHẦN 4: Hướng dẫn ráp code (Git Flow)

### 4.1. Cấu trúc branch

```
main        → Chỉ chứa code ĐÃ TEST ỔN ĐỊNH, dùng để nộp bài / demo. Không ai push thẳng vào đây.
  └── develop   → Nhánh tích hợp chung, nơi các Dev merge code vào sau khi review.
        ├── feature/dev2-player-movement
        ├── feature/dev3-tilemap-camera
        ├── feature/dev4-monster-ai
        ├── feature/dev5-inventory-ui
        └── feature/dev1-gamestate
```

### 4.2. Luật đặt tên branch

`feature/<devN>-<mo-ta-ngan>` — ví dụ `feature/dev2-combat-cooldown`, `feature/dev4-boss-phase2`. Không code trực tiếp trên `develop` hay `main`.

### 4.3. Quy trình làm việc (mỗi Dev lặp lại vòng này mỗi task)

1. `git checkout develop && git pull` — luôn cập nhật `develop` mới nhất trước khi bắt đầu việc mới.
2. `git checkout -b feature/devN-ten-task` — tạo nhánh riêng.
3. Code + commit thường xuyên, commit message rõ ràng: `[Dev4] Implement Monster loot drop system`.
4. Trước khi tạo Pull Request: `git checkout develop && git pull`, sau đó quay lại nhánh của mình `git merge develop` để giải quyết conflict TRƯỚC, tại máy cá nhân (dễ xử lý hơn conflict trên GitHub).
5. Push nhánh (`git push origin feature/devN-ten-task`) → mở **Pull Request vào `develop`** (không PR thẳng vào `main`).
6. **Bắt buộc có ít nhất 1 người khác review** (ưu tiên Dev 1 hoặc người có module liên quan) trước khi bấm Merge.
7. Sau khi merge xong, xóa nhánh feature đó để repo gọn (`git branch -d ...`).
8. Cuối mỗi Sprint, Dev 1 tạo PR từ `develop` vào `main` sau khi cả team đã test chạy ổn.

### 4.4. Luật tránh conflict "vỡ project"

- **Mỗi Dev chỉ sửa file trong package mình sở hữu** (theo bảng phân quyền ở Phần 1.3). Nếu cần sửa file dùng chung (`GameObject`, `Entity`, `GamePanel`, các interface...) — báo trước trong nhóm chat, để 1 người sửa tại 1 thời điểm, tránh 2 người cùng sửa song song gây conflict khó merge.
- Không bao giờ dùng `git push --force` lên `develop`/`main`.
- Không commit thư mục build (`out/`, `.idea/`, `*.class`) — đã cấu hình sẵn trong `.gitignore` đính kèm.
- Commit nhỏ và thường xuyên (mỗi commit nên là 1 thay đổi có ý nghĩa) thay vì gộp cả tuần code vào 1 commit khổng lồ — giúp review dễ và revert khi cần cũng dễ.
- Khi gặp conflict ở file dùng chung, người gây conflict SAU (người merge `develop` về nhánh mình sau) là người có trách nhiệm resolve, không đẩy việc đó sang review.

---

*Tài liệu này đi kèm bộ source code skeleton hoàn chỉnh trong thư mục `PATHS_Project/src/`, đã được biên dịch kiểm tra thành công bằng `javac` (JDK 25). Import thẳng thư mục `PATHS_Project` vào IntelliJ IDEA (Open → chọn thư mục) là dùng được ngay.*
