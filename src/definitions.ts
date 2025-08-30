import type { PluginListenerHandle } from '@capacitor/core';

type ProbeType = 'penetration' | 'infrared';

type EventName = 'temperature';

type Callback<T> = (data: T) => void;

type TemperatureData = {
  probe: ProbeType;
  value: number;
}

export interface SensorsPlugin {
  connect(): void;
  disconnect(): void;
  isConnected(): Promise<{ connected: boolean }>;
  addListener(
    eventName: EventName,
    listenerFunc: Callback<TemperatureData>
  ): Promise<PluginListenerHandle>;
}
