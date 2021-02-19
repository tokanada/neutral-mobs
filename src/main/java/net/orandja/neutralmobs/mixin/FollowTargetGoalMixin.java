package net.orandja.neutralmobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.orandja.neutralmobs.PacifistMobs;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FollowTargetGoal.class)
public abstract class FollowTargetGoalMixin extends TrackTargetGoal {

    @Shadow
    protected Class targetClass;

    public FollowTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Override
    protected boolean canTrack(@Nullable LivingEntity target, TargetPredicate targetPredicate) {
        return !this.isPassive() && super.canTrack(target, targetPredicate);
    }

    @Inject(method = "findClosestTarget()V", at = @At("HEAD"), cancellable = true)
    protected void findClosestTarget(CallbackInfo info) {
        if (this.isPassive()) {
            info.cancel();
        }

    }

    public boolean isPassive() {
        return !(this.mob instanceof EnderDragonEntity || this.mob instanceof WitherEntity) &&
                (this.targetClass == PlayerEntity.class || this.targetClass == ServerPlayerEntity.class) &&
                (this.mob.world.getGameRules().get(PacifistMobs.PASSIVE_MOBS).get() && this.targetClass == PlayerEntity.class);
    }
}
