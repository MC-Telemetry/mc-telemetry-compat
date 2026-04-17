package de.mctelemetry.compat.attributes

import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.IAttributeKeyTypeTemplate
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

object OTelCompatAttributeKeyTypes {
    val ATTRIBUTE_KEY_TYPES: DeferredRegister<IAttributeKeyTypeTemplate<*, *, *>> = DeferredRegister.create(
        OTelCompatMod.MOD_ID,
        OTelCoreModAPI.AttributeTypeMappings
    )

    fun init() {
        ATTRIBUTE_KEY_TYPES.register()
    }

    private fun <T : IAttributeKeyTypeTemplate<*, *, *>> registerAttributeKeyType(name: String, value: ()->T): RegistrySupplier<T> {
        return ATTRIBUTE_KEY_TYPES.register(ResourceLocation.fromNamespaceAndPath(OTelCompatMod.MOD_ID, name), Supplier<T> { value() })
    }
}
