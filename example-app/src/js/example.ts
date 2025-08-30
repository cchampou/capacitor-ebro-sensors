import type { TemperatureData } from 'capacitor-ebro-sensors';
import { Sensors } from 'capacitor-ebro-sensors';

function callback(result: TemperatureData) {
  const temperatureElement = document.getElementById('temperature');
  const probeTypeElement = document.getElementById('probe-type');
  if (temperatureElement) {
    temperatureElement.textContent = `${result.value} °C`;
  }
  if (probeTypeElement) {
    probeTypeElement.textContent = `${result.probe}`;
  }
}

window.connectAndListen = () => {
  Sensors.connect()
  Sensors.addListener('temperature', callback)
}

window.disconnect = () => {
  Sensors.disconnect()
}

Sensors.addListener('disconnected', () => {
  const temperatureElement = document.getElementById('temperature');
  const probeTypeElement = document.getElementById('probe-type');
  const statusElement = document.getElementById('status');
  if (statusElement) {
    statusElement.textContent = 'Disconnected';
  }
  if (temperatureElement) {
    temperatureElement.textContent = `-- °C`;
  }
  if (probeTypeElement) {
    probeTypeElement.textContent = `--`;
  }
})
Sensors.addListener('connected', () => {
  const statusElement = document.getElementById('status');
  if (statusElement) {
    statusElement.textContent = 'Connected';
  }
})
