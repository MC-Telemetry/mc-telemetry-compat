package de.mctelemetry.compat

import de.mctelemetry.compat.blocks.OTelCompatModBlocks
import de.mctelemetry.compat.instruments.builtin.BuiltinInstruments
import de.mctelemetry.compat.items.OTelCompatModItems
import de.mctelemetry.compat.platform.ModPlatform
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object OTelCompatMod {

    const val MOD_ID = "mcotelcompat"

    val logger: Logger = LogManager.getLogger(MOD_ID)

    fun registerCallbacks() {
        BuiltinInstruments.register()
    }

    fun registerContent() {
        OTelCompatModBlocks.init()
        OTelCompatModItems.init()
    }

    fun init() {
        logger.info("Hello from {}", ModPlatform.getPlatformName())
        registerCallbacks()
        registerContent()
    }
}
