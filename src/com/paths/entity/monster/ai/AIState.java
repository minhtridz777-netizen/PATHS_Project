package com.paths.entity.monster.ai;

import com.paths.entity.monster.Monster;

/** Dev 4. Một trạng thái AI của quái (State Pattern). Monster gọi aiState.execute(this) mỗi frame. */
public interface AIState {
    void execute(Monster monster);
}
