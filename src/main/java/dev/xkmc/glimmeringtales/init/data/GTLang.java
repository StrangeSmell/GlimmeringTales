package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum GTLang {
	TOOLTIP_FILL("tooltip.fill_crystal", "Progress: %s / %s", 2),
	TOOLTIP_BLOCK("tooltip.consume_block", "Right click %s to absorb essence", 1),
	TOOLTIP_VINE("tooltip.nearby_grow", "Grows when adjacent crops grow to maturity naturally", 0),
	TOOLTIP_AFFINITY("tooltip.affinity", "[%s] affinity: %s", 2),
	TOOLTIP_SPELL("tooltip.spell", "%s - [%s]", 2),
	TOOLTIP_COST("tooltip.cost", "Spell cost: %s", 1),
	TOOLTIP_COST_CONT("tooltip.cost_cont", "%s per tick", 1),
	TOOLTIP_COST_CAPPED("tooltip.cost_capped", "%s per tick, at most %s", 2),
	TOOLTIP_FOCUS("tooltip.focus", "Focus cost: %s", 1),

	TOOLTIP_MAGIC("item.glove_magic", "Convert all spell damage to magic damage", 0),
	TOOLTIP_ABYSS("item.glove_abyss", "Infuse all spell damage with abyss damage", 0),

	OVERLAY_DESTROY("overlay.destroy", "Consume target block", 0),
	OVERLAY_MANA("overlay.mana", "Mana: %s / %s", 2),
	OVERLAY_FOCUS("overlay.focus", "Focus: %s / %s", 2),

	DESC_DAMAGE("desc.damage", "Damage", 0),
	DESC_FIRE("desc.fire", "Fire", 0),
	DESC_EXPLOSION("desc.explosion", "Explosion", 0),
	DESC_FREEZING("desc.freezing", "Freezing", 0),
	DESC_PROJECTILE("desc.projectile", "Projectile", 0),
	DESC_LIGHTNING("desc.lightning", "Lightning", 0),
	DESC_MAGIC("desc.magic", "Magic", 0),
	DESC_SPACE("desc.space", " ", 0),
	DESC_DMG("desc.damage_number", "%s", 1),

	JEI_STRIKE_ITEM("jei.strike_item", "Lightning Strikes Item", 0),
	JEI_STRIKE_BLOCK("jei.strike_block", "Lightning Strikes Block", 0),
	JEI_TRANSFORM("jei.transform", "Item Special Transformation", 0),
	JEI_RITUAL("jei.ritual", "Ritual", 0),
	;

	final String id, def;
	final int count;

	GTLang(String id, String def, int count) {
		this.id = GlimmeringTales.MODID + "." + id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != this.count) {
			throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
		} else {
			return Component.translatable(this.id, objs);
		}
	}

	public static void addTranslations(RegistrateLangProvider pvd) {
		for (var e : values()) {
			pvd.add(e.id, e.def);
		}
	}

}
