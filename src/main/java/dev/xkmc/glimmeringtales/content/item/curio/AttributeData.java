package dev.xkmc.glimmeringtales.content.item.curio;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public record AttributeData(ArrayList<Entry> list) {

	public static AttributeData of(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
		return new AttributeData(new ArrayList<>(List.of(new Entry(attribute, value, operation))));
	}

	public record Entry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

	}

}
