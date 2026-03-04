package de.mctelemetry.compat.appliedenergisitics2

import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergisitics2.blocks.AppliedEnergistics2ScraperBlock
import de.mctelemetry.compat.appliedenergisitics2.observations.opstorage.AppliedEnergistics2AmountObservationSource
import de.mctelemetry.compat.neoforge.`arch$tab`
import de.mctelemetry.core.OTelCoreMod
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.component.OTelCoreModComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@AppliedEnergistics2ModRequired
object OTelCompatAppliedEnergistics2Content {

    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Registries.BLOCK, OTelCompatMod.MOD_ID)

    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Registries.ITEM, OTelCompatMod.MOD_ID)

    val OBSERVATION_SOURCES: DeferredRegister<IObservationSource<*, *>> = DeferredRegister.create(
        OTelCoreModAPI.ObservationSources,
        OTelCompatMod.MOD_ID
    )

    val APPLIED_ENERGISTICS2_SCRAPER_BLOCK = BLOCKS.register("applied_energistics2_scraper") { ->
        AppliedEnergistics2ScraperBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5f))
    }

    val APPLIED_ENERGISTICS2_SCRAPER_ITEM = ITEMS.register("applied_energistics2_scraper") { ->
        BlockItem(
            APPLIED_ENERGISTICS2_SCRAPER_BLOCK.get(),
            Item.Properties()
                .`arch$tab`(OTelCoreMod.OTEL_TAB)
                .component(OTelCoreModComponents.GENERATE_SINGLETON_STATES.get(), true)
        )
    }

    val APPLIED_ENERGISTICS2_AMOUNT_OBSERVATIONSOURCE = registerObservationSource(
        AppliedEnergistics2AmountObservationSource
    )

    private fun <T : IObservationSource<*, *>> registerObservationSource(source: T): DeferredHolder<IObservationSource<*, *>, T> {
        return OBSERVATION_SOURCES.register(source.id.location().path) { -> source }
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
        ITEMS.register(bus)
        OBSERVATION_SOURCES.register(bus)
    }
}
