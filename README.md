# capacitor-ebro-sensors

Connect to Ebro sensors using BLE

## Install

```bash
npm install capacitor-ebro-sensors
npx cap sync
```

## API

<docgen-index>

* [`connect()`](#connect)
* [`disconnect()`](#disconnect)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### connect()

```typescript
connect() => void
```

--------------------


### disconnect()

```typescript
disconnect() => void
```

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### TemperatureListener

<code>(eventName: "temperature", listenerFunc: <a href="#temperaturecallback">TemperatureCallback</a>): Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>


#### TemperatureCallback

<code>(data: <a href="#temperaturedata">TemperatureData</a>): void</code>


#### TemperatureData

<code>{ probe: <a href="#probetype">ProbeType</a>; value: number; }</code>


#### ProbeType

<code>'penetration' | 'infrared'</code>


#### ConnectedListener

<code>(eventName: "connected", listenerFunc: () =&gt; void): Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>


#### DisconnectedListener

<code>(eventName: "disconnected", listenerFunc: () =&gt; void): Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

</docgen-api>
