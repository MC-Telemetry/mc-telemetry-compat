package de.mctelemetry.compat.platform

interface ModPlatform {

    fun getPlatformName(): String

    companion object : ModPlatform by ModPlatformProvider.getPlatform()
}
