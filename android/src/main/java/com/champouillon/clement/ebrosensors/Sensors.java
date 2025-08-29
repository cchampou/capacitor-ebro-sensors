package com.champouillon.clement.ebrosensors;

import com.getcapacitor.Logger;

public class Sensors {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
