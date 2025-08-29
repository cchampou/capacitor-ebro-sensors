export interface SensorsPlugin {
  scan(): Promise<void>;
}
