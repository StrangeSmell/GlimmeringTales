package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GTConfigs {

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue crystalOfFlameRequirement;
		public final ModConfigSpec.IntValue crystalOfWinterstormRequirement;

		public Server(Builder builder) {
			markL2();
			builder.push("materials", "Material properties");
			crystalOfFlameRequirement = builder
					.text("Crystal of Flame: Lava consumption")
					.defineInRange("crystalOfFlameRequirement", 64, 1, 1000);
			crystalOfWinterstormRequirement = builder
					.text("Crystal of Winterstorm: Powder Snow consumption")
					.defineInRange("crystalOfWinterstormRequirement", 64, 1, 1000);

		}
	}

	public static final Server SERVER = GlimmeringTales.REGISTRATE.registerSynced(Server::new);

}
