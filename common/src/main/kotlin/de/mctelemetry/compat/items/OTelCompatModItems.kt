package de.mctelemetry.compat.items

import de.mctelemetry.compat.OTelCompatMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.Supplier

object OTelCompatModItems {
    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(OTelCompatMod.MOD_ID, Registries.ITEM)

    fun init() {
        ITEMS.register()
    }

    private fun registerItem(name: String, item: Supplier<Item>): RegistrySupplier<Item> {
        return ITEMS.register(ResourceLocation.fromNamespaceAndPath(OTelCompatMod.MOD_ID, name), item);
    }
}
