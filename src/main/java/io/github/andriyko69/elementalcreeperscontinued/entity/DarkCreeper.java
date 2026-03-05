package io.github.andriyko69.elementalcreeperscontinued.entity;

import com.mojang.datafixers.util.Pair;
import io.github.andriyko69.elementalcreeperscontinued.Config;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DarkCreeper extends ElementalCreeper {

    public DarkCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
    }

    @Override
    public void creeperEffect() {
        double radius = Config.darkCreeperExplosionRadius;
        if (this.isPowered()) {
            radius *= 1.5;
        }

        double rSqr = Math.pow(radius, 2);

        Level level = this.level();
        for (int x = (int) -radius - 1; x <= radius; x++)
            for (int y = (int) -radius - 1; y <= radius; y++)
                for (int z = (int) -radius - 1; z <= radius; z++) {
                    double distSqr = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);

                    if (distSqr <= rSqr) {
                        BlockPos blockPos = new BlockPos((int) this.getX() + x, (int) this.getY() + y,
                                (int) this.getZ() + z);
                        BlockState blockState = level.getBlockState(blockPos);

                        Explosion dummyExplosion = new Explosion(
                                level,
                                this,
                                this.getX(), this.getY(), this.getZ(),
                                (float) radius,
                                false,
                                Explosion.BlockInteraction.KEEP
                        );

                        // Copied and modified from Explosion.class
                        if (Config.darkCreeperDestroyBlocks.contains(blockState.getBlock())) {
                            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                            BlockPos blockPos1 = blockPos.immutable();
                            if (blockState.canDropFromExplosion(level, blockPos, dummyExplosion)) {
                                if (level instanceof ServerLevel serverlevel) {
                                    BlockEntity blockentity = blockState.hasBlockEntity()
                                            ? level.getBlockEntity(blockPos)
                                            : null;
                                    LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel))
                                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos))
                                            .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                                            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity)
                                            .withOptionalParameter(LootContextParams.THIS_ENTITY, this);

                                    blockState.spawnAfterBreak(serverlevel, blockPos, ItemStack.EMPTY, false);
                                    blockState.getDrops(lootparams$builder).forEach((p_46074_) -> addBlockDrops(objectarraylist, p_46074_, blockPos1));
                                }
                                blockState.onBlockExploded(level, blockPos, dummyExplosion);
                            }

                            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                                Block.popResource(this.level(), pair.getSecond(), pair.getFirst());
                            }
                        }
                    }
                }

        handleNetworkedExplosionEffects(radius, SoundEvents.CANDLE_EXTINGUISH);
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> pairs, ItemStack itemStack,
                                      BlockPos p_46070_) {
        int i = pairs.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = pairs.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, itemStack)) {
                ItemStack itemStack1 = ItemEntity.merge(itemstack, itemStack, 16);
                pairs.set(j, Pair.of(itemStack1, pair.getSecond()));
                if (itemStack.isEmpty()) {
                    return;
                }
            }
        }
        pairs.add(Pair.of(itemStack, p_46070_));
    }
}