package dev.xkmc.glimmeringtales.content.item.materials;

import dev.xkmc.glimmeringtales.content.core.searcher.BlockSearcher;
import net.minecraft.resources.ResourceLocation;

public interface IBlockSearcher {

	BlockSearcher getSearcher();

	ResourceLocation id();

}
