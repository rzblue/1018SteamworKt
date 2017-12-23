package frc.team1018.steamworksKotlin

import frc.team1018.steamworksKotlin.loops.Looper
import frc.team1018.steamworksKotlin.subsystems.Subsystem

class SubsystemManager(private val allSubsystems: List<Subsystem>) {

    fun outputToSmartDashboard() {
        allSubsystems.forEach { s -> s.outputToSmartDashboard() }
    }

    fun writeToLog() {
        allSubsystems.forEach { s -> s.writeToLog() }
    }

    fun stop() {
        allSubsystems.forEach { s -> s.stop() }
    }

    fun zeroSensors() {
        allSubsystems.forEach { s -> s.zeroSensors() }
    }

    fun registerEnabledLoops(enabledLooper: Looper) {
        allSubsystems.forEach { s -> s.registerEnabledLoops(enabledLooper) }
    }
}