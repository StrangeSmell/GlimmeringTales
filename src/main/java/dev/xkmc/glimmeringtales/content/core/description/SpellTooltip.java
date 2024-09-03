package dev.xkmc.glimmeringtales.content.core.description;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2magic.content.engine.context.AnalyticContext;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.engine.extension.ExtensionHolder;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2magic.content.engine.helper.EngineHelper;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

public class SpellTooltip {

	private static final IdentityHashMap<Verifiable, SpellTooltip> CACHE = new IdentityHashMap<>();

	public static SpellTooltip get(Level level, NatureSpell spell) {
		var action = spell.spell().value().action();
		var data = spell.tooltip();
		if (CACHE.containsKey(action)) {
			var e = CACHE.get(action);
			if (e.spell != action || e.components != data) {
				CACHE.clear();
			} else {
				return e;
			}
		}
		var e = new SpellTooltip(action, data);
		CACHE.put(action, e);
		return e;
	}

	private final Verifiable spell;
	private final SpellTooltipData components;
	private final LinkedHashMap<ExtensionHolder<?>, List<IExtended<?>>> map = new LinkedHashMap<>();
	private final Set<Verifiable> iterated = new HashSet<>();

	public SpellTooltip(Verifiable spell, SpellTooltipData components) {
		this.spell = spell;
		this.components = components;
		analyze();
	}

	public void analyze() {
		map.clear();
		iterated.clear();
		for (var e : components.list()) {
			map.put(e.type(), new ArrayList<>());
		}
		EngineHelper.analyze(spell, new AnalyticContext("", this::check), spell.getClass());
	}

	private void check(String s, @Nullable Verifiable v) {
		switch (v) {
			case CustomProjectileShoot p -> {
				if (iterated.contains(p)) return;
				iterated.add(p);
				var proj = p.config().value();
				var tick = proj.tick();
				if (tick != null) EngineHelper.analyze(tick, new AnalyticContext("", this::check), tick.getClass());
				for (var hit : proj.hit())
					EngineHelper.analyze(hit, new AnalyticContext("", this::check), hit.getClass());
			}
			case IExtended<?> p -> {
				if (map.containsKey(p.type())) {
					map.get(p.type()).add(p);
				}
			}
			case null -> GlimmeringTales.LOGGER.error("Null input not allowed for path {}", s);
			default -> {
			}
		}
	}

	public Component format(ResourceKey<NatureSpell> key) {
		return components.format(key.location(), this);
	}

	public void brief(ResourceKey<NatureSpell> key, List<Component> list) {
		components.brief(key.location(), list, this);
	}

	public void verify() {
		SpellDataStack stack = asStack();
		for (int i = 0; i < components.list().size(); i++) {
			var type = components.list().get(i).type();
			var ext = type.get(Component.class);
			ext.process(Wrappers.cast(stack.get(type)));
		}
	}

	public SpellDataStack asStack() {
		LinkedHashMap<ExtensionHolder<?>, Queue<IExtended<?>>> ans = new LinkedHashMap<>();
		for (var ent : map.entrySet()) {
			ans.put(ent.getKey(), new ArrayDeque<>(ent.getValue()));
		}
		return new SpellDataStack(ans);
	}

}
