import z from 'zod';
import type { CreateTaskRequest } from '@evovocab/shared-api';

export const taskFormSchema = z.object({
  title: z.string(),
  description: z.string(),
  isCompleted: z.boolean(),
}) satisfies z.ZodType<CreateTaskRequest>;

export type TaskFormValues = z.infer<typeof taskFormSchema>;
