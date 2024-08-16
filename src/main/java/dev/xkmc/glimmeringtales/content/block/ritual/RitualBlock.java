package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.l2modularblock.type.BlockMethod;

public class RitualBlock {

	public static final BlockMethod ITEM = new ClickRitualMethod();
	public static final BlockMethod LINK = new RitualLinkBlockMethod();
	public static final BlockMethod START = new StartRitualMethod();

}
