package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NanoProperties implements IExtendedEntityProperties {
	
	public static String IDENTIFIER = "NANOBOT_PROPERTIES";
	
	public static HashMap<UUID, List<NanoProperties>> earlyInitProperties = new HashMap<UUID, List<NanoProperties>>(); //Properties which need to be registered
	
	public int numOfBots = 0;
	public UUID antenna;
	public Entity entity;
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("bots", numOfBots);
		if (antenna != null)
			tag.setString("antenna", antenna.toString());
		compound.setTag(IDENTIFIER, tag);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey(IDENTIFIER)) {
			NBTTagCompound tag = compound.getCompoundTag(IDENTIFIER);
			numOfBots = tag.getInteger("bots");
			if (tag.hasKey("antenna"))
				antenna = UUID.fromString(tag.getString("antenna"));
			if (antenna != null) {
				if (TileEntityAntenna.antenna_registry.containsKey(antenna)) {
					TileEntityAntenna antenna = TileEntityAntenna.antenna_registry.get(this.antenna);
					antenna.swarmNetwork.add(entity);
				} else {
					if (!earlyInitProperties.containsKey(antenna))
						earlyInitProperties.put(antenna, new ArrayList<NanoProperties>());
					List<NanoProperties> propertiesList = earlyInitProperties.get(antenna);
					propertiesList.add(this);
					earlyInitProperties.put(antenna, propertiesList);
				}
			}
		}
	}
	
	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}
}
