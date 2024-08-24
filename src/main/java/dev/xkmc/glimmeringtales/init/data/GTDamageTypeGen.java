package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellGenRegistry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.function.Supplier;

public class GTDamageTypeGen extends DamageTypeAndTagsGen {

	public GTDamageTypeGen(L2Registrate reg) {
		super(reg);
		for (var e : NatureSpellGenRegistry.LIST) {
			e.registerDamage(this);
		}
	}

	@SafeVarargs
	public final void genDamage(ResourceKey<DamageType> id, Supplier<DamageType> def, TagKey<DamageType>... tags) {
		var root = new DamageTypeRoot(GlimmeringTales.MODID, id, List.of(tags), (type) -> def.get());
		new DamageTypeHolder(id, def.get()).add(tags);
		root.add(DefaultDamageState.BYPASS_MAGIC);
		root.add(GTDamageStates.MAGIC);
	}

}
