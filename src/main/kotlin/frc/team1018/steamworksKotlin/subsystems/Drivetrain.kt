package frc.team1018.steamworksKotlin.subsystems

import com.ctre.CANTalon
import com.kauailabs.navx.frc.AHRS
import com.team254.lib.util.CANTalonFactory
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.RobotDrive
import edu.wpi.first.wpilibj.SPI
import frc.team1018.lib.util.MecanumDriveSignal
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Loop
import frc.team1018.steamworksKotlin.loops.Looper
import kotlin.math.abs
import kotlin.math.max

object Drivetrain : Subsystem() {
    private val frontLeftMotor = CANTalonFactory.createDefaultTalon(Constants.kFrontLeftDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
    }
    private val frontRightMotor = CANTalonFactory.createDefaultTalon(Constants.kFrontRightDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
        inverted = true
    }
    private val rearLeftMotor = CANTalonFactory.createDefaultTalon(Constants.kRearLeftDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
    }
    private val rearRightMotor = CANTalonFactory.createDefaultTalon(Constants.kRearRightDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
        inverted = true
    }

    private val rightEncoder = Encoder(Constants.kRightEncoderChA, Constants.kRightEncoderChB, true)
    private val leftEncoder = Encoder(Constants.kLeftEncoderChA, Constants.kLeftEncoderChB, false)

    private val navX = AHRS(SPI.Port.kMXP)

    var isFieldOriented: Boolean = Constants.kIsFieldOrientedDefault

    private val driveHelper = RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor).apply {
        isSafetyEnabled = false
    }


    var brakeMode = false
        set(value) {
            frontRightMotor.enableBrakeMode(value)
            frontLeftMotor.enableBrakeMode(value)
            rearLeftMotor.enableBrakeMode(value)
            rearRightMotor.enableBrakeMode(value)
            field = value
        }

    private val mDriveControlState: DriveControlState = DriveControlState.OPEN_LOOP_MECANUM

    private val mLoop = object : Loop {
        override fun onStart(timestamp: Double) {
            synchronized(this@Drivetrain) {
                setOpenLoop(MecanumDriveSignal.NEUTRAL)

            }
        }

        override fun onLoop(timestamp: Double) {
            when (mDriveControlState) {
                DriveControlState.OPEN_LOOP_MECANUM -> return
            }
        }

        override fun onStop(timestamp: Double) {
            stop()
        }
    }


    init {
        setOpenLoop(MecanumDriveSignal.NEUTRAL)

    }

    enum class DriveControlState {
        OPEN_LOOP_MECANUM,
        TURN_TO_HEADING

    }

    val averagePositiveEncoderSpeed: Double
        get() = abs(leftEncoder.rate) + abs(rightEncoder.rate) / 2

    val isMoving: Boolean
        get() = abs(max(max(max(frontLeftMotor.get(), frontRightMotor.get()), rearRightMotor.get()), rearRightMotor.get())) > 0


    @Synchronized
    fun setOpenLoop(signal: MecanumDriveSignal) {
        if (mDriveControlState != DriveControlState.OPEN_LOOP_MECANUM) {
            frontLeftMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus)
            frontRightMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus)
            rearLeftMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus)
            rearRightMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus)
            brakeMode = false
        }
        driveHelper.mecanumDrive_Cartesian(signal.x, signal.y, signal.z, if (isFieldOriented) navX.yaw.toDouble() else 0.0)
    }

    fun resetEncoders() {
        leftEncoder.reset()
        rightEncoder.reset()
    }


    override fun outputToSmartDashboard() {

    }

    override fun stop() {
        setOpenLoop(MecanumDriveSignal.NEUTRAL)
    }

    override fun zeroSensors() {
        resetEncoders()
        navX.reset()
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
        enabledLooper.register(mLoop)
    }
}
