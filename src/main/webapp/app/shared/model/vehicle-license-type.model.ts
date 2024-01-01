export interface IVehicleLicenseType {
  id?: number;
  name?: string | null;
  rank?: number | null;
  engName?: string | null;
  code?: string | null;
}

export const defaultValue: Readonly<IVehicleLicenseType> = {};
