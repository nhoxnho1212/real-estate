import { IProject } from 'app/shared/model/project.model';

export interface IHomeType {
  id?: number;
  typeName?: string | null;
  projects?: IProject[] | null;
}

export const defaultValue: Readonly<IHomeType> = {};
