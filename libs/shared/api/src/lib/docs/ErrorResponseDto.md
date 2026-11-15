# ErrorResponseDto


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **string** |  | [optional] [default to undefined]
**title** | **string** |  | [optional] [default to undefined]
**status** | **number** |  | [optional] [default to undefined]
**detail** | **string** |  | [optional] [default to undefined]
**instance** | **string** |  | [optional] [default to undefined]
**errors** | **{ [key: string]: string; }** |  | [optional] [default to undefined]
**timestamp** | **number** |  | [optional] [default to undefined]

## Example

```typescript
import { ErrorResponseDto } from './api';

const instance: ErrorResponseDto = {
    type,
    title,
    status,
    detail,
    instance,
    errors,
    timestamp,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
