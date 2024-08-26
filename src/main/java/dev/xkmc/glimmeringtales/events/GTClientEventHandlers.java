package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.content.core.searcher.BlockSearcher;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GTClientEventHandlers {

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		BlockSearcher.iterate(player, s -> s.tick(player));
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void levelRenderLast(RenderLevelStageEvent event) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;
		BlockSearcher.iterate(player, s -> s.render());
	}

}
