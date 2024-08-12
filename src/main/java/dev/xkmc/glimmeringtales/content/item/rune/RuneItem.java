package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RuneItem extends Item {

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
		getSpell(level.registryAccess()).ifPresent(e -> e.value().desc(list));
	}

	public InteractionResultHolder<ItemStack> onUse(Level level, Player player, ItemStack stack) {
		var spell = getSpell(level.registryAccess());
		if (spell.isPresent() && castSpell(stack, level, player, spell.get().value().spell()))
			return InteractionResultHolder.success(stack);
		return InteractionResultHolder.fail(stack);
	}

	private boolean castSpell(ItemStack stack, Level level, LivingEntity user, Holder<SpellAction> spell) {
		SpellContext ctx = blockSpellContext(user, spell.value(), 0, 1, 64);
		if (ctx == null) return false;
		if (!level.isClientSide()) {
			spell.value().execute(ctx);
		}
		return true;
	}

	@Nullable
	public static SpellContext blockSpellContext(LivingEntity user, SpellAction spell, int useTick, double power, int distance) {
		Level level = user.level();
		Vec3 dir = user.getEyePosition();
		Vec3 forward = SpellContext.getForward(user);
		Vec3 end = dir.add(forward.scale(distance));
		BlockHitResult bhit = level.clip(new ClipContext(dir, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user));
		if (bhit.getType() == HitResult.Type.MISS) return null;
		Vec3 pos = bhit.getBlockPos().getCenter();
		var ori = Orientation.regular().asNormal();
		long seed = 0L;
		if (!level.isClientSide()) {
			seed = ThreadLocalRandom.current().nextLong();
		}
		return new SpellContext(user, pos, ori, seed, useTick, power);
	}

}
