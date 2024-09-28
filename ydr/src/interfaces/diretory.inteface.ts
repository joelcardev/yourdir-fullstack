import { File } from "./file.interface";

export interface Directory {
  id?: number;
  name: string;
  files?: File[];
  parentId?: number | null;

  subDirectories?: Directory[] | null;
}

export interface CreateDirectoryDTO {
  name: string;
  parentId?: number;
}
