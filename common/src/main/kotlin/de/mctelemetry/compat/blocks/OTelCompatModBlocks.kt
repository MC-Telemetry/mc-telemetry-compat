package de.mctelemetry.compat.blocks

import de.mctelemetry.compat.OTelCompatMod
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

object OTelCompatModBlocks {
    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(OTelCompatMod.MOD_ID, Registries.BLOCK)

    fun init() {
        BLOCKS.register()
    }

    private fun <T:Block> registerBlock(name: String, block: Supplier<T>): RegistrySupplier<T> {
        return BLOCKS.register(ResourceLocation.fromNamespaceAndPath(OTelCompatMod.MOD_ID, name), block)
    }
}
