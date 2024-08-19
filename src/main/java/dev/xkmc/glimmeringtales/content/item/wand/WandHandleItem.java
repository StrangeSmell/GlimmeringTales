package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class WandHandleItem extends Item {

	private final float size, offset;

	public WandHandleItem(Properties prop, float size, float offset) {
		super(prop);
		this.size = size;
		this.offset = offset;
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_handle"));
	}

	public float size() {
		return size;
	}

	public float offset() {
		return offset;
	}
}
