import { GoBack } from '@/components/go-back';
import TaskManager from '@/features/test/components/task-manager';

function Test() {
  return (
    <div className="container mx-auto space-y-8 p-6">
      <GoBack />
      <TaskManager />
    </div>
  );
}

export { Test };
