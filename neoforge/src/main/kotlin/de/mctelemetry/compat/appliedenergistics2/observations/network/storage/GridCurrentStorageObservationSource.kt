package de.mctelemetry.compat.appliedenergistics2.observations.network.storage

import appeng.api.networking.IGridNode
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.observations.network.GridObservationSourceBase
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.attributes.NativeAttributeKeyTypes
import de.mctelemetry.core.api.observations.IObservationRecorder
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.utils.EmptyAutoCloseable
import de.mctelemetry.core.utils.withValue
import de.mctelemetry.core.utils.withoutValue
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.collections.component1
import kotlin.collections.component2

@AppliedEnergistics2ModRequired
object GridCurrentStorageObservationSource :
    GridObservationSourceBase.GridSingletonBase.Simple<GridCurrentStorageObservationSource>() {

    val observedKey = NativeAttributeKeyTypes.StringType.createObservationAttributeReference("key")

    override val id: ResourceKey<IObservationSource<*, *>> = ResourceKey.create(
        OTelCoreModAPI.ObservationSources, ResourceLocation.fromNamespaceAndPath(
            OTelCompatMod.MOD_ID, "applied_energistics2_connector.grid.storage.amount"
        )
    )

    context(sourceOwner: IGridNode, observationContext: EmptyAutoCloseable, attributeStore: IAttributeValueStore.Mutable)
    override fun observe(
        recorder: IObservationRecorder.Unresolved,
        unusedAttributes: Set<AttributeDataSource<*>>
    ) {
        val stacks = sourceOwner.grid.storageService.inventory.availableStacks

        if (observedKey in unusedAttributes) {
            observedKey.withoutValue {
                recorder.observe(stacks.sumOf { it.longValue }, this)
            }
            return
        }

        for ((key, count) in stacks) {
            observedKey.withValue("${key.type}@${key.id}") {
                recorder.observe(count, this)
            }
        }
    }
}
