import {ProductSummary} from "@/types.ts";
import React from "react";
import {NoResult} from "@/components/NoResult.tsx";
import {ProductCard} from "@/components/ProductCard.tsx";

interface ProductListProps {
    title: string;
    items: ProductSummary[];
}

export const ProductList: React.FC<ProductListProps> = ({title, items}) => {
    return (
        <div className="space-y-4">
            <h3 className="font-bold text-3xl">{title}</h3>
            {items.length === 0 && <NoResult />}
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 auto-rows-fr">
                {items.map((item) => (
                    <ProductCard key={item.id} data={item} />
                ))}
            </div>
        </div>
    );
};