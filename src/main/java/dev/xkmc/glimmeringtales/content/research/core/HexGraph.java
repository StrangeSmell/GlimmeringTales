package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public record HexGraph(SpellElement[] elements, boolean[][] graph) {

	@Nullable
	public SpellElement getElem(int index) {
		return index >= elements.length ? null : elements[index];
	}

	public boolean connected(int src, int dst) {
		return graph[src][dst];
	}

	public static HexGraph create(ResourceLocation id, Map<String, SpellElement> elements, List<String> flows) {
		int n = elements.size();
		SpellElement[] elems = new SpellElement[n];
		char[] chars = new char[n];
		int i = 0;
		for (Map.Entry<String, SpellElement> ent : elements.entrySet()) {
			elems[i] = ent.getValue();
			if (ent.getKey().length() != 1)
				LogManager.getLogger().error("key length not 1 in {}", id);
			chars[i] = ent.getKey().charAt(0);
			i++;
		}
		boolean[][] bools = new boolean[6][6];
		for (String flow : flows) {
			if (flow.contains("<->")) {
				String[] strs = flow.split("<->");
				if (strs.length != 2 || !flowRegex(chars, strs[0], strs[1], bools, true))
					LogManager.getLogger().error("illegal side expression{} in {}", flow, id);
			} else if (flow.contains("->")) {
				String[] strs = flow.split("->");
				if (strs.length != 2 || !flowRegex(chars, strs[0], strs[1], bools, false))
					LogManager.getLogger().error("illegal side expression {} in {}", flow, id);
			} else if (flow.endsWith("|")) {
				if (!flowRound(chars, flow.substring(0, flow.length() - 1), bools))
					LogManager.getLogger().error("illegal round expression {} in {}", flow, id);
			} else LogManager.getLogger().error("illegal connector {} in {}", flow, id);
		}
		return new HexGraph(elems, bools);
	}

	private static boolean flowRegex(char[] chars, String s0, String s1, boolean[][] bools, boolean bidirect) {
		int[] i0 = new int[s0.length()];
		int[] i1 = new int[s1.length()];
		for (int i = 0; i < s0.length(); i++) {
			i0[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == s0.charAt(i)) {
					i0[i] = c;
					break;
				}
			}
			if (i0[i] == -1)
				return false;
		}
		for (int i = 0; i < s1.length(); i++) {
			i1[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == s1.charAt(i)) {
					i1[i] = c;
					break;
				}
			}
			if (i1[i] == -1)
				return false;
		}
		for (int k : i0)
			for (int i : i1) {
				if (k == i)
					return false;
				if (bools[k][i])
					return false;
				bools[k][i] = true;
				if (bidirect) {
					if (bools[i][k])
						return false;
					bools[i][k] = true;
				}
			}
		return true;
	}

	private static boolean flowRound(char[] chars, String str, boolean[][] bools) {
		int[] i0 = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			i0[i] = -1;
			for (int c = 0; c < chars.length; c++) {
				if (chars[c] == str.charAt(i)) {
					i0[i] = c;
					break;
				}
			}
			if (i0[i] == -1)
				return false;
		}
		for (int i : i0)
			for (int j : i0) {
				if (i == j)
					continue;
				bools[i][j] = true;
			}
		return true;
	}

}
