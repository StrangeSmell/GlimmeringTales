package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GTConfigs {

	public static class Client extends ConfigInit {

		public final ModConfigSpec.IntValue compassSearchRadius;
		public final ModConfigSpec.IntValue compassSearchTrialsPerTick;

		public Client(Builder builder) {
			markL2();
			builder.push("compass", "Compass properties");
			compassSearchRadius = builder.text("Amethyst Compass search radius")
					.defineInRange("compassSearchRadius", 64, 16, 128);
			compassSearchTrialsPerTick = builder.text("Amethyst Compass search trials per tick")
					.defineInRange("compassSearchTrialsPerTick", 500, 16, 10000);
			builder.pop();

		}
	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue crystalOfFlameRequirement;
		public final ModConfigSpec.IntValue crystalOfWinterstormRequirement;
		public final ModConfigSpec.IntValue wandInteractionDistance;

		public Server(Builder builder) {
			markL2();
			wandInteractionDistance = builder
					.text("Wand interaction range")
					.defineInRange("wandInteractionDistance", 24, 4, 64);
			builder.push("materials", "Material properties");
			crystalOfFlameRequirement = builder
					.text("Crystal of Flame: Lava consumption")
					.defineInRange("crystalOfFlameRequirement", 64, 1, 1000);
			crystalOfWinterstormRequirement = builder
					.text("Crystal of Winterstorm: Powder Snow consumption")
					.defineInRange("crystalOfWinterstormRequirement", 64, 1, 1000);
			builder.pop();
		}
	}

	public static final Client CLIENT = GlimmeringTales.REGISTRATE.registerClient(Client::new);
	public static final Server SERVER = GlimmeringTales.REGISTRATE.registerSynced(Server::new);

}
