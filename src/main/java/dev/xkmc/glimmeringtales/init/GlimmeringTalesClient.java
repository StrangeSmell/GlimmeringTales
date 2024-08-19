package dev.xkmc.glimmeringtales.init;

import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GlimmeringTalesClient {

	@SubscribeEvent
	public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void registerItemDecoration(RegisterItemDecorationsEvent event) {

	}

	@SubscribeEvent
	public static void onResourceReload(RegisterClientReloadListenersEvent event) {

	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		for (var item : GTItems.CORES) {
			event.register(item.get().model());
		}
		for (var item : GTItems.HANDLES) {
			event.register(item.get().model());
		}
	}

}
