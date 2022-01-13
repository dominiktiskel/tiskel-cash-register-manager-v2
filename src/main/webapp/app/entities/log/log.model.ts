import dayjs from 'dayjs/esm';
import { LogType } from 'app/entities/enumerations/log-type.model';

export interface ILog {
  id?: number;
  created?: dayjs.Dayjs;
  type?: LogType;
  message?: string | null;
  data?: string | null;
}

export class Log implements ILog {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public type?: LogType,
    public message?: string | null,
    public data?: string | null
  ) {}
}

export function getLogIdentifier(log: ILog): number | undefined {
  return log.id;
}
