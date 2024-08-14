package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.content.item.materials.LightningImmuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellCoreItem extends LightningImmuneItem implements IBlockSpellItem {

	public SpellCoreItem(Properties prop) {
		super(prop);
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

	@Override
	public int entityTrace() {
		return 0;
	}

	@Nullable
	private ElementAffinity getAffinity(Level level) {
		return GTRegistries.AFFINITY.get(level.registryAccess(), builtInRegistryHolder());
	}

	public boolean castSpell(SpellCastContext user) {
		var ctx = BlockSpellContext.blockSpellContext(user.user(), 64);//TODO
		if (ctx == null) return false;
		var spell = GTRegistries.BLOCK.get(user.level().registryAccess(), ctx.state().getBlockHolder());
		if (spell == null) return false;
		var nature = spell.spell().value();
		var aff = getAffinity(user.level());
		if (aff == null || !aff.affinity().containsKey(nature.elem()))
			return false;
		if (!user.level().isClientSide()) {
			execute(nature, ctx.ctx(), user, aff);
		}
		return true;
	}

}
