import { useMutation } from '@tanstack/react-query';
import { TaskControllerApiFactory } from '@evovocab/shared-api';
import { config } from '@/config/api-config';

function useDeleteTask() {
  const update = useMutation({
    mutationKey: ['delete-task'],
    mutationFn: async (id: number) => {
      const response = await TaskControllerApiFactory(config)._delete(id);
      return response.data;
    },
  });
  return update;
}
export { useDeleteTask };
