package de.mctelemetry.compat.appliedenergistics2.observations.network.storage

import appeng.api.networking.IGridNode
import appeng.api.stacks.AEKeyType
import appeng.api.stacks.AEKeyTypes
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.observations.network.GridObservationSourceBase
import de.mctelemetry.compat.attributes.ResourceLocationAttributeKeyType
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationRecorder
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.api.observations.IParameterizedObservationSource
import de.mctelemetry.core.api.observations.IParameterizedObservationSource.Parameter.Companion.get
import de.mctelemetry.core.observations.model.ObservationAttributeMapping
import de.mctelemetry.core.utils.EmptyAutoCloseable
import de.mctelemetry.core.utils.withValue
import de.mctelemetry.core.utils.withoutValue
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.collections.component1
import kotlin.collections.component2

@AppliedEnergistics2ModRequired
object GridCurrentFilterStorageObservationSource :
    GridObservationSourceBase<GridCurrentFilterStorageObservationSource.Instance>(),
    IParameterizedObservationSource<IGridNode, GridCurrentFilterStorageObservationSource.Instance>  {

    override val id: ResourceKey<IObservationSource<*, *>> = ResourceKey.create(
        OTelCoreModAPI.ObservationSources,
        ResourceLocation.fromNamespaceAndPath(OTelCompatMod.MOD_ID, "applied_energistics2_connector.grid.storage.amount.filter")
    )

    val typeParameter: IParameterizedObservationSource.Parameter<ResourceLocation> = IParameterizedObservationSource.Parameter(
        name = "type",
        argumentType = ResourceLocationArgument.id(),
    ) { type, _ -> require(AEKeyTypes.getAll().any { it.id == type }) { "Type must be any of [${AEKeyTypes.getAll().joinToString(", ")}]" } }

    val observedKey = ResourceLocationAttributeKeyType.createObservationAttributeReference("key")

    override val parameters: Map<String, IParameterizedObservationSource.Parameter<*>> =
        listOf(typeParameter).associateBy { it.name }

    context(parameters: IParameterizedObservationSource.ParameterMap)
    override fun instanceFromParameters(): Instance {
        return Instance(AEKeyTypes.getAll().first { it.id == typeParameter.get() })
    }

    override val streamCodec: StreamCodec<RegistryFriendlyByteBuf, Instance> =
        AEKeyType.STREAM_CODEC.map(
            { Instance(it) },
            {
                it.type
            },
        )

    override val codec: Codec<Instance> = AEKeyType.CODEC.comapFlatMap({
        DataResult.success(Instance(it))
    }, {
        it.type
    })

    class Instance(val type: AEKeyType) : GridInstanceBase<EmptyAutoCloseable, Instance>(GridCurrentFilterStorageObservationSource) {

        context(sourceOwner: IGridNode, mapping: ObservationAttributeMapping)
        override fun createObservationContext(): EmptyAutoCloseable {
            return EmptyAutoCloseable
        }

        context(sourceOwner: IGridNode, observationContext: EmptyAutoCloseable, attributeStore: IAttributeValueStore.Mutable)
        override fun observe(
            recorder: IObservationRecorder.Unresolved,
            unusedAttributes: Set<AttributeDataSource<*>>
        ) {
            val stacks = sourceOwner.grid.storageService.inventory.availableStacks.filter { it.key.type == type }

            if (observedKey in unusedAttributes) {
                observedKey.withoutValue {
                    recorder.observe(stacks.sumOf { it.longValue }, this)
                }
                return
            }

            for ((key, count) in stacks) {
                observedKey.withValue(key.id) {
                    recorder.observe(count, this)
                }
            }
        }
    }
}
