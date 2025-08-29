import { WebPlugin } from '@capacitor/core';

import type { SensorsPlugin } from './definitions';

export class SensorsWeb extends WebPlugin implements SensorsPlugin {
  async scan(): Promise<void> {
    return;
  }
}
