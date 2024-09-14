package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;

import java.util.ArrayList;

public final class ResearchData {

	public static ResearchData create() {
		return new ResearchData(new HexHandler(3).write(), HexData.create(), SpellResearch.UNLOCKED);
	}

	private HexHandler.Data hex;
	private HexData shape;
	private int cost;

	public ResearchData(
			HexHandler.Data hex,
			HexData shape,
			int cost
	) {
		this.hex = hex;
		this.shape = shape;
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

	public HexData shape() {
		return shape;
	}

	public void update(HexHandler handler, HexData data, int cost) {
		this.hex = handler.write();
		this.shape = data;
		this.cost = cost;
	}

	public record HexData(int[] order, ArrayList<SpellElement> list) {

		public static HexData create() {
			ArrayList<SpellElement> list = new ArrayList<>();
			for (var e : GTRegistries.ELEMENT.reg()) {
				list.add(e);
			}
			int[] order = new int[6];
			for (int i = 0; i < 6; i++)
				order[i] = i;
			return new HexData(order, list);
		}

		public HexData copy() {
			return new HexData(order.clone(), new ArrayList<>(list()));
		}

	}

}
