export interface ProductSummaryDTO {
    id: number;
    name: string;
    producentId: number;
    producentName: string;
    availableInStock: boolean;
    netPrice: number;
    grossPrice: number;
}