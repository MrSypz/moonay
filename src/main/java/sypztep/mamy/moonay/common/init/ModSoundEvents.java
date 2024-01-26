package sypztep.mamy.moonay.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import sypztep.mamy.moonay.common.MoonayMod;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ModSoundEvents {
	Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();
	//Sound
	SoundEvent ITEM_CARVE = createSoundEvent("item.carve");
	SoundEvent ITEM_STIGMA = createSoundEvent("item.stigma");
	SoundEvent ITEM_STALWART = createSoundEvent("item.stalwart");

	static void init() {
		SOUND_EVENTS.keySet().forEach((soundEvent) -> {
			Registry.register(Registries.SOUND_EVENT, SOUND_EVENTS.get(soundEvent), soundEvent);
		});
	}
	private static SoundEvent createSoundEvent(String path) {
		SoundEvent soundEvent = SoundEvent.of(MoonayMod.id(path));
		SOUND_EVENTS.put(soundEvent, MoonayMod.id(path));
		return soundEvent;
	}
}
