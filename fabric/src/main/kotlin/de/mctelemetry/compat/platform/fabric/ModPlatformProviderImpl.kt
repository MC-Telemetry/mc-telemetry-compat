package de.mctelemetry.compat.platform.fabric

import de.mctelemetry.compat.platform.ModPlatform

@Suppress("unused")
object ModPlatformProviderImpl {
    @JvmStatic
    fun getPlatform(): ModPlatform {
        return FabricModPlatform
    }

    object FabricModPlatform : ModPlatform {
        override fun getPlatformName(): String = "Fabric"
    }
}
