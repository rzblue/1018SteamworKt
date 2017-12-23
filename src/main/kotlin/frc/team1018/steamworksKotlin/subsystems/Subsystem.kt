package frc.team1018.steamworksKotlin.subsystems

import frc.team1018.steamworksKotlin.loops.Looper

abstract class Subsystem {
    fun writeToLog() {
    }

    abstract fun outputToSmartDashboard()

    abstract fun stop()

    abstract fun zeroSensors()

    abstract fun registerEnabledLoops(enabledLooper: Looper)
}