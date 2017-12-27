package frc.team1018.steamworksKotlin.subsystems

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.VictorSP
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Loop
import frc.team1018.steamworksKotlin.loops.Looper

/**
 * @author Ryan Blue
 */
object GearRotator : Subsystem() {
    private val mGearRotatorMotor = VictorSP(Constants.kGearRotatorPwm).apply { inverted = true }

    private val mSlotDetector = DigitalInput(Constants.kGearRotatorBannerDio)

    enum class WantedState {
        AUTOMATIC,
        FLIP_OVERRIDE
    }

    enum class SystemState {
        FLIP_AUTOMATIC,
        OFF_AUTOMATIC,
        FLIP_OVERRIDE
    }

    private var mSystemState = SystemState.OFF_AUTOMATIC
    private var wantedState = WantedState.AUTOMATIC

    private val mLoop = object: Loop {
        override fun onStart(timestamp: Double) {
            synchronized(this@GearRotator) {
                mSystemState = SystemState.OFF_AUTOMATIC
                wantedState = WantedState.AUTOMATIC
            }
        }

        override fun onLoop(timestamp: Double) {
            synchronized(this@GearRotator) {
                when(mSystemState) {

                }
            }
        }

        override fun onStop(timestamp: Double) {
            stop()
        }
    }

    val containsGear
        get() = true

    val isGearAligned
        get() = containsGear && !mSlotDetector.get()

    fun startFlipping() {
        mGearRotatorMotor.set(1.0)
    }

    override fun outputToSmartDashboard() {
    }

    override fun stop() {
        mGearRotatorMotor.stopMotor()
    }

    override fun zeroSensors() {
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
        enabledLooper.register(mLoop)
    }
}
