package frc.team1018.steamworksKotlin.subsystems

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.VictorSP
import frc.team1018.lib.util.drivers.LidarLiteV3
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Loop
import frc.team1018.steamworksKotlin.loops.Looper
import kotlin.math.roundToInt

/**
 * @author Ryan Blue
 */
object GearRotator : Subsystem() {
    private val kFlipDuration = 0.75

    private val mGearRotatorMotor = VictorSP(Constants.kGearRotatorPwm).apply { inverted = true }

    private val mSlotDetector = DigitalInput(Constants.kGearRotatorBannerDio)

    private val mLidar = LidarLiteV3(I2C.Port.kMXP)

    enum class WantedState {
        AUTOMATIC,
        FLIP_OVERRIDE
    }

    enum class SystemState {
        WAITING,
        CORRECTING,
        DONE,
        FLIP_OVERRIDE
    }

    var enabled = true

    private var isCycleOverriden = false

    private var mCurrentStateStartTime = 0.0

    private var mSystemState = SystemState.WAITING
    var wantedState = WantedState.AUTOMATIC
        set(value) {
            if (value == WantedState.FLIP_OVERRIDE) isCycleOverriden = true
            field = value
        }

    private val mLoop = object: Loop {
        override fun onStart(timestamp: Double) {
            synchronized(this@GearRotator) {
                mSystemState = SystemState.WAITING
                wantedState = WantedState.AUTOMATIC
            }
            mCurrentStateStartTime = timestamp
        }

        override fun onLoop(timestamp: Double) {
            synchronized(this@GearRotator) {
                if(enabled) {
                    val timeInState = timestamp - mCurrentStateStartTime
                    if (isCycleOverriden) {
                        if (wantedState == WantedState.FLIP_OVERRIDE) {
                            mSystemState = SystemState.FLIP_OVERRIDE
                        } else {
                            mSystemState = SystemState.DONE
                        }
                    }
                    val newState = when (mSystemState) {
                        SystemState.WAITING -> handleWaiting()
                        SystemState.CORRECTING -> handleCorrecting(timeInState)
                        SystemState.DONE -> handleDone()
                        SystemState.FLIP_OVERRIDE -> handleOverride()
                    }
                    if (newState != mSystemState) mCurrentStateStartTime = timestamp
                    mSystemState = newState
                } else {
                    wantedState = WantedState.AUTOMATIC
                    mSystemState = SystemState.WAITING
                    mCurrentStateStartTime = timestamp
                    stopFlipping()
                }
            }
        }

        override fun onStop(timestamp: Double) {
            stop()
        }
    }

    val isGearOut
        get() = mLidar.distanceCm > Constants.kIsGearOutDistanceThreshold

    val containsGear
        get() = mLidar.distanceCm < Constants.kContainsGearDistanceThreshold

    val isGearAligned
        get() = containsGear && !mSlotDetector.get()

    fun startFlipping() {
        mGearRotatorMotor.set(1.0)
    }

    fun stopFlipping() {
        mGearRotatorMotor.stopMotor()
    }

    fun handleWaiting(): SystemState {
        isCycleOverriden = false
        if(containsGear) {
            return SystemState.CORRECTING
        } else {
            return SystemState.WAITING
        }
    }

    fun handleCorrecting(timeInState: Double): SystemState {
        if(isGearAligned) {
            return SystemState.DONE
        } else {
            val cycleNum = (timeInState / (kFlipDuration / 2.0)).toInt()
            if(cycleNum % 2 == 0) {
                startFlipping()
            } else {
                stopFlipping()
            }
            return SystemState.CORRECTING
        }
    }

    fun handleDone(): SystemState {
        stopFlipping()
        if(isGearOut) {
            return SystemState.WAITING
        } else {
            return SystemState.DONE
        }
    }

    fun handleOverride(): SystemState {
        startFlipping()
        return SystemState.FLIP_OVERRIDE
    }


    /**
     * Resets the state machine
     */
    fun reset() {
        wantedState = WantedState.AUTOMATIC
        mSystemState = SystemState.WAITING
    }

    override fun outputToSmartDashboard() {
    }

    override fun stop() {
        stopFlipping()
    }

    override fun zeroSensors() {
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
        enabledLooper.register(mLoop)
    }
}
