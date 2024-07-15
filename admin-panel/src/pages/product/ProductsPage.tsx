import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {ProductColumn} from "@/components/tableLayout/ProductTableLayout.tsx";
import {ProductClient} from "@/components/client/ProductClient.tsx";
import {ProductSummaryDTO} from "@/models/ProductSummaryDTO.ts";

const ProductsPage = () => {
    const [products, setProducts] = useState<ProductSummaryDTO[] | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {storeId} = useParams();
    const {authState} = useAuth();

    useEffect(() => {
        setLoading(true);
        const fetchBillboards = async () => {
            try {
                const response = await axios.get<ProductSummaryDTO[]>(
                    `http://localhost:8080/api/v1/products?storeId=${storeId}`);
                setProducts(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchBillboards();
    }, [storeId, authState]);

    const formattedBillboardsToColumns = (products: ProductSummaryDTO[]): ProductColumn[] => {
        return products.map((item) => ({
            id: item.id,
            name: item.name,
            producent: item.producentName,
            availableInStock: item.availableInStock,
            netPrice: item.netPrice,
            grossPrice: item.grossPrice
        }));
    }

    if (!loading && products) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <ProductClient data={formattedBillboardsToColumns(products)}/>
                </div>
            </div>
        );
    }
}
export default ProductsPage;