package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellRuneItem extends Item implements IWandCoreItem {

	private final ResourceKey<NatureSpell> id;

	public SpellRuneItem(Properties properties, ResourceLocation id) {
		super(properties);
		this.id = ResourceKey.create(GTRegistries.SPELL, id);
	}

	@Override
	public int entityTrace() {
		return GTConfigs.SERVER.wandInteractionDistance.get();
	}

	@Override
	public @Nullable ISpellHolder getSpell(ItemStack sel, Level level) {
		return level.registryAccess().holder(id).map(x -> new SpellHolder(x, entityTrace())).orElse(null);
	}

	@Override
	public List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
		var spell = player.level().registryAccess().holder(id);
		return spell.map(e -> e.value().getSpellCastTooltip(player, wand)).orElseGet(List::of);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		var spell = level.registryAccess().holder(id);
		spell.ifPresent(e -> NatureSpell.runeItemSpellDesc(e, level, list));
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_core"));
	}


}
