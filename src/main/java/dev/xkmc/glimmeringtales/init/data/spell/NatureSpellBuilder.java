package dev.xkmc.glimmeringtales.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltip;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.l2core.util.MathHelper;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NatureSpellBuilder extends NatureSpellEntry {

	private final ResourceLocation id;
	private final SpellElement elem;

	public DataGenCachedHolder<ProjectileConfig> proj;
	public DataGenCachedHolder<SpellAction> spell;
	public DataGenCachedHolder<NatureSpell> nature;
	public ResourceKey<DamageType> damage;

	private Function<NatureSpellBuilder, ProjectileConfig> projectileFactory;
	private Function<NatureSpellBuilder, SpellAction> spellFactory;
	private Function<Holder<SpellAction>, NatureSpell> natureFactory;
	private Function<ResourceLocation, String> langFactory;
	private SpellDamageEntry damageEntry;
	private SpellDesc desc;

	private DataGenContext cache;

	private final List<BiConsumer<BlockSpellBuilder, Holder<NatureSpell>>> blockFactories = new ArrayList<>();

	public NatureSpellBuilder(ResourceLocation id, SpellElement elem) {
		this.id = id;
		this.elem = elem;
	}

	@SafeVarargs
	public final NatureSpellBuilder damageVanilla(Supplier<DamageType> def, TagKey<DamageType>... tags) {
		damage = damage(id);
		this.damageEntry = new SpellDamageEntry(msg -> def.get(), null, null, MathHelper.merge(tags, elem.damgeTag()));
		return this;
	}

	@SafeVarargs
	public final NatureSpellBuilder damageCustom(Function<String, DamageType> def, String noPlayer, String withPlayer, TagKey<DamageType>... tags) {
		damage = damage(id);
		this.damageEntry = new SpellDamageEntry(def, noPlayer, withPlayer, MathHelper.merge(tags, elem.damgeTag()));
		return this;
	}

	public NatureSpellBuilder projectile(Function<NatureSpellBuilder, ProjectileConfig> factory) {
		proj = projectile(id);
		this.projectileFactory = factory;
		return this;
	}

	public NatureSpellBuilder spell(Function<NatureSpellBuilder, SpellAction> factory) {
		spell = spell(id);
		this.spellFactory = factory;
		return this;
	}

	public NatureSpellBuilder cost(int cost) {
		return cost(cost, 0);
	}

	public NatureSpellBuilder cost(int cost, int max) {
		nature = nature(id);
		this.natureFactory = e -> new NatureSpell(e, elem, cost, max, desc == null ? warnEmpty() : desc.data);
		return this;
	}

	public NatureSpellBuilder block(BiConsumer<BlockSpellBuilder, Holder<NatureSpell>> cons) {
		this.blockFactories.add(cons);
		return this;
	}

	public NatureSpellBuilder lang(String name) {
		return lang(e -> name);
	}

	public NatureSpellBuilder lang(Function<ResourceLocation, String> factory) {
		this.langFactory = factory;
		return this;
	}

	public NatureSpellBuilder desc(String brief, String detail, SpellTooltipData data) {
		desc = new SpellDesc(brief, detail, data);
		return this;
	}

	public Holder<DamageType> damage() {
		return cache.damage(damage);
	}

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		cache = new DataGenContext(ctx);
		nature.gen(ctx, natureFactory.apply(spell));
		cache = null;
		try {
			new SpellTooltip(nature.value().spell().value().action(), desc.data).verify();
		} catch (Exception e) {
			GlimmeringTales.LOGGER.error("Spell {} failed description check", id);
			GlimmeringTales.LOGGER.throwing(e);
		}
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		var b = new BlockSpellBuilder(builder);
		for (var e : blockFactories) {
			e.accept(b, nature);
		}
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		cache = new DataGenContext(ctx);
		spellFactory.apply(this).verifyOnBuild(ctx, spell);
		cache = null;
	}

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(id), langFactory.apply(id));
		if (damageEntry != null) {
			if (damageEntry.noPlayer != null) {
				pvd.add("death.attack." + id.toLanguageKey(), damageEntry.noPlayer);
			}
			if (damageEntry.withPlayer != null) {
				pvd.add("death.attack." + id.toLanguageKey() + ".player", damageEntry.withPlayer);
			}
		}
		if (desc != null) {
			pvd.add(SpellTooltipData.brief(id), desc.brief);
			pvd.add(SpellTooltipData.detail(id), desc.detail);
		}
	}

	@Override
	public void registerDamage(GTDamageTypeGen gen) {
		if (damageEntry != null) {
			gen.genDamage(damage, () -> damageEntry.def().apply(id.toLanguageKey()), damageEntry.tags());
		}
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		if (projectileFactory != null && proj != null) {
			cache = new DataGenContext(ctx);
			projectileFactory.apply(this).verifyOnBuild(ctx, proj);
			cache = null;
		}
	}

	private SpellTooltipData warnEmpty() {
		GlimmeringTales.LOGGER.error("Spell {} does not have description setup", id);
		return SpellTooltipData.of();
	}

	public record BlockSpellBuilder(DataMapProvider.Builder<BlockSpell, Block> builder) {

		public void add(Block block, BlockSpell spell) {
			builder.add(block.builtInRegistryHolder(), spell, false);
		}

		public void add(Holder<Block> block, BlockSpell spell) {
			builder.add(block, spell, false);
		}

		public void add(TagKey<Block> block, BlockSpell spell) {
			builder.add(block, spell, false);
		}

	}

	public record SpellDamageEntry(
			Function<String, DamageType> def,
			@Nullable String noPlayer, @Nullable String withPlayer,
			TagKey<DamageType>... tags
	) {

		@SafeVarargs
		public SpellDamageEntry {
		}

	}

	public record SpellDesc(String brief, String detail, SpellTooltipData data) {

	}

}
