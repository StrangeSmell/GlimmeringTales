package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.compat.GTCurioCompat;
import dev.xkmc.glimmeringtales.content.item.curio.DamageTypeCurioItem;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;

public class GTAttackListener implements AttackListener {

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		var type = DamageTypeRoot.of(event.getOriginal());
		if (type == null) return;
		var attacker = event.getAttacker();
		var level = attacker.level();
		if (type.getHolder(level).is(GTDamageTypeGen.SPELL)) {
			var list = GTCurioCompat.getAll(attacker, DamageTypeCurioItem.class);
			for (var e : list) {
				DamageState state = e.getState();
				if (type.validState(state)) {
					event.enable(state);
				}
			}
		}
	}

}
