package dev.xkmc.glimmeringtales.content.item.curio;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public record AttributeData(ArrayList<Entry> list) {

	public static AttributeData of(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
		return of(entry(attribute, value, operation));
	}

	public static Entry add(Holder<Attribute> attribute, double value) {
		return new Entry(attribute, value, AttributeModifier.Operation.ADD_VALUE);
	}

	public static Entry base(Holder<Attribute> attribute, double value) {
		return new Entry(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static Entry entry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
		return new Entry(attribute, value, operation);
	}

	public static AttributeData of(Entry... entries) {
		return new AttributeData(new ArrayList<>(List.of(entries)));
	}

	public record Entry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

	}

}
