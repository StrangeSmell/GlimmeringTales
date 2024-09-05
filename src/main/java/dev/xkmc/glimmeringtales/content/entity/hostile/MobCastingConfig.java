package dev.xkmc.glimmeringtales.content.entity.hostile;

public interface MobCastingConfig {

	MobCastingConfig DEF = new Def(0.75, 0.5, 5, 1,
			20, 0.3f, 0.3f);

	int stopMovingSeeTime();

	double speed();

	int strafRotateTime();

	float switchRotationChance();

	float switchDirectionChance();

	double stopBackoffRange();

	double startBackoffRange();

	record Def(
			double stopBackoffRange, double startBackoffRange,
			int stopMovingSeeTime, double speed,
			int strafRotateTime, float switchRotationChance, float switchDirectionChance
	) implements MobCastingConfig {

	}

}
