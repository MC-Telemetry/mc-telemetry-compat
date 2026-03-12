package de.mctelemetry.compat.appliedenergistics2.observations.network.energy

import appeng.api.networking.IGridNode
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.observations.network.GridObservationSourceBase
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationRecorder
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.utils.observe
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

@AppliedEnergistics2ModRequired
object GridCurrentEnergyObservationSource :
    GridObservationSourceBase.GridSingletonBase<GridCurrentEnergyObservationSource>() {

    override val id: ResourceKey<IObservationSource<*, *>> = ResourceKey.create(
        OTelCoreModAPI.ObservationSources, ResourceLocation.fromNamespaceAndPath(
            OTelCompatMod.MOD_ID, "applied_energistics2_connector.grid.energy.amount"
        )
    )

    context(sourceContext: IGridNode, attributeStore: IAttributeValueStore.MapAttributeStore)
    override fun observe(
        recorder: IObservationRecorder.Unresolved,
        unusedAttributes: Set<AttributeDataSource<*>>
    ) {
        val energyService = sourceContext.grid.energyService
        recorder.observe(energyService.storedPower)
    }
}
