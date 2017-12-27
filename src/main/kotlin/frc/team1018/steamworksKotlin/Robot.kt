package frc.team1018.steamworksKotlin

import edu.wpi.first.wpilibj.IterativeRobot
import frc.team1018.steamworksKotlin.loops.Looper
import frc.team1018.steamworksKotlin.subsystems.*
import java.util.*

object Robot: IterativeRobot() {
    private val mDrivetrain = Drivetrain
    private val mClimber = Climber
    private val mBrakes = Brakes
    private val mPaddles = Paddles
    private val mGearRotator = GearRotator

    private val mControlBoard = ControlBoard

    private val mSubsystemManager = SubsystemManager(Arrays.asList(Drivetrain, Climber, Brakes, Paddles, GearRotator))
    private val mEnabledLooper = Looper()

    override fun robotInit() {
        mSubsystemManager.registerEnabledLoops(mEnabledLooper)
        mSubsystemManager.zeroSensors()
    }

    override fun robotPeriodic() {
        super.robotPeriodic()
    }

    override fun teleopInit() {
        mGearRotator.enabled = true
        mEnabledLooper.start()
        mPaddles.deploy()
    }

    override fun teleopPeriodic() {
        mDrivetrain.setOpenLoop(mControlBoard.latestDrivePacket)

        mClimber.wantedState = when {
            mControlBoard.climbUpButton -> Climber.WantedState.CLIMB_UP
            mControlBoard.climbDownButton -> Climber.WantedState.CLIMB_DOWN
            else -> Climber.WantedState.STOP
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

        mGearRotator.wantedState = if(mControlBoard.gearRotatorButton) {
            GearRotator.WantedState.FLIP_OVERRIDE
        } else {
            GearRotator.WantedState.AUTOMATIC
        }
    }


    override fun autonomousInit() {
        mGearRotator.enabled = false
        mEnabledLooper.start()
        mPaddles.retract()
    }

    override fun autonomousPeriodic() {
        super.autonomousPeriodic()
    }
}
