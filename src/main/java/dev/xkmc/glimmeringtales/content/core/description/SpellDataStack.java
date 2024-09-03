package dev.xkmc.glimmeringtales.content.core.description;

import dev.xkmc.l2magic.content.engine.extension.ExtensionHolder;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2serial.util.Wrappers;

import java.util.Map;
import java.util.Queue;

public record SpellDataStack(Map<ExtensionHolder<?>, Queue<IExtended<?>>> map) {

	public <T extends IExtended<T>> T get(ExtensionHolder<T> type) {
		var ans = map.get(type);
		return Wrappers.cast(ans.poll());
	}


}
