package io.github.andriyko69.elementalcreeperscontinued.misc;

import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class EntityOnlyExplosion {

    public static Map<Player, Vec3> explodeAt(
            Level level,
            Entity source,
            double x,
            double y,
            double z,
            double radius,
            double damageMulti,
            double launchMulti
    ) {
        if (!(level instanceof ServerLevel)) {
            return Map.of();
        }

        double diameter = radius * 2.0D;
        int minX = Mth.floor(x - diameter - 1.0D);
        int maxX = Mth.floor(x + diameter + 1.0D);
        int minY = Mth.floor(y - diameter - 1.0D);
        int maxY = Mth.floor(y + diameter + 1.0D);
        int minZ = Mth.floor(z - diameter - 1.0D);
        int maxZ = Mth.floor(z + diameter + 1.0D);

        List<Entity> entities = level.getEntities(
                source,
                new AABB(minX, minY, minZ, maxX, maxY, maxZ)
        );

        Vec3 explosionCenter = new Vec3(x, y, z);

        Explosion dummyExplosion = new Explosion(
                level,
                source,
                x, y, z,
                (float) radius,
                false,
                Explosion.BlockInteraction.KEEP
        );

        Map<Player, Vec3> hitPlayers = Maps.newHashMap();

        for (Entity entity : entities) {
            if (source instanceof TamableAnimal tamable && tamable.isTame()) {
                LivingEntity owner = tamable.getOwner();
                if (owner == entity) {
                    continue;
                }

                if (owner instanceof Player ownerPlayer && entity instanceof Player targetPlayer) {
                    if (!ownerPlayer.canHarmPlayer(targetPlayer)) {
                        continue;
                    }
                }
            }

            if (entity.ignoreExplosion(dummyExplosion)) {
                continue;
            }

            double normalizedDistance = Math.sqrt(entity.distanceToSqr(explosionCenter)) / diameter;
            if (normalizedDistance > 1.0D) {
                continue;
            }

            double dx = entity.getX() - x;
            double dy = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - y;
            double dz = entity.getZ() - z;

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (length == 0.0D) {
                continue;
            }

            dx /= length;
            dy /= length;
            dz /= length;

            float seenPercent = Explosion.getSeenPercent(explosionCenter, entity);
            double impact = (1.0D - normalizedDistance) * seenPercent;

            if (damageMulti > 0.0D) {
                float damage = (float) ((int) (
                        damageMulti * ((impact * impact + impact) / 2.0D * 7.0D * diameter + 1.0D)
                ));

                DamageSource damageSource = Explosion.getDefaultDamageSource(level, source);
                entity.hurt(damageSource, damage);
            }

            double knockback;
            if (entity instanceof LivingEntity livingEntity) {
                knockback = impact * (1.0D - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
            } else {
                knockback = impact;
            }

            dx *= knockback * launchMulti;
            dy *= knockback * launchMulti;
            dz *= knockback * launchMulti;

            if (launchMulti > 1.0D) {
                dy = Math.min(0.5D, dy);
            }

            Vec3 push = new Vec3(dx, dy, dz);
            entity.setDeltaMovement(entity.getDeltaMovement().add(push));
            entity.hurtMarked = true;

            if (entity instanceof Player player) {
                if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                    hitPlayers.put(player, push);
                }
            }
        }

        return hitPlayers;
    }
}