import { GoBack } from '@/components/go-back';
import TaskManager from '@/features/test/components/task-manager';

function Test() {
  return (
    <div className="col-span-full flex flex-col gap-4 py-8 md:pt-24 md:pb-12">
      <div>
        <GoBack />
      </div>
      <div>
        <TaskManager />
      </div>
    </div>
  );
}

export { Test };
