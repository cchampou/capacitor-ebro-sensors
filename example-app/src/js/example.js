import { Sensors } from 'capacitor-ebro-sensors';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    Sensors.scan({ value: inputValue })
}
