package de.mctelemetry.compat.instruments.builtin

import de.mctelemetry.compat.instruments.builtin.game.BuiltinGameInstruments
import de.mctelemetry.compat.instruments.builtin.world.BuiltinWorldInstruments

object BuiltinInstruments {

    fun register() {
        BuiltinGameInstruments.register()
        BuiltinWorldInstruments.register()
    }
}
