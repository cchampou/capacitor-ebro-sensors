import { Sensors } from 'capacitor-ebro-sensors';

function callback(result) {
  const temperatureElement = document.getElementById('temperature');
  const probeTypeElement = document.getElementById('probe-type');
  if (temperatureElement) {
    temperatureElement.textContent = `${result.value} °C`;
  }
  if (probeTypeElement) {
    probeTypeElement.textContent = `${result.input}`;
  }
}

window.connectAndListen = () => {
  Sensors.scan()
  Sensors.addListener('temperature', callback)
}
