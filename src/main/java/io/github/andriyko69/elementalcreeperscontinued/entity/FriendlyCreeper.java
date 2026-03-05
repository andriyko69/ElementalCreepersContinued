package io.github.andriyko69.elementalcreeperscontinued.entity;

import io.github.andriyko69.elementalcreeperscontinued.ai.FriendlyCreeperSwellGoal;
import io.github.andriyko69.elementalcreeperscontinued.misc.EntityOnlyExplosion;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FriendlyCreeper extends TamableAnimal implements NeutralMob, PowerableMob {

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData
            .defineId(FriendlyCreeper.class, EntityDataSerializers.INT);
    private static final float START_HEALTH = 8.0F;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(FriendlyCreeper.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(FriendlyCreeper.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(FriendlyCreeper.class,
            EntityDataSerializers.BOOLEAN);
    private int oldSwell;
    private int swell;
    private int maxSwell = 30;
    private int cooldown;
    private int explosionRadius = 3;

    @Nullable
    private UUID persistentAngerTarget;

    public FriendlyCreeper(EntityType<? extends FriendlyCreeper> entityType, Level level) {
        super(entityType, level);
        this.setTame(false, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FriendlyCreeperSwellGoal(this));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4,
                new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MAX_HEALTH, START_HEALTH);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
        builder.define(DATA_SWELL_DIR, -1);
        builder.define(DATA_IS_POWERED, false);
        builder.define(DATA_IS_IGNITED, false);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);
        if (this.entityData.get(DATA_IS_POWERED)) {
            compoundTag.putBoolean("powered", true);
        }

        compoundTag.putShort("Fuse", (short) this.maxSwell);
        compoundTag.putByte("ExplosionRadius", (byte) this.explosionRadius);
        compoundTag.putBoolean("ignited", this.isIgnited());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
        this.entityData.set(DATA_IS_POWERED, compoundTag.getBoolean("powered"));
        if (compoundTag.contains("Fuse", 99)) {
            this.maxSwell = compoundTag.getShort("Fuse");
        }

        if (compoundTag.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compoundTag.getByte("ExplosionRadius");
        }

        if (compoundTag.getBoolean("ignited")) {
            this.ignite();
        }
    }

    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.cooldown > 0) {
                this.cooldown -= 1;

            } else {
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

                if (this.swell >= this.maxSwell) {
                    this.swell = 0;
                    this.cooldown = 80;
                    this.setSwellDir(-1);
                    this.explodeCreeper();
                }
            }
        }

        super.tick();
    }

    public int getMaxFallDistance() {
        return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
    }

    public boolean causeFallDamage(float p_149687_, float p_149688_, @NotNull DamageSource damageSource) {
        boolean flag = super.causeFallDamage(p_149687_, p_149688_, damageSource);
        this.swell += (int) (p_149687_ * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }

        return flag;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.CREEPER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    public boolean isPowered() {
        return this.entityData.get(DATA_IS_POWERED);
    }

    public float getSwelling(float p_32321_) {
        return Mth.lerp(p_32321_, (float) this.oldSwell, (float) this.swell) / (float) (this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.entityData.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int p_32284_) {
        this.entityData.set(DATA_SWELL_DIR, p_32284_);
    }

    public void thunderHit(@NotNull ServerLevel serverLevel, @NotNull LightningBolt lightning) {
        super.thunderHit(serverLevel, lightning);
        this.entityData.set(DATA_IS_POWERED, true);
    }

    private void explodeCreeper() {
        Level level = this.level();
        if (!level.isClientSide()) {
            double radius = this.explosionRadius;
            if (this.isPowered()) {
                radius *= 1.5d;
            }

            Map<Player, Vec3> hitPlayers = EntityOnlyExplosion.explodeAt(level, this, this.getX(), this.getY(),
                    this.getZ(), radius, 0.8d, 1.d);
            handleNetworkedExplosionEffects(radius, hitPlayers, SoundEvents.GENERIC_EXPLODE.value());
        }
    }

    protected void handleNetworkedExplosionEffects(double ignoredRadius, @Nullable Map<Player, Vec3> ignoredHitPlayers,
                                                   SoundEvent genericExplode) {
        Level level = this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(
                    null,
                    x, y, z,
                    genericExplode,
                    SoundSource.HOSTILE,
                    4.0F,
                    (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F
            );

            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    x, y, z,
                    1, 0.0, 0.0, 0.0, 0.0
            );
        }

        this.spawnLingeringCloud();
    }

    private void spawnLingeringCloud() {
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

    public boolean isIgnited() {
        return this.entityData.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.entityData.set(DATA_IS_IGNITED, true);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public void aiStep() {
        super.aiStep();
        Level level = this.level();
        if (!level.isClientSide()) {
            this.updatePersistentAnger((ServerLevel) level, true);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }

        this.setOrderedToSit(false);
        return super.hurt(source, amount);
    }

    public @NotNull InteractionResult mobInteract(Player p_30412_, @NotNull InteractionHand hand) {
        ItemStack itemstack = p_30412_.getItemInHand(hand);
        Level level = this.level();
        if (level.isClientSide()) {
            boolean flag = this.isOwnedBy(p_30412_) || this.isTame()
                    || itemstack.is(Items.GUNPOWDER) && !this.isTame() && !this.isAngry();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                this.heal(10);
                if (!p_30412_.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.gameEvent(GameEvent.EAT, this);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult interactionresult = super.mobInteract(p_30412_, hand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(p_30412_)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                } else {
                    return interactionresult;
                }
            }
        } else if (itemstack.is(Items.GUNPOWDER) && !this.isAngry()) {
            if (!p_30412_.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (this.random.nextInt(3) == 0
                    && !net.neoforged.neoforge.common.NeoForge.EVENT_BUS
                    .post(new net.neoforged.neoforge.event.entity.living.AnimalTameEvent(this, p_30412_))
                    .isCanceled()) {
                this.tame(p_30412_);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(p_30412_, hand);
        }
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int p_30404_) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, p_30404_);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID p_30400_) {
        this.persistentAngerTarget = p_30400_;
    }

    @Override
    public boolean isFood(ItemStack p_27600_) {
        return p_27600_.is(Items.GUNPOWDER);
    }

    @Nullable
    public FriendlyCreeper getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        FriendlyCreeper baby = new FriendlyCreeper(ModEntities.FRIENDLY_CREEPER.get(), level);
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            baby.setOwnerUUID(uuid);
            baby.setTame(true, true);
        }
        return baby;
    }

    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(otherAnimal instanceof FriendlyCreeper other)) {
            return false;
        } else {
            if (!other.isTame()) {
                return false;
            } else if (other.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && other.isInLove();
            }
        }
    }

    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            return switch (target) {
                case TamableAnimal tamable -> !tamable.isTame() || tamable.getOwner() != owner;
                case Player player when owner instanceof Player && !((Player) owner).canHarmPlayer(player) -> false;
                case AbstractHorse abstractHorse when abstractHorse.isTamed() -> false;
                default -> true;
            };
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeLeashed() {
        return !this.isAngry();
    }
}