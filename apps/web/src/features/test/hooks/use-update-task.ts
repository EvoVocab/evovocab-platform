import { useMutation } from '@tanstack/react-query';
import { type UpdateTaskRequest, TaskControllerApiFactory } from '@evovocab/shared-api';
import { config } from '@/config/api-config';
import type { TaskFormValues } from '../form';
import type { UseFormReturn } from 'react-hook-form';

function useUpdateTask(form?: UseFormReturn<TaskFormValues>) {
  const update = useMutation({
    mutationKey: ['update-task'],
    meta: {
      form,
    },
    mutationFn: async ({ id, data }: { id: number; data: UpdateTaskRequest }) => {
      const response = await TaskControllerApiFactory(config).update(id, data);
      return response.data;
    },
  });
  return update;
}
export { useUpdateTask };
