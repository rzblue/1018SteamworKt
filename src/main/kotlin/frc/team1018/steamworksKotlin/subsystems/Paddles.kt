package frc.team1018.steamworksKotlin.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Looper

/**
 * @author Ryan Blue
 */
object Paddles: Subsystem() {
    private val mPaddlesSolenoid = DoubleSolenoid(Constants.kPaddlesForwardSlnd, Constants.kPaddlesReverseSlnd)

    init {
        retract()
    }

    fun deploy() {
        mPaddlesSolenoid.set(DoubleSolenoid.Value.kForward)
    }

    fun retract() {
        mPaddlesSolenoid.set(DoubleSolenoid.Value.kReverse)
    }

    override fun outputToSmartDashboard() {
    }

    override fun stop() {
        retract()
    }

    override fun zeroSensors() {
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
    }
}
