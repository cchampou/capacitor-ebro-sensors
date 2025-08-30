import type { PluginListenerHandle } from '@capacitor/core';

export type ProbeType = 'penetration' | 'infrared';

export type EventName = 'temperature' | 'connected' | 'disconnected';

export type TemperatureData = {
  probe: ProbeType;
  value: number;
}

export type TemperatureCallback = (data: TemperatureData) => void;

type TemperatureListener = (
  eventName: 'temperature',
  listenerFunc: TemperatureCallback
) => Promise<PluginListenerHandle>;
type ConnectedListener = (
  eventName: 'connected',
  listenerFunc: () => void
) => Promise<PluginListenerHandle>;
type DisconnectedListener = (
  eventName: 'disconnected',
  listenerFunc: () => void
) => Promise<PluginListenerHandle>;


export interface SensorsPlugin {
  connect(): void;
  disconnect(): void;
  addListener: TemperatureListener & ConnectedListener & DisconnectedListener;
}
