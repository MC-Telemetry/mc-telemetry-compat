package de.mctelemetry.compat.platform

import dev.architectury.injectables.annotations.ExpectPlatform

object ModPlatformProvider {
    @JvmStatic
    @ExpectPlatform
    fun getPlatform(): ModPlatform {
        throw AssertionError()
    }
}
