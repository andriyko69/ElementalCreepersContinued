package io.github.andriyko69.elementalcreeperscontinued.entity;

import io.github.andriyko69.elementalcreeperscontinued.Config;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class ElementalCreeper extends Creeper {

    protected int oldSwell;
    protected int swell;
    protected int maxSwell = 30;
    protected int elementalExplosionRadius = 3;

    public ElementalCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, @NotNull DamageSource source) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, source);
        this.swell += (int) (p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    public float getSwelling(float lerp) {
        return Mth.lerp(lerp, (float) this.oldSwell, (float) this.swell) / (float) (this.maxSwell - 2);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putShort("Fuse", (short) this.maxSwell);
        compoundTag.putByte("ExplosionRadius", (byte) this.elementalExplosionRadius);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("Fuse", 99)) {
            this.maxSwell = compoundTag.getShort("Fuse");
        }
        if (compoundTag.contains("ExplosionRadius", 99)) {
            this.elementalExplosionRadius = compoundTag.getByte("ExplosionRadius");
        }
    }

    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            Level level = this.level();
            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                if (!level.isClientSide()) {
                    this.dead = true;
                    creeperEffect();
                    this.discard();
                }

            }
        }

        super.tick();
    }

    protected void creeperEffect() {
        float f = this.isPowered() ? 2.0F : 1.0F;
        this.level().explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                this.elementalExplosionRadius * f,
                Level.ExplosionInteraction.MOB
        );
        this.spawnLingeringCloud();
    }

    protected void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

            for (MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            this.level().addFreshEntity(areaeffectcloud);
        }
    }

    protected void handleNetworkedExplosionEffects(double radius, SoundEvent soundEvent) {
        handleNetworkedExplosionEffects(radius, null, soundEvent);
    }

    protected void handleNetworkedExplosionEffects(double ignoredRadius, @Nullable Map<Player, Vec3> ignoredHitPlayers,
                                                   SoundEvent soundEvent) {
        Level level = this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(
                    null,
                    x, y, z,
                    soundEvent,
                    SoundSource.HOSTILE,
                    4.0F,
                    (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F
            );

            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    x, y, z,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );
        }

        this.spawnLingeringCloud();
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);

        Level level = this.level();
        if (level instanceof ServerLevel && level.random.nextDouble() < Config.ghostCreeperSpawnChance
                && !(this instanceof GhostCreeper)) {
            GhostCreeper ghost = new GhostCreeper(ModEntities.GHOST_CREEPER.get(), level);
            ghost.moveTo(this.blockPosition(), this.getYRot(), this.getXRot());
            level.addFreshEntity(ghost);
        }
    }
}
