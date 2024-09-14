package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.capability.PlayerResearchCapability;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerResearch {

	private final Player player;
	private final PlayerResearchCapability data;

	private final Map<ResourceLocation, SpellResearch> cache = new LinkedHashMap<>();
	private Set<ResourceLocation> validSpells;

	public PlayerResearch(Player player, PlayerResearchCapability data) {
		this.player = player;
		this.data = data;
	}

	public Player player() {
		return player;
	}

	@Nullable
	public SpellResearch get(ResourceLocation id) {
		var reg = player.level().registryAccess().registryOrThrow(GTRegistries.SPELL);
		if (validSpells == null) {
			validSpells = reg.holders()
					.filter(e -> e.value().graph() != null).map(e -> e.key().location())
					.collect(Collectors.toCollection(LinkedHashSet::new));
		}
		if (!validSpells.contains(id))
			return null;
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		var spell = reg.get(id);
		if (spell == null) return null;
		var graph = spell.graph();
		if (graph == null) return null;
		ResearchData dat = data.get(id);
		if (dat==null)dat = ResearchData.create();
		SpellResearch ans = new SpellResearch(this, id, dat, HexGraph.create(id, graph.map(), graph.flows()));
		cache.put(id, ans);
		return ans;
	}

	protected void save(ResourceLocation id, ResearchData dat) {
		data.put(id, dat);
		if (!player.level().isClientSide()){
			data.sync();
		}
	}

}
