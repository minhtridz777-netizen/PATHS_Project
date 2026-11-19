package com.paths.entity.monster.ai;

import com.paths.entity.monster.Monster;
import com.paths.entity.player.Player;

/** Dev 4. Rượt đuổi: đi thẳng về phía Player, bỏ cuộc khi Player chết hoặc ra khỏi loseRange. */
public class ChaseBehavior implements AIState {

    @Override
    public void execute(Monster monster) {
        Player player = monster.getWorld().getPlayer();
        if (player.isDead() || monster.distanceToPlayer() > monster.getLoseRange()) {
            monster.setAIState(new PatrolBehavior());
            return;
        }
        monster.moveToward(player.getCenterX(), player.getCenterY(), 1.0);
    }
}
