package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SpellResearch {

	public static final int LOCKED = -2, UNLOCKED = -1;

	private final PlayerResearch player;
	private final ResourceLocation id;
	private final ResearchData data;
	private final HexGraph graph;

	public SpellResearch(PlayerResearch player, ResourceLocation id, ResearchData data, HexGraph graph) {
		this.player = player;
		this.id = id;
		this.data = data;
		this.graph = graph;
	}

	public final boolean unlocked() {
		return data.cost() > LOCKED;
	}

	public final int getCost() {
		return data.cost();
	}

	public final void setUnlock() {
		if (!unlocked()) {
			data.setCost(UNLOCKED);
		}
	}

	public void updateBestSolution(HexHandler hex, ResearchData.HexData data, int cost) {
		this.data.update(hex, data, cost);
	}

	public HexHandler getSolution() {
		return data.hex();
	}

	public final boolean usable() {
		return data.cost() > UNLOCKED;
	}

	public ResearchState getState() {
		return switch (data.cost()) {
			case LOCKED -> ResearchState.LOCKED;
			case UNLOCKED -> ResearchState.UNLOCKED;
			default -> ResearchState.COMPLETED;
		};
	}

	public boolean visible() {
		return true;
	}

	public ResearchData.HexData getMiscData() {
		return data.shape().copy();
	}

	public boolean matchList(List<SpellElement> elem) {
		return elem.equals(data.shape().list());
	}

	public HexGraph getGraph() {
		return graph;
	}

	public void save() {
		player.save(id, data);
	}

	public List<FormattedText> getFullDesc() {
		List<FormattedText> list = new ArrayList<>();
		list.add(getState().getDesc());
		if (usable()) {
			list.add(GTLang.HEX_COST.get(getCost()));
		}
		return list;
	}

}
