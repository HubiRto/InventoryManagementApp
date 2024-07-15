export interface ProductDetailsDTO {
    id: number;
    name: string;
    description: string;
    producentId: number;
    producentName: string;
    categoryId: number;
    quantity: number;
    netPrice: number;
    grossPrice: number;
    vat: number;
    attributes: { [key: string]: string };
    images: string[];
    createdAt: string;
    updatedAt: string;
}