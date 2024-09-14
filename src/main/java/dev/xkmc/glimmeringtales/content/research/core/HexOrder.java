package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.logic.*;

import javax.annotation.Nullable;
import java.util.ArrayList;

public record HexOrder(int[] order, ArrayList<SpellElement> list) {

	public static final int[][] DEF = {
			{0, 1, 2, 3, 4, 5},
			{0, 2, 4, 1, 3, 5},
			{0, 3, 1, 4, 2, 5},
			{0, 1, 4, 2, 3, 5},
			{0, 1, 2, 3, 4, 5},
			{0, 1, 2, 3, 4, 5},
	};

	public static HexOrder create(int n) {
		return new HexOrder(DEF[n - 1].clone(), new ArrayList<>());
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
