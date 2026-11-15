# TaskControllerApi

All URIs are relative to *http://localhost:5000/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**_delete**](#_delete) | **DELETE** /v1/tasks/{id} | |
|[**create**](#create) | **POST** /v1/tasks | |
|[**getAll**](#getall) | **GET** /v1/tasks | |
|[**update**](#update) | **PUT** /v1/tasks | |

# **_delete**
> ApiResponseDtoVoid _delete()


### Example

```typescript
import {
    TaskControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TaskControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance._delete(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ApiResponseDtoVoid**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create**
> ApiResponseDtoTask create(createTaskRequest)


### Example

```typescript
import {
    TaskControllerApi,
    Configuration,
    CreateTaskRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TaskControllerApi(configuration);

let createTaskRequest: CreateTaskRequest; //

const { status, data } = await apiInstance.create(
    createTaskRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createTaskRequest** | **CreateTaskRequest**|  | |


### Return type

**ApiResponseDtoTask**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAll**
> ApiResponseDtoListTask getAll()


### Example

```typescript
import {
    TaskControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TaskControllerApi(configuration);

const { status, data } = await apiInstance.getAll();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**ApiResponseDtoListTask**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update**
> ApiResponseDtoTask update(updateTaskRequest)


### Example

```typescript
import {
    TaskControllerApi,
    Configuration,
    UpdateTaskRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TaskControllerApi(configuration);

let updateTaskRequest: UpdateTaskRequest; //

const { status, data } = await apiInstance.update(
    updateTaskRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateTaskRequest** | **UpdateTaskRequest**|  | |


### Return type

**ApiResponseDtoTask**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

