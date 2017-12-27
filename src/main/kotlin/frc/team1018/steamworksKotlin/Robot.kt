package frc.team1018.steamworksKotlin

import edu.wpi.first.wpilibj.IterativeRobot
import frc.team1018.steamworksKotlin.loops.Looper
import frc.team1018.steamworksKotlin.subsystems.Brakes
import frc.team1018.steamworksKotlin.subsystems.Climber
import frc.team1018.steamworksKotlin.subsystems.Drivetrain
import frc.team1018.steamworksKotlin.subsystems.Paddles
import java.util.*

object Robot: IterativeRobot() {
    private val mDrivetrain = Drivetrain
    private val mClimber = Climber
    private val mBrakes = Brakes
    private val mPaddles = Paddles

    private val mControlBoard = ControlBoard

    private val mSubsystemManager = SubsystemManager(Arrays.asList(Drivetrain, Climber))
    private val mEnabledLooper = Looper()
    override fun robotInit() {
        mSubsystemManager.registerEnabledLoops(mEnabledLooper)
        mSubsystemManager.zeroSensors()
    }

    override fun robotPeriodic() {
        super.robotPeriodic()
    }

    override fun teleopInit() {
        mEnabledLooper.start()
        mPaddles.deploy()
    }

    override fun teleopPeriodic() {
        mDrivetrain.setOpenLoop(mControlBoard.latestDrivePacket)

        mClimber.wantedState = if(mControlBoard.climbUpButton) {
            Climber.WantedState.CLIMB_UP
        } else if(mControlBoard.climbDownButton) {
            Climber.WantedState.CLIMB_DOWN
        } else {
            Climber.WantedState.STOP
        }

        if(mControlBoard.paddlesInButton) {
            mPaddles.retract()
        } else {
            mPaddles.deploy()
        }

        if(mControlBoard.brakesButton) {
            mBrakes.deploy()
        } else {
            mBrakes.release()
        }


    }

    override fun autonomousInit() {
        mEnabledLooper.start()
        mPaddles.retract()
    }

    override fun autonomousPeriodic() {
        super.autonomousPeriodic()
    }
}
