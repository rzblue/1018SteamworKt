package com.team254.lib.util

import com.ctre.CANTalon

class LazyCANTalon : CANTalon {
    protected var lastSet = Double.NaN
    protected var lastControlMode: TalonControlMode = TalonControlMode.Disabled

    constructor(deviceNumber: Int) : super(deviceNumber)
    constructor(deviceNumber: Int, controlPeriodMs: Int) : super(deviceNumber, controlPeriodMs)
    constructor(deviceNumber: Int, controlPeriodMs: Int, enablePeriodMs: Int) : super(deviceNumber, controlPeriodMs, enablePeriodMs)

    override fun set(outputValue: Double) {
        if(outputValue != lastSet || controlMode != lastControlMode) {
            lastSet = outputValue
            lastControlMode = controlMode
            super.set(outputValue)
        }
    }
}
