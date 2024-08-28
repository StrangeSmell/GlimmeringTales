package dev.xkmc.glimmeringtales.content.item.curio;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class AttributeCurioItem extends GTCurioItem {

	private static final ResourceLocation DUMMY = GlimmeringTales.loc("dummy");

	public AttributeCurioItem(Properties properties) {
		super(properties);
	}

	private Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(RegistryAccess access, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> ans = LinkedHashMultimap.create();
		var attrs = GTRegistries.ITEM_ATTR.get(access, stack.getItemHolder());
		if (attrs == null) return ans;
		for (var e : attrs.list()) {
			ans.put(e.attribute(), new AttributeModifier(id, e.value(), e.operation()));
		}
		return ans;
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		return getAttributeModifiers(ctx.entity().level().registryAccess(), id, stack);
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		var attrs = getAttributeModifiers(Proxy.getRegistryAccess(), DUMMY, stack);
		return AttrTooltip.modifyTooltip(tooltips, attrs, false);
	}

}
