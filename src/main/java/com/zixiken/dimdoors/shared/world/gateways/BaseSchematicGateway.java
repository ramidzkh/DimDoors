package com.zixiken.dimdoors.shared.world.gateways;

import com.zixiken.dimdoors.DimDoors;
import com.zixiken.dimdoors.shared.SchematicHandler;
import com.zixiken.dimdoors.shared.util.Schematic;
import com.zixiken.dimdoors.shared.util.SchematicConverter;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseSchematicGateway extends BaseGateway {
    private Schematic schematic;

    public BaseSchematicGateway(String name) {
        String schematicJarDirectory = "/assets/dimdoors/gateways/";

        //Initialising the possible locations/formats for the schematic file
        InputStream oldVersionSchematicStream = DimDoors.class.getResourceAsStream(schematicJarDirectory + name + ".schematic"); //@todo also check for other schematics

        //determine which location to load the schematic file from (and what format)
        DataInputStream schematicDataStream = null;
        boolean streamOpened = false;
        if (oldVersionSchematicStream != null) {
            schematicDataStream = new DataInputStream(oldVersionSchematicStream);
            streamOpened = true;
        } else {
            DimDoors.warn(SchematicHandler.class, "Schematic '" + name + "' was not found in the jar or config directory, neither with the .schem extension, nor with the .schematic extension.");
        }

        NBTTagCompound schematicNBT;
        this.schematic = null;
        if (streamOpened) {
            try {
                schematicNBT = CompressedStreamTools.readCompressed(schematicDataStream);
                schematic = SchematicConverter.loadOldDimDoorSchematicFromNBT(schematicNBT, name);
                schematicDataStream.close();
            } catch (IOException ex) {
                Logger.getLogger(SchematicHandler.class.getName()).log(Level.SEVERE, "Schematic file for " + name + " could not be read as a valid schematic NBT file.", ex);
            } finally {
                try {
                    schematicDataStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(SchematicHandler.class.getName()).log(Level.SEVERE, "Error occured while closing schematicDataStream", ex);
                }
            }
        }
    }

    @Override
    public boolean generate(World world, int x, int y, int z) {
        Schematic.place(schematic, world, x, y, z);
        this.generateRandomBits(world, x, y, z);


        return true;
    }

    /**
     * Generates randomized portions of the gateway structure (e.g. rubble, foliage)
     *
     * @param world - the world in which to generate the gateway
     * @param x     - the x-coordinate at which to center the gateway; usually where the door is placed
     * @param y     - the y-coordinate of the block on which the gateway may be built
     * @param z     - the z-coordinate at which to center the gateway; usually where the door is placed
     */
    protected void generateRandomBits(World world, int x, int y, int z) {
    }
}