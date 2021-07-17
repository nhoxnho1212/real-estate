import { IProject } from 'app/shared/model/project.model';

export interface IExtra {
  id?: number;
  extraName?: string | null;
  projects?: IProject[] | null;
}

export const defaultValue: Readonly<IExtra> = {};
