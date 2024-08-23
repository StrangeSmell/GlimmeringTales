package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockRuneItem extends Item implements IBlockSpellItem {

	private final Supplier<Block> block;

	public BlockRuneItem(Properties properties, Supplier<Block> block) {
		super(properties);
		this.block = block;
	}

	public Optional<BlockSpell> getSpell(RegistryAccess access) {
		return Optional.ofNullable(GTRegistries.BLOCK.get(access,
				BuiltInRegistries.BLOCK.wrapAsHolder(block.get())));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		getSpell(level.registryAccess()).ifPresent(e -> e.spell().value().blockRuneDesc(list));
	}

	@Override
	public int entityTrace() {
		return 64; //TODO
	}

	@Override
	public boolean castSpell(SpellCastContext user) {
		var opt = getSpell(user.level().registryAccess());
		if (opt.isEmpty()) return false;
		var spell = opt.get();
		var ctx = BlockSpellContext.entitySpellContext(user.user(), entityTrace(), spell.noBlockOffset());
		if (ctx == null) return false;
		if (!user.level().isClientSide()) {
			execute(spell.spell().value(), ctx.ctx(), user, DefaultAffinity.INS);
		}
		return true;
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_core"));
	}

}
