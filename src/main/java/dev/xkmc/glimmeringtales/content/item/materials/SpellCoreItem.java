package dev.xkmc.glimmeringtales.content.item.materials;

import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.content.item.rune.BlockSpellContext;
import dev.xkmc.glimmeringtales.content.item.rune.IWandCoreItem;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellCoreItem extends LightningImmuneItem implements IWandCoreItem {

	public SpellCoreItem(Properties prop) {
		super(prop);
	}

	@Override
	public InteractionResultHolder<ItemStack> onUse(Level level, Player player, ItemStack stack) {
		if (castSpell(level, player, stack)) {
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.fail(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		var aff = getAffinity(level);
		if (aff == null) return;
		for (var e : aff.affinity().entrySet()) {
			Component val = Component.literal((int) Math.round(e.getValue() * 100) + "%")
					.withStyle(ChatFormatting.BLUE);
			list.add(GTLang.TOOLTIP_AFFINITY.get(e.getKey().coloredDesc(), val).withStyle(ChatFormatting.GRAY));
		}
	}

	@Nullable
	private ElementAffinity getAffinity(Level level) {
		return GTRegistries.AFFINITY.get(level.registryAccess(), builtInRegistryHolder());
	}

	private boolean castSpell(Level level, Player player, ItemStack stack) {
		var ctx = BlockSpellContext.blockSpellContext(player, 64);
		if (ctx == null) return false;
		var spell = GTRegistries.BLOCK.get(level.registryAccess(), ctx.state().getBlockHolder());
		if (spell == null) return false;
		var nature = spell.spell().value();
		var aff = getAffinity(level);
		if (aff == null || !aff.affinity().containsKey(nature.elem()))
			return false;
		if (!level.isClientSide()) {
			nature.spell().value().execute(ctx.ctx());
			nature.cooldown(player, stack, aff.affinity().getOrDefault(nature.elem(), 1d));
		}
		return true;
	}

}
