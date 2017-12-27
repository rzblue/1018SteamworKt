package frc.team1018.steamworksKotlin.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Looper

/**
 * @author Ryan Blue
 */
object Brakes: Subsystem() {
    private val mBrakesSolenoid = DoubleSolenoid(Constants.kBrakesForwardSlnd, Constants.kBrakesReverseSlnd)

    init {
        //Force a solenoid change
        release()
    }

    val canDeploy: Boolean
        get() = !Drivetrain.isMoving

    fun deploy() {
        //If the drivetrain is moving at first deploy, don't deploy
        //If brakes are deployed and it starts moving, then stay deployed
        if(canDeploy) {
            mBrakesSolenoid.set(DoubleSolenoid.Value.kForward)
        }
    }

    fun release() {
        mBrakesSolenoid.set(DoubleSolenoid.Value.kReverse)
    }

    override fun outputToSmartDashboard() {

    }

    override fun stop() {
        release()
    }

    override fun zeroSensors() {

    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
    }
}
