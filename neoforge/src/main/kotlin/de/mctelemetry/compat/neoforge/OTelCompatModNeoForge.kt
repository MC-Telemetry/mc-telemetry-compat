package de.mctelemetry.compat.neoforge

import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.draconicevolution.DraconicEvolutionModRequired
import de.mctelemetry.compat.draconicevolution.OTelCompatDraconicEvolutionContent
import dev.architectury.platform.Platform
import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

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
        if (Platform.isModLoaded("draconicevolution")) {
            @OptIn(DraconicEvolutionModRequired::class)
            OTelCompatDraconicEvolutionContent.register(MOD_BUS)
        }
    }

    init {
        init()
    }
}
