package org.dimdev.dimdoors.block.entity;

import java.util.Random;

import org.dimdev.annotatednbt.AnnotatedNbt;
import org.dimdev.annotatednbt.Saved;
import org.dimdev.dimdoors.ModConfig;
import org.dimdev.dimdoors.block.ModBlocks;
import org.dimdev.dimdoors.client.RiftCurves;
import org.dimdev.dimdoors.util.TeleportUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.macosx.LibSystem;


public class DetachedRiftBlockEntity extends RiftBlockEntity implements Tickable {
    private static final Random random = new Random();

    @Saved
    public boolean closing = false;
    @Saved
    public boolean stabilized = false;
    @Saved
    public int spawnedEndermanId = 0;
    @Saved
    public float size = 0;

    private boolean unregisterDisabled = false;

    @Environment(EnvType.CLIENT)
    public double renderAngle;

    public DetachedRiftBlockEntity() {
        super(ModBlockEntityTypes.DETACHED_RIFT);
    }

    @Override
    public void tick() {
        if (world == null) {
            return;
        }

        if (world.getBlockState(pos).getBlock() != ModBlocks.DETACHED_RIFT) {
            markInvalid();
            return;
        }

        if (!world.isClient() && random.nextDouble() < ModConfig.GENERAL.endermanSpawnChance) {
            EndermanEntity enderman = EntityType.ENDERMAN.spawn((ServerWorld) world, null, null, null, pos, SpawnReason.STRUCTURE, false, false);

            if (random.nextDouble() < ModConfig.GENERAL.endermanAggressiveChance) {
                if (enderman != null) {
                    enderman.setTarget(world.getClosestPlayer(enderman, 50));
                }
            }
        }

        if (closing) {
            if (size > 0) {
                size -= ModConfig.GENERAL.riftCloseSpeed;
            } else {
                world.removeBlock(pos, false);
            }
        } else if (!stabilized) {
            size += ModConfig.GENERAL.riftGrowthSpeed / (size + 1);
        }
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
        markDirty();
    }

    public void setStabilized(boolean stabilized) {
        this.stabilized = stabilized;
        markDirty();
    }

    @Override
    protected CompoundTag serialize(CompoundTag tag) {
        super.serialize(tag);
        tag.putBoolean("closing", closing);
        tag.putBoolean("stablized", stabilized);
        tag.putInt("spawnedEnderManId", spawnedEndermanId);
        tag.putFloat("size", size);
        return tag;
    }

    @Override
    protected void deserialize(CompoundTag tag) {
        super.deserialize(tag);
        closing = tag.getBoolean("closing");
        stabilized = tag.getBoolean("stablized");
        spawnedEndermanId = tag.getInt("spawnedEnderManId");
        size = tag.getFloat("size");
    }

    @Override
    public boolean isDetached() {
        return true;
    }

    @Override
    public void unregister() {
        if (!unregisterDisabled) {
            super.unregister();
        }
    }

    @Override
    public boolean receiveEntity(Entity entity, float yawOffset) {
        if (world instanceof ServerWorld)
            TeleportUtil.teleport(entity, world, pos, 0);
        return true;
    }

    public void setUnregisterDisabled(boolean unregisterDisabled) {
        this.unregisterDisabled = unregisterDisabled;
    }
}
