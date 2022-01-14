export interface ISetting {
  id?: number;
  key?: string;
  value?: string;
  defaultValue?: string;
  description?: string | null;
}

export class Setting implements ISetting {
  constructor(
    public id?: number,
    public key?: string,
    public value?: string,
    public defaultValue?: string,
    public description?: string | null
  ) {}
}

export function getSettingIdentifier(setting: ISetting): number | undefined {
  return setting.id;
}
