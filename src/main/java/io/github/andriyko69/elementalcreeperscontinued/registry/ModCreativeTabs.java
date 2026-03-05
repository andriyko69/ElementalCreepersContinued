package io.github.andriyko69.elementalcreeperscontinued.registry;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities.EGG_ITEMS;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ElementalCreepersContinued.MOD_ID);

    public static final Supplier<CreativeModeTab> ELEMENTAL_CREEPERS_TAB = CREATIVE_TABS.register("elemental_creepers_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.elementalcreeperscontinued.elemental_creeper_egg_tab"))
                    .icon(() -> EGG_ITEMS.get(3).get().getDefaultInstance()).displayItems((parameters, output) -> {
                        for (DeferredItem<? extends Item> egg : EGG_ITEMS) {
                            output.accept(egg.get());
                        }
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
