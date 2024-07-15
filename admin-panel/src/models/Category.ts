export interface Category {
    id: number;
    name: string;
    createdAt: string;
    updatedAt: string | null;
    billboardId: number | null;
    billboardLabel: string | null;
    parentId: number | null;
    children: Category[] | null;
}