package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public final class ResearchData {

	public static ResearchData create(int n) {
		return new ResearchData(new HexHandler(3).write(), HexOrder.create(n), SpellResearch.UNLOCKED);
	}

	@SerialField
	private HexHandler.Data hex;
	@SerialField
	private HexOrder order;
	@SerialField
	private int cost;

	@Deprecated
	public ResearchData() {
	}

	public ResearchData(
			HexHandler.Data hex,
			HexOrder order,
			int cost
	) {
		this.hex = hex;
		this.order = order;
		this.cost = cost;
	}

	public int cost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public HexHandler hex() {
		return new HexHandler(hex);
	}

	public HexOrder order() {
		return order;
	}

	public void update(HexHandler handler, HexOrder order, int cost) {
		this.hex = handler.write();
		this.order = order;
		this.cost = cost;
	}

}
