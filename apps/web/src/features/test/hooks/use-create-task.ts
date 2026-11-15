import { useMutation } from '@tanstack/react-query';
import { type CreateTaskRequest, TaskControllerApiFactory } from '@evovocab/shared-api';
import { config } from '@/config/api-config';
import type { UseFormReturn } from 'react-hook-form';
import type { TaskFormValues } from '../form';

function useCreateTask(form?: UseFormReturn<TaskFormValues>) {
  const create = useMutation({
    mutationKey: ['create-task'],
    meta: {
      form,
    },
    mutationFn: async (data: CreateTaskRequest) => {
      const response = await TaskControllerApiFactory(config).create(data);
      return response.data;
    },
  });
  return create;
}
export { useCreateTask };
