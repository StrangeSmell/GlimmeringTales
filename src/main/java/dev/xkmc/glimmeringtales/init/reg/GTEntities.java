package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.glimmeringtales.content.entity.misc.GTFallingBlockEntity;
import dev.xkmc.glimmeringtales.content.entity.misc.GTLightningBolt;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.world.entity.MobCategory;

public class GTEntities {

	public static final EntityEntry<GTFallingBlockEntity> FALLING = GlimmeringTales.REGISTRATE
			.<GTFallingBlockEntity>entity("falling_block", GTFallingBlockEntity::new, MobCategory.MISC)
			.properties(b -> b.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20))
			.renderer(() -> FallingBlockRenderer::new)
			.lang("Falling Block")
			.register();

	public static final EntityEntry<GTLightningBolt> LIGHTNING = GlimmeringTales.REGISTRATE
			.entity("lightning_bolt", GTLightningBolt::new, MobCategory.MISC)
			.properties(b -> b.noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE))
			.renderer(() -> LightningBoltRenderer::new)
			.lang("Lightning Bolt")
			.register();

	public static void register() {

	}

}
