package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RuneItem extends Item implements IWandCoreItem {

	private final Supplier<Block> block;

	public RuneItem(Properties properties, Supplier<Block> block) {
		super(properties);
		this.block = block;
	}

	public Optional<Holder<NatureSpell>> getSpell(RegistryAccess access) {
		return Optional.ofNullable(GTRegistries.BLOCK.get(access,
						BuiltInRegistries.BLOCK.wrapAsHolder(block.get())))
				.map(BlockSpell::spell);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		getSpell(level.registryAccess()).ifPresent(e -> e.value().runeDesc(list));
	}

	public InteractionResultHolder<ItemStack> onUse(Level level, Player player, ItemStack stack) {
		var spell = getSpell(level.registryAccess());
		if (spell.isPresent() && castSpell(stack, level, player, spell.get().value()))
			return InteractionResultHolder.success(stack);
		return InteractionResultHolder.fail(stack);
	}

	private boolean castSpell(ItemStack stack, Level level, Player user, NatureSpell spell) {
		var ctx = BlockSpellContext.entitySpellContext(user, 64);
		if (ctx == null) return false;
		if (!level.isClientSide()) {
			spell.spell().value().execute(ctx.ctx());
			spell.cooldown(user, stack, 1);
		}
		return true;
	}

}
