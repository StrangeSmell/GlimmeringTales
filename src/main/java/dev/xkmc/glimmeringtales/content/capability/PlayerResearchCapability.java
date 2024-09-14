package dev.xkmc.glimmeringtales.content.capability;

import dev.xkmc.glimmeringtales.content.research.core.ResearchData;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

@SerialClass
public class PlayerResearchCapability extends PlayerCapabilityTemplate<PlayerResearchCapability> {

	@SerialField
	private final LinkedHashMap<ResourceLocation, ResearchData> research = new LinkedHashMap<>();

	@Nullable
	public ResearchData put(ResourceLocation id, ResearchData dat) {
		return research.put(id,dat);
	}

	@Nullable
	public ResearchData get(ResourceLocation id) {
		return research.get(id);
	}

	public void sync(ServerPlayer sp) {
		GTRegistries.RESEARCH.type().network.toClient(sp);
	}

}
