import type { StateCreator, StoreMutatorIdentifier } from 'zustand';

type Logger = <
  T,
  Mps extends Array<[StoreMutatorIdentifier, unknown]> = [],
  Mcs extends Array<[StoreMutatorIdentifier, unknown]> = [],
>(
  f: StateCreator<T, Mps, Mcs>,
  name?: string,
) => StateCreator<T, Mps, Mcs>;

export const loggerMiddleware: Logger = (f, name) => (set, get, api) => {
  const loggedSet = ((...args: Parameters<typeof set>) => {
    if (process.env.NODE_ENV === 'development') {
      console.group(`[Store: ${name || 'Anonymous'}] State Update`);
      console.log('Prev State:', get());
      set(args[0], args[1]);
      console.log('Next State:', get());
      console.groupEnd();
    } else {
      set(args[0], args[1]);
    }
  }) as typeof set;

  return f(loggedSet, get, api);
};
