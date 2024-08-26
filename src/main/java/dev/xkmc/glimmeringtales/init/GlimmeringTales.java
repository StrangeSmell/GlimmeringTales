package dev.xkmc.glimmeringtales.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.glimmeringtales.content.block.altar.BaseRitualBlockEntity;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipRegistry;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.data.*;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2backpack.content.common.BaseBagItemHandler;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GlimmeringTales.MODID)
@EventBusSubscriber(modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GlimmeringTales {

	public static final String MODID = "glimmeringtales";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public GlimmeringTales() {
		GTRegistries.register();
		GTItems.register();
		GTRecipes.register();
		GTEngine.register();
		Handlers.registerReg(NatureSpell.class, GTRegistries.SPELL);
		Handlers.registerReg(SpellAction.class, EngineRegistry.SPELL);
		Handlers.enableVanilla(Wrappers.cast(ProcessorType.class), EngineRegistry.PROCESSOR.registry().get());
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SpellTooltipRegistry.init();
		});
	}

	@SubscribeEvent
	public static void onAttribute(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, GTRegistries.MAX_MANA);
		event.add(EntityType.PLAYER, GTRegistries.MANA_REGEN);
		GTRegistries.ELEMENT.reg().holders().forEach(e -> event.add(EntityType.PLAYER, e.value().getAffinity()));
	}

	@SubscribeEvent
	public static void onDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(GTRegistries.SPELL, NatureSpell.CODEC, NatureSpell.CODEC);
	}

	@SubscribeEvent
	public static void registerCap(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, c) -> new BaseBagItemHandler(stack), GTItems.WAND);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GTItems.ALTAR_BE.get(), BaseRitualBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GTItems.MATRIX_BE.get(), BaseRitualBlockEntity::getItemHandler);
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void gatherDataInit(GatherDataEvent event){
		SpellTooltipRegistry.init();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, GTRecipeGen::onRecipeGen);
		REGISTRATE.addDataGenerator(ProviderType.LANG, GTLang::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GTDataMapGen::genMap);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, GTTagGen::genItemTag);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, GTTagGen::genBlockTag);
		var init = REGISTRATE.getDataGenInitializer();
		REGISTRATE.addDataGenerator(ProviderType.LANG, GTSpells::addLang);
		new GTDamageTypeGen(REGISTRATE).generate();
		init.add(EngineRegistry.PROJECTILE, GTSpells::genProjectiles);
		init.add(EngineRegistry.SPELL, GTSpells::genSpells);
		init.add(GTRegistries.SPELL, GTSpells::genNature);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GTSpells::genMap);
		init.addDependency(ProviderType.DATA_MAP, ProviderType.DYNAMIC);
		REGISTRATE.addDataGenerator(GTTagGen.STRUCTURE, GTTagGen::genStructureTag);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
