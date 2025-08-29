package com.champouillon.clement.ebrosensors

import com.getcapacitor.Logger

class Sensors {
    fun echo(value: String): String {
        Logger.debug("Echo", value)
        return value
    }
}
