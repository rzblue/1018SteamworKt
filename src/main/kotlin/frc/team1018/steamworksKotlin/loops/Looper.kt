package frc.team1018.steamworksKotlin.loops

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team1018.steamworksKotlin.Constants

class Looper {
    private val kPeriod = Constants.kLooperDt

    private var running = false

    private val notifier: Notifier
    private val loops = ArrayList<Loop>()
    private val taskRunningLock = Any()

    private var timestamp = 0.0
    private var dt = 0.0

    private val runnable = Runnable {
        synchronized(taskRunningLock) {
            if(running) {
                val now = Timer.getFPGATimestamp()

                loops.forEach { loop -> loop.onLoop(now) }

                dt = now - timestamp
                timestamp = now

            }
        }
    }

    init {
        notifier = Notifier(runnable)
    }

    @Synchronized fun register(loop: Loop) {
        synchronized(taskRunningLock) {
            loops.add(loop)
        }
    }

    @Synchronized fun start() {
        if(!running) {
            println("Starting loops")
            synchronized(taskRunningLock) {
                timestamp = Timer.getFPGATimestamp()
                loops.forEach { loop -> loop.onStart(timestamp) }
                running = true
            }
            notifier.startPeriodic(kPeriod)
        }
    }

    @Synchronized fun stop() {
        if (running) {
            println("Stopping loops")
            notifier.stop()
            synchronized(taskRunningLock) {
                running = false
                timestamp = Timer.getFPGATimestamp()
                loops.forEach { loop -> loop.onStop(timestamp) }
            }
        }
    }

    fun outputToSmartDashboard() {

    }


}