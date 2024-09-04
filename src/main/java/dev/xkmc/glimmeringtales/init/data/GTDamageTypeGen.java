package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellGenRegistry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.util.MathHelper;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class GTDamageTypeGen extends DamageTypeAndTagsGen {

	public static final TagKey<DamageType> SPELL = TagKey.create(Registries.DAMAGE_TYPE, GlimmeringTales.loc("spells"));

	public static final ResourceKey<DamageType> THUNDER = ResourceKey.create(Registries.DAMAGE_TYPE, GlimmeringTales.loc("lightning"));

	@SafeVarargs
	public static TagKey<DamageType>[] magic(TagKey<DamageType>... tags) {
		return MathHelper.merge(tags, Tags.DamageTypes.IS_MAGIC, DamageTypeTags.BYPASSES_ARMOR);
	}

	@SafeVarargs
	public static TagKey<DamageType>[] freeze(TagKey<DamageType>... tags) {
		return MathHelper.merge(tags, DamageTypeTags.IS_FREEZING, DamageTypeTags.NO_KNOCKBACK, DamageTypeTags.BYPASSES_ARMOR);
	}

	protected final List<DamageTypeWrapper> list = new ArrayList<>();

	public GTDamageTypeGen(L2Registrate reg) {
		super(reg);
		genDamage(THUNDER, () -> new DamageType("lightningBolt", 0.1F),
				GTRegistries.THUNDER.get().damgeTag(), DamageTypeTags.IS_LIGHTNING, DamageTypeTags.NO_KNOCKBACK
		);

		for (var e : NatureSpellGenRegistry.LIST) {
			e.registerDamage(this);
		}
		DamageTypeRoot.configureGeneration(Set.of(L2DamageTracker.MODID, GlimmeringTales.MODID), GlimmeringTales.MODID, list);
	}

	@SafeVarargs
	public final void genDamage(ResourceKey<DamageType> id, Supplier<DamageType> def, TagKey<DamageType>... tags) {
		var root = new DamageTypeRoot(GlimmeringTales.MODID, id, List.of(tags), (type) -> def.get());
		new DamageTypeHolder(id, def.get()).add(tags);
		root.add(DefaultDamageState.BYPASS_MAGIC);
		root.add(DefaultDamageState.BYPASS_COOLDOWN);
		root.add(GTDamageStates.MAGIC);
	}

	@Override
	protected void addDamageTypes(BootstrapContext<DamageType> ctx) {
		super.addDamageTypes(ctx);
		DamageTypeRoot.generateAll();
		for (DamageTypeWrapper wrapper : list) {
			ctx.register(wrapper.type(), wrapper.getObject());
		}
	}

	@Override
	protected void addDamageTypeTags(RegistrateTagsProvider.Impl<DamageType> pvd) {
		super.addDamageTypeTags(pvd);
		DamageTypeRoot.generateAll();
		for (DamageTypeWrapper wrapper : list) {
			wrapper.gen(pvd::addTag);
		}
		var spell = pvd.addTag(SPELL);
		for (var e : GTRegistries.ELEMENT.reg()) {
			pvd.addTag(e.damgeTag());
			spell.addTag(e.damgeTag());
		}
	}

}
