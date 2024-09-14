package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record GraphToServerPacket(
		ResourceLocation id, ResearchData data
) implements SerialPacketBase<GraphToServerPacket> {

	@Override
	public void handle(Player player) {
		PlayerResearch.of(player).save(id, data);
	}

}
