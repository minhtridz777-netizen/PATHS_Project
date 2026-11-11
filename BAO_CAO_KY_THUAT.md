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
│   ├── player/                     → Dev 2 (sprite nhân vật)
│   ├── tiles/, maps/               → Dev 3 (ảnh ô đất, file bản đồ)
│   ├── monster/, items/            → Dev 4 (quái, Boss, vật phẩm, rương)
│   ├── npc/                        → Dev 5 (hình NPC)
│   └── sounds/                     → Dev 1 (nhạc nền, hiệu ứng, định dạng WAV)
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

## PHẦN 2: Kế hoạch thực hiện theo giai đoạn

Dự án chia thành **Giai đoạn 0** (Leader dựng bộ khung) và **4 giai đoạn làm việc**. Trong mỗi giai đoạn, 5 thành viên làm **độc lập** trên nhánh Git riêng; cuối giai đoạn Leader ghép code qua Pull Request, chạy thử theo kịch bản kiểm tra, rồi giao giai đoạn tiếp theo.

Điều kiện để làm độc lập: bộ khung Giai đoạn 0 đã chốt sẵn **tên và chữ ký mọi hàm dùng chung** cho cả 4 giai đoạn (thân hàm chưa viết thì trả giá trị an toàn), mỗi người chỉ sửa file trong package của mình, và mỗi người có một file `XxxTestMain` để tự chạy phần của mình bằng `core/TestWindow`.

| | Giai đoạn 1: Thế giới | Giai đoạn 2: Chiến đấu | Giai đoạn 3: Kho báu & Nhiệm vụ | Giai đoạn 4: Hoàn thiện |
|---|---|---|---|---|
| **Dev 1 (Leader)** | Đặt quái lên bản đồ, chế độ debug F3 | Phát hiện nhân vật chết, chơi lại | Đặt rương, giao nhiệm vụ, bảng phím, sưu tầm âm thanh | Menu, Tạm dừng, Thua, Thắng; đặt NPC; nhạc nền; đóng gói JAR |
| **Dev 2** | Di chuyển 8 hướng, hoạt ảnh đi bộ | Chém, vùng chém, bị đánh, bất tử | Phím E tương tác, tự nhặt đồ | Kỹ năng lướt, sprite nhân vật, âm thanh |
| **Dev 3** | Đọc và vẽ bản đồ, camera bám theo | Va chạm tường, rung màn hình | Nước gợn sóng, hỗ trợ ảnh ô đất | Bóng tối trong hầm ngục, ảnh ô đất |
| **Dev 4** | Vẽ Slime/Skeleton/Boss, AI tuần tra | Rượt đuổi, cắn, hiệu ứng chết | Vật phẩm, rương, bảng rơi đồ | Boss 2 pha, ảnh quái và vật phẩm, âm thanh |
| **Dev 5** | Thanh máu, bảng trạng thái, thông báo | Bản đồ nhỏ, thanh máu Boss | Hệ thống nhiệm vụ, túi đồ | NPC Già làng, hội thoại, banner, ảnh NPC, âm thanh |

**Tài nguyên:** game chạy được hoàn toàn bằng hình vẽ trong code. Mỗi thành viên tự sưu tầm ảnh cho phần của mình (Leader sưu tầm âm thanh WAV) và đặt vào thư mục con tương ứng trong `res/`; có file đúng tên thì game dùng, không có thì vẫn vẽ bằng code. Nguồn của từng tài nguyên được ghi trong file `NGUON.txt` cùng thư mục.

Mỗi giai đoạn trong hướng dẫn được chia thành các **bài nhỏ** (đọc, thêm một đoạn code, chạy thử, tick), kèm bài tập tự làm và câu hỏi bảo vệ.

Hướng dẫn chi tiết từng thành viên (lý thuyết, các bước, code mẫu, cách chạy thử, checklist, câu hỏi bảo vệ) nằm trong thư mục `docs/` của repo. Mở `docs/index.html` bằng trình duyệt để bắt đầu.

---

## PHẦN 3: Kiến trúc OOP & Skeleton Code

### 3.1. Sơ đồ quan hệ Class cốt lõi

