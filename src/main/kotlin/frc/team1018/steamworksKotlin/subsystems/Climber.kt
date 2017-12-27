package frc.team1018.steamworksKotlin.subsystems

import edu.wpi.first.wpilibj.PowerDistributionPanel
import edu.wpi.first.wpilibj.TalonSRX
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Loop
import frc.team1018.steamworksKotlin.loops.Looper

/**
 * @author Ryan Blue
 */
object Climber : Subsystem() {
    private val topMotor = TalonSRX(Constants.kClimberTopPwm)
    private val bottomMotor = TalonSRX(Constants.kClimberBottomPwm).apply {
        inverted = true
    }


    enum class WantedState {
        STOP,
        CLIMB_DOWN,
        CLIMB_UP,
        CLIMB_UP_OVERRIDE
    }

    enum class SystemState {
        STOPPED,
        CLIMBING_DOWN,
        CLIMBING_UP,
        CLIMBING_UP_OVERRIDE
    }

    val isFinishedClimbing: Boolean
        get() = averageCurrent > Constants.kClimberStopCurrent

    val averageCurrent: Double
        get() = (PowerDistributionPanel().getCurrent(Constants.kClimberTopPdp) +
                PowerDistributionPanel().getCurrent(Constants.kClimberBottomPdp)) / 2

    private var mSystemState = SystemState.STOPPED
    var wantedState = WantedState.STOP

    private val mLoop = object : Loop {
        override fun onStart(timestamp: Double) {
            mSystemState = SystemState.STOPPED
            wantedState = WantedState.STOP
            setOff()
        }

        override fun onLoop(timestamp: Double) {
            val newState: SystemState
            newState = when(mSystemState) {
                SystemState.STOPPED -> handleOff()
                SystemState.CLIMBING_DOWN -> handleClimbingDown()
                SystemState.CLIMBING_UP -> handleClimbingUp()
                SystemState.CLIMBING_UP_OVERRIDE -> handleClimbingUpOverride()
            }
            if(newState != mSystemState) {
                mSystemState = newState
            }
        }

        override fun onStop(timestamp: Double) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private var currentSpeed = 0.0

    @Synchronized private fun handleOff(): SystemState {
        setOff()
        return defaultStateTransfer()
    }

    @Synchronized private fun handleClimbingDown(): SystemState {
        climbDown()
        return defaultStateTransfer()
    }

    @Synchronized private fun handleClimbingUp(): SystemState {
        if(isFinishedClimbing) {
            wantedState = WantedState.STOP
            setOff()
            return SystemState.STOPPED
        } else {
            climbUp()
            return SystemState.CLIMBING_UP
        }
    }

    @Synchronized private fun handleClimbingUpOverride(): SystemState {
        climbUp()
        return defaultStateTransfer()
    }

    private fun defaultStateTransfer(): SystemState {
        return when(wantedState) {
            WantedState.STOP -> SystemState.STOPPED
            WantedState.CLIMB_DOWN -> SystemState.CLIMBING_DOWN
            WantedState.CLIMB_UP -> SystemState.CLIMBING_UP
            WantedState.CLIMB_UP_OVERRIDE -> SystemState.CLIMBING_UP_OVERRIDE
        }
    }

    private fun setOpenLoop(speed: Double) {
        if(speed != currentSpeed) {
            topMotor.set(speed)
            bottomMotor.set(speed)
            currentSpeed = speed
        }
    }

    private fun climbUp() {
        setOpenLoop(1.0)
    }

    private fun climbDown() {
        setOpenLoop(-1.0)
    }

    private fun setOff() {
        setOpenLoop(0.0)
    }

    override fun outputToSmartDashboard() {
    }

    override fun stop() {
        setOff()
    }

    override fun zeroSensors() {
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
    }
}
