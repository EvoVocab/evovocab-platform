# ActuatorApi

All URIs are relative to *http://localhost:5000/api*

| Method                                    | HTTP request                                   | Description                                                  |
| ----------------------------------------- | ---------------------------------------------- | ------------------------------------------------------------ |
| [**environment**](#environment)           | **GET** /actuator/env                          | Actuator web endpoint \&#39;env\&#39;                        |
| [**environmentEntry**](#environmententry) | **GET** /actuator/env/{toMatch}                | Actuator web endpoint \&#39;env-toMatch\&#39;                |
| [**health**](#health)                     | **GET** /actuator/health                       | Actuator web endpoint \&#39;health\&#39;                     |
| [**info**](#info)                         | **GET** /actuator/info                         | Actuator web endpoint \&#39;info\&#39;                       |
| [**links**](#links)                       | **GET** /actuator                              | Actuator root web endpoint                                   |
| [**listNames**](#listnames)               | **GET** /actuator/metrics                      | Actuator web endpoint \&#39;metrics\&#39;                    |
| [**metric**](#metric)                     | **GET** /actuator/metrics/{requiredMetricName} | Actuator web endpoint \&#39;metrics-requiredMetricName\&#39; |

# **environment**

> object environment()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

let pattern: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.environment(pattern);
```

### Parameters

| Name        | Type         | Description | Notes                            |
| ----------- | ------------ | ----------- | -------------------------------- |
| **pattern** | [**string**] |             | (optional) defaults to undefined |

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **environmentEntry**

> object environmentEntry()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

let toMatch: string; // (default to undefined)

const { status, data } = await apiInstance.environmentEntry(toMatch);
```

### Parameters

| Name        | Type         | Description | Notes                 |
| ----------- | ------------ | ----------- | --------------------- |
| **toMatch** | [**string**] |             | defaults to undefined |

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |
| **404**     | Not Found   | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **health**

> object health()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.health();
```

### Parameters

This endpoint does not have any parameters.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **info**

> object info()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.info();
```

### Parameters

This endpoint does not have any parameters.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **links**

> { [key: string]: { [key: string]: Link; }; } links()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.links();
```

### Parameters

This endpoint does not have any parameters.

### Return type

**{ [key: string]: { [key: string]: Link; }; }**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listNames**

> object listNames()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

const { status, data } = await apiInstance.listNames();
```

### Parameters

This endpoint does not have any parameters.

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **metric**

> object metric()

### Example

```typescript
import { ActuatorApi, Configuration } from './api';

const configuration = new Configuration();
const apiInstance = new ActuatorApi(configuration);

let requiredMetricName: string; // (default to undefined)
let tag: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.metric(requiredMetricName, tag);
```

### Parameters

| Name                   | Type         | Description | Notes                            |
| ---------------------- | ------------ | ----------- | -------------------------------- |
| **requiredMetricName** | [**string**] |             | defaults to undefined            |
| **tag**                | [**string**] |             | (optional) defaults to undefined |

### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/vnd.spring-boot.actuator.v3+json, application/vnd.spring-boot.actuator.v2+json, application/json

### HTTP response details

| Status code | Description | Response headers |
| ----------- | ----------- | ---------------- |
| **200**     | OK          | -                |
| **404**     | Not Found   | -                |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)