```
                    GameObject (abstract)
        x, y, width, height · update() · draw() · getBounds()
              /                 |                 \
       Entity (abstract)       Item             Chest        QuestGiverNpc
  máu, tốc độ, takeDamage()  (IInteractable)  (IInteractable)  (IInteractable)
     attack() abstract
        /            \
    Player         Monster (abstract)
    (Dev 2)        drawBody(), getKind(), getLootTable() abstract
                    /        |         \
                 Slime   Skeleton     Boss          (Dev 4)
```

Các module không gọi thẳng nhau mà đi qua `core/World` (bản đồ, Player, danh sách quái/vật phẩm/rương/NPC, nhiệm vụ, giao diện của 1 ván chơi). Màn hình được quản lý bằng State Pattern (`GameState`: Menu, Chơi, Tạm dừng, Thua, Thắng); AI quái dùng `AIState` (Tuần tra, Rượt đuổi).

**Interfaces dùng xuyên suốt:**
- `IDamageable` — bất kỳ ai "có máu, có thể bị đánh" (Player, Monster, Boss).
- `IInteractable` — bất kỳ vật thể nào "bấm E để tương tác được" (Item, Chest, sau này có thể thêm NPC).

### 3.2. Tính Trừu tượng (Abstraction) và Đa hình (Polymorphism) thể hiện ở đâu?

- **Trừu tượng:** `GameObject` và `Entity` là `abstract class` — không object nào được khởi tạo trực tiếp từ 2 class này (`new GameObject()` là bất hợp lệ). Chúng chỉ định nghĩa "khung sườn" chung (có vị trí, có thể update/draw) và ép buộc mọi lớp con phải tự hiện thực chi tiết. Tương tự, `IDamageable`/`IInteractable` cho phép các module (combat, UI, tương tác) thao tác với "khái niệm" (có thể bị đánh / có thể tương tác) mà không cần biết class cụ thể đằng sau nó là gì.
- **Đa hình:** Thể hiện rõ nhất ở `Entity.attack()` (abstract) — `Player.attack()` vung vũ khí, `Monster.attack()` cắn/lao vào, `Boss.attack()` (kế thừa từ `Monster` nhưng override lại) tung chiêu đặc biệt. Khi `PlayState` duyệt qua `List<Monster>` và gọi `m.attack()`, Java tự động chạy đúng phiên bản `attack()` của object thực sự tại runtime (dynamic dispatch) — dù code gọi ra chỉ viết đúng 1 dòng.
- Toàn bộ giải thích chi tiết hơn (kèm ví dụ cụ thể từng dòng) đã được viết ngay trong Javadoc của từng file `.java` — xem trực tiếp trong source code đính kèm.

### 3.3. Danh sách file của bộ khung (Giai đoạn 0)

Toàn bộ file dưới đây có sẵn trong `src/com/paths/`, đã compile thành công, comment tiếng Việt ghi rõ class làm gì, ai phụ trách, giai đoạn nào viết tiếp.

| Nhóm | File |
|---|---|
| Lõi game (Dev 1) | `main/Main.java`, `core/GamePanel.java`, `core/World.java`, `core/AssetManager.java`, `core/Sprites.java`, `core/Sound.java`, `core/TestWindow.java` |
| Màn hình (Dev 1) | `core/state/GameState.java`, `core/state/GameStateManager.java`, `core/state/PlayState.java` (Giai đoạn 4 thêm Menu, Pause, GameOver, Victory) |
| Lớp nền OOP (Dev 1) | `entity/GameObject.java`, `entity/Entity.java`, `entity/Direction.java`, `interfaces/IDamageable.java`, `interfaces/IInteractable.java` |
| Nhân vật (Dev 2) | `core/KeyHandler.java`, `entity/player/Player.java`, `entity/player/PlayerAnimator.java` |
| Thế giới (Dev 3) | `world/Tile.java`, `world/TileManager.java`, `world/Camera.java`, `world/CollisionChecker.java`, `world/LightingEffect.java` |
| Quái & vật phẩm (Dev 4) | `entity/monster/Monster.java`, `Slime.java`, `Skeleton.java`, `Boss.java`, `entity/monster/ai/AIState.java`, `PatrolBehavior.java`, `ChaseBehavior.java`, `entity/object/Item.java`, `ItemType.java`, `Chest.java` |
| Giao diện & nhiệm vụ (Dev 5) | `ui/UIManager.java`, `ui/HealthBar.java`, `ui/MiniMap.java`, `inventory/Inventory.java`, `quest/Quest.java`, `quest/QuestManager.java`, `quest/QuestGiverNpc.java` |

