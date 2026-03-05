package io.github.andriyko69.elementalcreeperscontinued.entity;

import io.github.andriyko69.elementalcreeperscontinued.Config;
import io.github.andriyko69.elementalcreeperscontinued.misc.EntityOnlyExplosion;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class PsychicCreeper extends ElementalCreeper {

    public PsychicCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    @Override
    public void creeperEffect() {
        double radius = Config.psychicCreeperExplosionRadius;
        if (this.isPowered()) {
            radius *= 1.5d;
        }
        Map<Player, Vec3> hitPlayers = EntityOnlyExplosion.explodeAt(this.level(), this, this.getX(), this.getY(),
                this.getZ(), radius, 0, Config.psychicCreeperLaunchMultiplier);

        handleNetworkedExplosionEffects(radius, hitPlayers, SoundEvents.ENDER_PEARL_THROW);
    }
}