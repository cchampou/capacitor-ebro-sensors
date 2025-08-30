import { WebPlugin } from '@capacitor/core';

import type { SensorsPlugin } from './definitions';

export class SensorsWeb extends WebPlugin implements SensorsPlugin {
  connect(): void {
    console.warn('Method not implemented.');
  }
  disconnect(): void {
    console.warn('Method not implemented.');
  }
  isConnected(): Promise<{ connected: boolean }> {
    console.warn('Method not implemented.');
    return Promise.resolve({ connected: false });
  }
}
