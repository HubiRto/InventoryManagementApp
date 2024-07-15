import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useAuth} from "@/providers/AuthContext.tsx";
import {ProductDetailsDTO} from "@/models/ProductDetailsDTO.ts";
import axios from "axios";
import toast from "react-hot-toast";
import {ProductForm} from "@/components/form/ProductForm.tsx";

const ProductPage = () => {
    const {storeId, productId} = useParams();
    const {authState} = useAuth();
    const navigate = useNavigate();
    const [product, setProduct] = useState<ProductDetailsDTO | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (productId !== "new") {
            const fetchProduct = async () => {
                try {
                    setLoading(true);
                    const response = await axios.get<ProductDetailsDTO>(
                        `http://localhost:8080/api/v1/products/${productId}`);
                    setProduct(response.data);
                } catch (error: any) {
                    if (error.response && error.response.status === 404) {
                        navigate(`/dashboard/${storeId}/products`);
                    } else {
                        toast.error("Server error.");
                    }
                } finally {
                    setLoading(false);
                }
            };
            fetchProduct();
        }
    }, [navigate, authState, productId, storeId]);

    if (!loading) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <ProductForm initialData={product}/>
                </div>
            </div>
        );
    }

    return <div>Loading...</div>;
}
export default ProductPage;