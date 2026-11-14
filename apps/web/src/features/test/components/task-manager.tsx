'use client';

import * as React from 'react';
import { zodResolver } from '@hookform/resolvers/zod';
import { Controller, useForm } from 'react-hook-form';
import { toast } from 'sonner';
import * as z from 'zod';
import { Pencil, Plus, Trash2 } from 'lucide-react';

import {
  Badge,
  Button,
  Checkbox,
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  Field,
  FieldDescription,
  FieldError,
  FieldGroup,
  FieldLabel,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  InputGroupTextarea,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@evovocab/ui';

export interface Task {
  id: string;
  title: string;
  description: string;
  isCompleted: boolean;
}

const taskFormSchema = z.object({
  title: z.string().min(1, 'Tiêu đề không được để trống.').max(50, 'Tiêu đề tối đa 50 ký tự.'),
  description: z.string().max(200, 'Mô tả tối đa 200 ký tự.'),
  isCompleted: z.boolean(),
});

type TaskFormValues = z.infer<typeof taskFormSchema>;

const INITIAL_TASKS: Array<Task> = [
  {
    id: '1',
    title: 'Thiết kế cơ sở dữ liệu',
    description: 'Hoàn thiện sơ đồ ERD cho thực thể TASK',
    isCompleted: true,
  },
  {
    id: '2',
    title: 'Xây dựng giao diện CRUD',
    description: 'Tích hợp Table, Dialog và Field với React Hook Form',
    isCompleted: false,
  },
];

export default function TaskManager() {
  const [tasks, setTasks] = React.useState<Array<Task>>(INITIAL_TASKS);
  const [isModalOpen, setIsModalOpen] = React.useState(false);
  const [editingTask, setEditingTask] = React.useState<Task | null>(null);

  const form = useForm<TaskFormValues>({
    resolver: zodResolver(taskFormSchema),
    defaultValues: {
      title: '',
      description: '',
      isCompleted: false,
    },
  });

  const handleOpenCreate = () => {
    setEditingTask(null);
    form.reset({
      title: '',
      description: '',
      isCompleted: false,
    });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (task: Task) => {
    setEditingTask(task);
    form.reset({
      title: task.title,
      description: task.description,
      isCompleted: task.isCompleted,
    });
    setIsModalOpen(true);
  };

  function onSubmit(data: TaskFormValues) {
    if (editingTask) {
      setTasks((previous) =>
        previous.map((item) => (item.id === editingTask.id ? { ...item, ...data } : item)),
      );
      toast.success(`Đã cập nhật ${editingTask.id}`);
    } else {
      const newTask: Task = {
        id: `${String(tasks.length + 1)}`,
        ...data,
      };
      setTasks((previous) => [newTask, ...previous]);
      toast.success(`Đã thêm ${newTask.id}`);
    }
    setIsModalOpen(false);
  }

  const handleDelete = (id: string) => {
    setTasks((previous) => previous.filter((item) => item.id !== id));
    toast.success(`Đã xóa ${id}`);
  };

  const handleToggleComplete = (id: string, checked: boolean) => {
    setTasks((previous) =>
      previous.map((item) => (item.id === id ? { ...item, isCompleted: checked } : item)),
    );
  };

  return (
    <>
      <div className="flex flex-wrap items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Quản lý Task</h1>
          <p className="text-muted-foreground text-sm">Danh sách công việc theo hiện tại.</p>
        </div>
        <Button onClick={handleOpenCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Thêm Task
        </Button>
      </div>

      <div className="rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-12.5 text-center">Xong</TableHead>
              <TableHead className="w-25">ID</TableHead>
              <TableHead>Tiêu đề</TableHead>
              <TableHead>Mô tả</TableHead>
              <TableHead className="w-35">Trạng thái</TableHead>
              <TableHead className="w-30 text-right">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {tasks.length === 0 ? (
              <TableRow>
                <TableCell className="text-muted-foreground h-24 text-center" colSpan={6}>
                  Chưa có công việc nào. Hãy bấm &quot;Thêm Task&quot; để tạo mới.
                </TableCell>
              </TableRow>
            ) : (
              tasks.map((task) => (
                <TableRow key={task.id}>
                  <TableCell className="text-center">
                    <Checkbox
                      aria-label={`Đánh dấu hoàn thành ${task.title}`}
                      checked={task.isCompleted}
                      onCheckedChange={(checked) => {
                        handleToggleComplete(task.id, checked === true);
                      }}
                    />
                  </TableCell>
                  <TableCell className="font-mono text-xs font-medium">{task.id}</TableCell>
                  <TableCell
                    className={
                      task.isCompleted
                        ? 'text-muted-foreground font-medium line-through'
                        : 'font-medium'
                    }
                  >
                    {task.title}
                  </TableCell>
                  <TableCell className="text-muted-foreground max-w-xs truncate">
                    {task.description || '—'}
                  </TableCell>
                  <TableCell>
                    {task.isCompleted ? (
                      <Badge variant="default">Hoàn thành</Badge>
                    ) : (
                      <Badge variant="secondary">Chưa xong</Badge>
                    )}
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button
                        size="icon"
                        title="Chỉnh sửa"
                        variant="outline"
                        onClick={() => {
                          handleOpenEdit(task);
                        }}
                      >
                        <Pencil className="h-4 w-4" />
                      </Button>
                      <Button
                        size="icon"
                        title="Xóa"
                        variant="destructive"
                        onClick={() => {
                          handleDelete(task.id);
                        }}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent className="sm:max-w-115">
          <DialogHeader>
            <DialogTitle>
              {editingTask ? `Chỉnh sửa: ${editingTask.id}` : 'Thêm Task mới'}
            </DialogTitle>
            <DialogDescription>Nhập thông tin công việc và nhấn Lưu để cập nhật.</DialogDescription>
          </DialogHeader>

          <form id="task-form" onSubmit={form.handleSubmit(onSubmit)}>
            <FieldGroup>
              <Controller
                control={form.control}
                name="title"
                render={({ field, fieldState }) => (
                  <Field data-invalid={fieldState.invalid}>
                    <FieldLabel htmlFor="task-form-title">Tiêu đề </FieldLabel>
                    <Input
                      {...field}
                      aria-invalid={fieldState.invalid}
                      autoComplete="off"
                      id="task-form-title"
                      placeholder="Nhập tiêu đề công việc..."
                    />
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </Field>
                )}
              />

              <Controller
                control={form.control}
                name="description"
                render={({ field, fieldState }) => (
                  <Field data-invalid={fieldState.invalid}>
                    <FieldLabel htmlFor="task-form-description">Mô tả (description)</FieldLabel>
                    <InputGroup>
                      <InputGroupTextarea
                        {...field}
                        aria-invalid={fieldState.invalid}
                        className="min-h-24 resize-none"
                        id="task-form-description"
                        placeholder="Nhập mô tả chi tiết công việc..."
                        rows={4}
                      />
                      <InputGroupAddon align="block-end">
                        <InputGroupText className="tabular-nums">
                          {field.value.length}/200 ký tự
                        </InputGroupText>
                      </InputGroupAddon>
                    </InputGroup>
                    <FieldDescription>Mô tả ngắn gọn nội dung cần thực hiện.</FieldDescription>
                    {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                  </Field>
                )}
              />

              <Controller
                control={form.control}
                name="isCompleted"
                render={({ field, fieldState }) => (
                  <Field
                    className="rounded-md border p-3"
                    data-invalid={fieldState.invalid}
                    orientation="horizontal"
                  >
                    <Checkbox
                      aria-invalid={fieldState.invalid}
                      checked={field.value}
                      id="task-form-completed"
                      onCheckedChange={(checked) => {
                        field.onChange(checked === true);
                      }}
                    />
                    <FieldLabel
                      className="cursor-pointer font-normal"
                      htmlFor="task-form-completed"
                    >
                      Đã hoàn thành
                    </FieldLabel>
                  </Field>
                )}
              />
            </FieldGroup>
          </form>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                setIsModalOpen(false);
              }}
            >
              Hủy
            </Button>
            <Button form="task-form" type="submit">
              {editingTask ? 'Lưu thay đổi' : 'Tạo mới'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}
