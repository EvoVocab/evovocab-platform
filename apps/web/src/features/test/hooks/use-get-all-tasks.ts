import { useQuery } from '@tanstack/react-query';
import { config } from '@/config/api-config';
import { type Task, TaskControllerApiFactory } from '@evovocab/shared-api';

function useGetAllTasks() {
  const tasks = useQuery({
    queryKey: ['tasks'],
    queryFn: async (): Promise<Array<Task>> => {
      const response = await TaskControllerApiFactory(config).getAll();
      return response.data.data as Array<Task>;
    },
  });
  return tasks;
}
export { useGetAllTasks };
