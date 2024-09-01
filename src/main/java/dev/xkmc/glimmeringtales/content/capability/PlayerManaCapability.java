package dev.xkmc.glimmeringtales.content.capability;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class PlayerManaCapability extends PlayerCapabilityTemplate<PlayerManaCapability> {

	@SerialField
	private double mana = GTRegistries.MAX_MANA.get().getDefaultValue();

	@SerialField
	private double focus = GTRegistries.MAX_FOCUS.get().getDefaultValue();

	@Override
	public void onClone(Player player, boolean isWasDeath) {
		if (isWasDeath) {
			mana = player.getAttributeValue(GTRegistries.MAX_MANA);
			focus = GTRegistries.MAX_FOCUS.get().getDefaultValue();
		}
	}

	@Override
	public void tick(Player player) {
		if (player instanceof ServerPlayer sp && sp.tickCount % 20 == 0) {
			double max = sp.getAttributeValue(GTRegistries.MAX_MANA);
			double regen = sp.getAttributeValue(GTRegistries.MANA_REGEN);
			if (mana >= max) mana = max;
			else {
				if (!(mana >= 0)) mana = 0;
				mana = Math.min(max, mana + regen);
				GTRegistries.MANA.type().network.toClient(sp);
			}
		}
		double maxFocus = player.getAttributeValue(GTRegistries.MAX_FOCUS);
		if (focus > maxFocus) focus = maxFocus;
		if (!(focus >= 0)) focus = 0;
		if (focus < maxFocus) {
			focus = Math.min(maxFocus, focus + 1);
			if (focus == maxFocus && player instanceof ServerPlayer sp) {
				GTRegistries.MANA.type().network.toClient(sp);
			}
		}
	}

	public double getMana() {
		return mana;
	}

	public boolean consume(Player player, double focus, double mana) {
		if (focus > this.focus || mana > this.mana) {
			return false;
		}
		this.focus -= focus;
		this.mana -= mana;
		if (player instanceof ServerPlayer sp) {
			GTRegistries.MANA.type().network.toClient(sp);
		}
		return true;
	}

	public double getFocus() {
		return focus;
	}
}
