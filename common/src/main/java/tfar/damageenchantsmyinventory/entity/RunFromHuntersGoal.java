package tfar.damageenchantsmyinventory.entity;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.damageenchantsmyinventory.ducks.PlayerDuck;

import java.util.EnumSet;

public class RunFromHuntersGoal extends Goal {

    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public RunFromHuntersGoal(PathfinderMob pMob, double pSpeedModifier) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        Player hunter = getNearbyHunter();
        if (hunter == null) {
            return false;
        } else {
            this.findFleePosition();
            return true;
        }
    }

    protected boolean isHunterNearby() {
        return getNearbyHunter() != null;
    }

    @Nullable Player getNearbyHunter() {
        return mob.level().getNearestPlayer(mob.getX(),mob.getY(),mob.getZ(),16,entity -> entity instanceof Player player && !PlayerDuck.of(player).isRunner());
    }

    protected void findFleePosition() {

        Player player = getNearbyHunter();
        if (player == null) return;
        Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 64, 10, player.position());
        if (vec3 == null) {
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {

        if (isHunterNearby()) {
            if (mob.getNavigation().isDone()) {
                findFleePosition();
                this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
            }
            return true;
        }

        return !this.mob.getNavigation().isDone();
    }

}
