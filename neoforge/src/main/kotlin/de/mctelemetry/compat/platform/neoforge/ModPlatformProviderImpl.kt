package de.mctelemetry.compat.platform.neoforge

import de.mctelemetry.compat.platform.ModPlatform

@Suppress("unused")
object ModPlatformProviderImpl {
    @JvmStatic
    fun getPlatform(): ModPlatform {
        return NeoForgeModPlatform
    }

    object NeoForgeModPlatform : ModPlatform {
        override fun getPlatformName(): String = "NeoForge"
    }
}
