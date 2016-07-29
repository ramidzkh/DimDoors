package com.zixiken.dimdoors.blocks;

import java.util.Random;

import com.zixiken.dimdoors.DimDoors;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoorGold extends BlockDoor {
    public static final String ID = "doorGold";

	public BlockDoorGold() {
		super(Material.iron);
        setHardness(0.1F);
        setUnlocalizedName(ID);
	}

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER ? DimDoors.itemGoldenDoor : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {return DimDoors.itemGoldenDoor;}
}