import { ActuatorApiFactory } from '@evovocab/shared-api';
import { useQuery } from '@tanstack/react-query';
import { config } from '@/config/api-config';

function useActuatorHealth() {
  const health = useQuery({
    queryKey: ['actuator-health'],
    queryFn: async () => {
      const response = await ActuatorApiFactory(config).health();
      return response.data;
    },
  });
  return health;
}

export { useActuatorHealth };
