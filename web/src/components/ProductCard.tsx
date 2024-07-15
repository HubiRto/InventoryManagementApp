import {Category, ProductSummary} from "@/types.ts";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {IconButton} from "@/components/IconButton.tsx";
import {Expand, ShoppingCart} from "lucide-react";
import {Currency} from "@/components/Currency.tsx";

interface ProductCardProps {
    data: ProductSummary;
}

export const ProductCard: React.FC<ProductCardProps> = ({data}) => {
    const [firstImageUrl, setFirstImageUrl] = useState<string>();
    const [loadingFirstImageUrl, setLoadingFirstImageUrl] = useState<boolean>();

    const [category, setCategory] = useState<Category>();
    const [loadingCategory, setLoadingCategory] = useState<boolean>();

    useEffect(() => {
        const fetchProductImagesUrls = async () => {
            setLoadingFirstImageUrl(true);
            try {
                const response = await axios.get<string[]>(
                    `http://localhost:8080/api/v1/products/${data.id}/images`);
                console.log(response.data[0]);
                setFirstImageUrl(response.data[0]);
            } catch (error: any) {
                console.log(error);
            } finally {
                setLoadingFirstImageUrl(false);
            }
        }
        fetchProductImagesUrls();

        const fetchProductCategory = async () => {
            setLoadingCategory(true);
            try {
                const response = await axios.get<Category>(
                    `http://localhost:8080/api/v1/categories/${data?.categoryId}`);
                setCategory(response.data);
            } catch (error: any) {
                console.log(error);
            } finally {
                setLoadingCategory(false);
            }
        }
        fetchProductCategory();
    }, [data]);

    return (
        <div className="bg-white group cursor-pointer rounded-xl border p-3 flex flex-col">
            <div className="aspect-square rounded-xl bg-gray-100 relative">
                {(!loadingFirstImageUrl && firstImageUrl) && (
                    <img
                        src={firstImageUrl} alt="Image"
                        className="aspect-square object-cover rounded-md w-full h-full"
                    />
                )}
                <div className="opacity-0 group-hover:opacity-100 transition absolute w-full px-6 bottom-5">
                    <div className="flex gap-x-6 justify-center">
                        <IconButton
                            onClick={() => {
                            }}
                            icon={<Expand size={20} className="text-gray-600" />}
                        />
                        <IconButton
                            onClick={() => {
                            }}
                            icon={<ShoppingCart size={20} className="text-gray-600" />}
                        />
                    </div>
                </div>
            </div>
            <div className="flex-grow">
                {data && (
                    <p className="font-semibold text-lg">{data?.name}</p>
                )}
                {(!loadingCategory && category) && (
                    <p className="text-sm text-gray-500">{category?.name}</p>
                )}
            </div>
            <div className="flex items-center justify-between mt-auto">
                <Currency value={data?.netPrice} />
            </div>
        </div>
    );
}