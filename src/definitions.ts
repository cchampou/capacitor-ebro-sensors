type Callback<T> = (data: T) => void;

export interface SensorsPlugin {
  scan(callback: Callback<{
    value: number;
    input: string;
  }>): void;
}
