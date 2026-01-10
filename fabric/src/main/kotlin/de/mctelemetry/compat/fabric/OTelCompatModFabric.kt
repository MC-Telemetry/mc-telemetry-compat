package de.mctelemetry.compat.fabric

import de.mctelemetry.compat.OTelCompatMod
import net.fabricmc.api.ModInitializer

object OTelCompatModFabric : ModInitializer {

    private fun registerCallbacks() {

    }

    private fun registerContent() {

    }

    override fun onInitialize() {
        registerCallbacks()
        OTelCompatMod.init()
        registerContent()
    }
}
