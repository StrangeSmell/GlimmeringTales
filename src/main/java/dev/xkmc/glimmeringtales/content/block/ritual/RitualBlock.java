package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;

public class RitualBlock {

	public static final BlockMethod ITEM = new ClickRitualMethod();
	public static final BlockMethod LINK = new RitualLinkBlockMethod();
	public static final BlockMethod START = new StartRitualMethod();

	public static final BlockMethod SIDE = new BlockEntityBlockMethodImpl<>(GTItems.ALTAR_BE, NatureSideBlockEntity.class);

}
