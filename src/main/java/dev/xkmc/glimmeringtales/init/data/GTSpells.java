package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellGenRegistry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import net.minecraft.data.worldgen.BootstrapContext;

public class GTSpells {

	public static void genSpells(BootstrapContext<SpellAction> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.register(ctx);
		}
	}

	public static void genProjectiles(BootstrapContext<ProjectileConfig> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.registerProjectile(ctx);
		}
	}

	public static void genNature(BootstrapContext<NatureSpell> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.regNature(ctx);
		}
	}

	public static void genBlockMap(RegistrateDataMapProvider pvd) {
		var builder = pvd.builder(GTRegistries.BLOCK.reg());
		for (var e : NatureSpellGenRegistry.LIST) {
			e.regBlock(builder);
		}
	}

}
