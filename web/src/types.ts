export interface Billboard {
    id: number;
    name: string;
    label: string;
    type: string;
    imageUrl: string;
    storeId: string;
    createdAt: string;
    updatedAt: string;
}

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

export interface ProductSummary {
    id: number;
    name: string;
    categoryId: number;
    producentId: number;
    producentName: string;
    availableInStock: boolean;
    netPrice: number;
    grossPrice: number;
}