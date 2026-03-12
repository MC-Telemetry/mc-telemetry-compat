package de.mctelemetry.compat.appliedenergistics2

import appeng.api.AECapabilities
import appeng.api.networking.IInWorldGridNodeHost
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergistics2.blocks.AE2ConnectorBlock
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ConnectorBlockEntity
import de.mctelemetry.compat.appliedenergistics2.observations.network.energy.GridCurrentEnergyObservationSource
import de.mctelemetry.compat.appliedenergistics2.observations.network.energy.GridMaxEnergyObservationSource
import de.mctelemetry.compat.neoforge.`arch$tab`
import de.mctelemetry.core.OTelCoreMod
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.component.OTelCoreModComponents
import de.mctelemetry.core.utils.consumeAllRethrow
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.LinkedList

@AppliedEnergistics2ModRequired
object OTelCompatAppliedEnergistics2Content {


    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Registries.BLOCK, OTelCompatMod.MOD_ID)

    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Registries.ITEM, OTelCompatMod.MOD_ID)


    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, OTelCompatMod.MOD_ID)

    val OBSERVATION_SOURCES: DeferredRegister<IObservationSource<*, *>> = DeferredRegister.create(
        OTelCoreModAPI.ObservationSources,
        OTelCompatMod.MOD_ID
    )

    val AE2_CONNECTOR_BLOCK = BLOCKS.register("applied_energistics2_connector") { ->
        AE2ConnectorBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5f))
    }

    val AE2_CONNECTOR_ITEM = ITEMS.register("applied_energistics2_connector") { ->
        BlockItem(
            AE2_CONNECTOR_BLOCK.get(),
            Item.Properties()
                .`arch$tab`(OTelCoreMod.OTEL_TAB)
                .component(OTelCoreModComponents.GENERATE_SINGLETON_STATES.get(), true)
        )
    }

    val AE2_CONNECTOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("applied_energistics2_connector") { ->
        BlockEntityType(
            ::AE2ConnectorBlockEntity,
            setOf(AE2_CONNECTOR_BLOCK.get()),
            null
        )
    }

    val AE2_NETWORK_ENERGY_CURRENT_OBSERVATIONSOURCE = registerObservationSource(
        GridCurrentEnergyObservationSource
    )

    val AE2_NETWORK_ENERGY_MAX_OBSERVATIONSOURCE = registerObservationSource(
        GridMaxEnergyObservationSource
    )

    private fun <T : IObservationSource<*, *>> registerObservationSource(source: T): DeferredHolder<IObservationSource<*, *>, T> {
        return OBSERVATION_SOURCES.register(source.id.location().path) { -> source }
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
        BLOCK_ENTITIES.register(bus)
        ITEMS.register(bus)
        OBSERVATION_SOURCES.register(bus)
        bus.addListener(this::registerCapabilities)
        NeoForge.EVENT_BUS.addListener(TickEndListenerExecutor::onTickEnd)
    }

    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            AECapabilities.IN_WORLD_GRID_NODE_HOST,
            AE2_CONNECTOR_BLOCK_ENTITY.get()
        ) { obj, _ -> obj as IInWorldGridNodeHost }
    }

    internal fun addEndOfTickListener(block: () -> Unit) {
        TickEndListenerExecutor.queue.add(block)
    }

    private object TickEndListenerExecutor {
        val queue: MutableList<() -> Unit> = LinkedList()

        fun onTickEnd(event: ServerTickEvent.Post) {
            queue.consumeAllRethrow { it.invoke() }
        }
    }
}
