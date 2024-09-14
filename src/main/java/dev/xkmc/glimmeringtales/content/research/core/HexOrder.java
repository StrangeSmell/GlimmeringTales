package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.logic.*;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;

public record HexOrder(int[] order, ArrayList<SpellElement> list) {

	public static HexOrder create() {
		ArrayList<SpellElement> list = new ArrayList<>();
		for (var e : GTRegistries.ELEMENT.reg()) {
			list.add(e);
		}
		int[] order = new int[6];
		for (int i = 0; i < 6; i++)
			order[i] = i;
		return new HexOrder(order, list);
	}

	public HexOrder copy() {
		return new HexOrder(order.clone(), new ArrayList<>(list()));
	}

	@Nullable
	public SpellElement getElem(HexGraph graph, int i) {
		return graph.getElem(order[i]);
	}

	public boolean connected(HexGraph graph, int i, int j) {
		return graph.connected(order[i], order[j]);
	}

	public boolean check(HexHandler handler, FlowChart flow, HexGraph graph, boolean[] wrong_flow, boolean[] ignore) {
		boolean wrong = false;
		HexCell cell = new HexCell(handler, 0, 0);
		for (int i = 0; i < 6; i++) {
			wrong_flow[i] = false;
			cell.toCorner(HexDirection.values()[i]);
			if (cell.exists() == (getElem(graph, i) == null)) {
				wrong_flow[i] = true;
				wrong = true;
			}
		}
		if (wrong) return false;
		for (int i = 0; i < 6; i++) {
			Frac[] arr = flow.matrix[i];
			int rec = 0;
			for (int j = 0; j < 6; j++)
				if (connected(graph, i, j))
					rec++;
			ignore[i] = rec == 0;
			if (rec == 0) {
				continue;
			}
			Frac b = new Frac(1, rec);
			for (int j = 0; j < 6; j++) {
				Frac f = arr[j];
				if (!connected(graph, i, j)) {
					if (f != null) {
						wrong |= wrong_flow[i] = true;
						break;
					}
					continue;
				}
				if (f == null) {
					wrong |= wrong_flow[i] = true;
					break;
				}
				if (!f.equals(b)) {
					wrong |= wrong_flow[i] = true;
				}
			}
		}
		return !wrong;
	}

}
