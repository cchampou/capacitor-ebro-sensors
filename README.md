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
* [`isConnected()`](#isconnected)
* [`addListener('temperature', ...)`](#addlistenertemperature-)
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


### isConnected()

```typescript
isConnected() => Promise<{ connected: boolean; }>
```

**Returns:** <code>Promise&lt;{ connected: boolean; }&gt;</code>

--------------------


### addListener('temperature', ...)

```typescript
addListener(eventName: EventName, listenerFunc: Callback<TemperatureData>) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                                |
| ------------------ | --------------------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'temperature'</code>                                                                          |
| **`listenerFunc`** | <code><a href="#callback">Callback</a>&lt;<a href="#temperaturedata">TemperatureData</a>&gt;</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### EventName

<code>'temperature'</code>


#### Callback

<code>(data: T): void</code>


#### TemperatureData

<code>{ probe: <a href="#probetype">ProbeType</a>; value: number; }</code>


#### ProbeType

<code>'penetration' | 'infrared'</code>

</docgen-api>
