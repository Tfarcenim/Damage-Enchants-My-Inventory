package tfar.damageenchantsmyinventory;

import net.minecraft.world.entity.player.Player;

public interface PlayerDuck {

    boolean isRunner();
    void setRunner(boolean runner);

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }
}
