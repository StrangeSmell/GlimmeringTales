package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.glimmeringtales.content.entity.misc.GTFallingBlockEntity;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.MobCategory;

public class GTEntities {

	public static final EntityEntry<GTFallingBlockEntity> FALLING = GlimmeringTales.REGISTRATE
			.<GTFallingBlockEntity>entity("falling_block", GTFallingBlockEntity::new, MobCategory.MISC)
			.properties(b -> b.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20))
			.renderer(() -> FallingBlockRenderer::new)
			.lang("Falling Block")
			.register();

	public static void register() {

	}

}
