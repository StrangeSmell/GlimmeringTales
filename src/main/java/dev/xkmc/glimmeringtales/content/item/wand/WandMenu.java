package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2backpack.content.common.BaseBagMenu;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;
import java.util.UUID;

public class WandMenu extends BaseBagMenu<WandMenu> {

	public static final SpriteManager MANAGERS = new SpriteManager(GlimmeringTales.MODID, "wand");

	public static WandMenu fromNetwork(MenuType<WandMenu> type, int windowId, Inventory inv, @Nullable RegistryFriendlyByteBuf buf) {
		assert buf != null;
		PlayerSlot<?> slot = PlayerSlot.read(buf);
		UUID id = buf.readUUID();
		return new WandMenu(windowId, inv, slot, id, null);
	}

	public WandMenu(int windowId, Inventory inventory, PlayerSlot<?> hand, UUID uuid, @Nullable Component title) {
		super(GTItems.WAND_MENU.get(), windowId, inventory, MANAGERS, hand, uuid, 1);
	}

	@Override
	protected void addSlot(String name) {
		for (int i = 0; i < 9; i++) {
			super.addSlot("x" + i);
		}
	}

}
