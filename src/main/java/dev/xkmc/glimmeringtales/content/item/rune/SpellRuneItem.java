package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellInfo;
import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SpellRuneItem extends BaseRuneItem implements IWandCoreItem {

	private final ResourceKey<NatureSpell> id;

	public SpellRuneItem(Properties properties, ResourceLocation id) {
		super(properties);
		this.id = ResourceKey.create(GTRegistries.SPELL, id);
	}

	@Override
	public @Nullable ISpellHolder getSpell(ItemStack sel, Level level) {
		return level.registryAccess().holder(id).map(x -> new SpellHolder(x, entityTrace())).orElse(null);
	}

	public SpellInfo getSpellInfo(RegistryAccess access) {
		return SpellInfo.ofSpell(access.holder(id).orElse(null));
	}

}
