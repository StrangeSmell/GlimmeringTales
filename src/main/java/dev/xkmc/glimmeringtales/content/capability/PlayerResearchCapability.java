package dev.xkmc.glimmeringtales.content.capability;

import dev.xkmc.glimmeringtales.content.research.core.ResearchData;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

@SerialClass
public class PlayerResearchCapability extends PlayerCapabilityTemplate<PlayerResearchCapability> {

	@SerialField
	private final LinkedHashMap<ResourceLocation, ResearchData> research = new LinkedHashMap<>();

	public void put(ResourceLocation id, ResearchData dat) {
		research.put(id,dat);
	}

	@Nullable
	public ResearchData get(ResourceLocation id) {
		return research.get(id);
	}

	public void sync() {
	}

}
