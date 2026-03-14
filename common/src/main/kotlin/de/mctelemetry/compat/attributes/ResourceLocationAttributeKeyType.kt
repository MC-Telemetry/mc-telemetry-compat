package de.mctelemetry.compat.attributes

import com.mojang.serialization.Codec
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.GenericAttributeType
import de.mctelemetry.core.api.attributes.IAttributeKeyTypeInstance
import de.mctelemetry.core.api.attributes.IAttributeKeyTypeTemplate
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object ResourceLocationAttributeKeyType : IAttributeKeyTypeInstance.InstanceType<ResourceLocation, String, ResourceLocationAttributeKeyType> {

    override val id: ResourceKey<IAttributeKeyTypeTemplate<*, *, *>>
        get() = ResourceKey.create(
            OTelCoreModAPI.AttributeTypeMappings,
            ResourceLocation.fromNamespaceAndPath(OTelCompatMod.MOD_ID, "resource_location")
        )

    override fun format(value: ResourceLocation): String {
        return value.toString()
    }

    override val baseType: GenericAttributeType<String>
        get() = GenericAttributeType.STRING
    override val valueCodec: Codec<ResourceLocation>
        get() = ResourceLocation.CODEC
    override val valueStreamCodec: StreamCodec<in ByteBuf, ResourceLocation>
        get() = ResourceLocation.STREAM_CODEC
    override val valueType: Class<ResourceLocation>
        get() = ResourceLocation::class.java
}
