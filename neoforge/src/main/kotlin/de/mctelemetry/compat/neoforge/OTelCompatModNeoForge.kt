package de.mctelemetry.compat.neoforge

import de.mctelemetry.compat.OTelCompatMod
import net.neoforged.fml.common.Mod

@Suppress("unused")
@Mod(OTelCompatMod.MOD_ID)
object OTelCompatModNeoForge {

    private fun registerCallbacks() {

    }

    private fun init() {
        registerCallbacks()
        OTelCompatMod.init()
        registerContent()
    }

    private fun registerContent() {

    }

    init {
        init()
    }
}