> Mỗi thành viên đọc file hướng dẫn của mình trong `docs/` trước khi bắt tay code Giai đoạn 1.

---

## PHẦN 4: Hướng dẫn ráp code (Git Flow)

### 4.1. Cấu trúc branch

```
master      → Chỉ chứa code ĐÃ TEST ỔN ĐỊNH, dùng để nộp bài / demo. Không ai push thẳng vào đây.
  └── develop   → Nhánh tích hợp chung, nơi các Dev merge code vào sau khi review.
        ├── feature/dev2-gd1
        ├── feature/dev3-gd1
        ├── feature/dev4-gd1
        ├── feature/dev5-gd1
        └── feature/dev1-gd1
```

### 4.2. Luật đặt tên branch

`feature/devN-gdX` (X là số giai đoạn) — ví dụ `feature/dev2-gd1`, `feature/dev4-gd3`. Không code trực tiếp trên `develop` hay `master`.

### 4.3. Quy trình làm việc (mỗi Dev lặp lại vòng này mỗi task)

1. `git checkout develop && git pull` — luôn cập nhật `develop` mới nhất trước khi bắt đầu việc mới.
2. `git checkout -b feature/devN-ten-task` — tạo nhánh riêng.
3. Code + commit thường xuyên, commit message rõ ràng: `[Dev4] Implement Monster loot drop system`.
4. Trước khi tạo Pull Request: `git checkout develop && git pull`, sau đó quay lại nhánh của mình `git merge develop` để giải quyết conflict TRƯỚC, tại máy cá nhân (dễ xử lý hơn conflict trên GitHub).
5. Push nhánh (`git push origin feature/devN-ten-task`) → mở **Pull Request vào `develop`** (không PR thẳng vào `master`).
6. **Bắt buộc có ít nhất 1 người khác review** (ưu tiên Dev 1 hoặc người có module liên quan) trước khi bấm Merge.
7. Sau khi merge xong, xóa nhánh feature đó để repo gọn (`git branch -d ...`).
8. Cuối mỗi giai đoạn, Dev 1 ghép các PR vào `develop`, chạy thử; xong Giai đoạn 4 thì đưa `develop` vào `master`.

### 4.4. Luật tránh conflict "vỡ project"

- **Mỗi Dev chỉ sửa file trong package mình sở hữu** (theo bảng phân quyền ở Phần 1.3). Nếu cần sửa file dùng chung (`GameObject`, `Entity`, `GamePanel`, các interface...) — báo trước trong nhóm chat, để 1 người sửa tại 1 thời điểm, tránh 2 người cùng sửa song song gây conflict khó merge.
- Không bao giờ dùng `git push --force` lên `develop`/`master`.
- Không commit thư mục build (`out/`, `.idea/`, `*.class`) — đã cấu hình sẵn trong `.gitignore` đính kèm.
- Commit nhỏ và thường xuyên (mỗi commit nên là 1 thay đổi có ý nghĩa) thay vì gộp cả giai đoạn vào 1 commit khổng lồ — giúp review dễ và revert khi cần cũng dễ.
- Khi gặp conflict ở file dùng chung, người gây conflict SAU (người merge `develop` về nhánh mình sau) là người có trách nhiệm resolve, không đẩy việc đó sang review.

---

*Tài liệu này đi kèm bộ source code skeleton hoàn chỉnh trong thư mục `PATHS_Project/src/`, đã được biên dịch kiểm tra thành công bằng `javac` (JDK 25). Import thẳng thư mục `PATHS_Project` vào IntelliJ IDEA (Open → chọn thư mục) là dùng được ngay.*
