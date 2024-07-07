import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {useAuth} from "@/providers/AuthContext.tsx";
import toast from "react-hot-toast";
import {CategoryForm} from "@/components/form/CategoryForm.tsx";
import {BillboardName} from "@/models/BillboardName.ts";
import {Category} from "@/models/Category.ts";

const CategoryPage = () => {
    const {storeId, categoryId} = useParams();
    const {authState} = useAuth();
    const navigate = useNavigate();
    const [billboardsNames, setBillboardsNames] = useState<BillboardName[]>([]);
    const [category, setCategory] = useState<Category | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if(categoryId !== "new") {
            const fetchCategory = async () => {
                try {
                    setLoading(true);
                    const response = await axios.get<Category>(
                        `http://localhost:8080/api/v1/categories/${categoryId}`,
                        {
                            headers: {
                                Authorization: `Bearer ${authState?.token}`,
                            },
                        });
                    setCategory(response.data)
                } catch (error: any) {
                    if (error.response && error.response.status === 404) {
                        navigate(`/dashboard/${storeId}/categories`);
                    } else {
                        toast.error("Server error.");
                    }
                } finally {
                    setLoading(false);
                }
            };

            fetchCategory();
        }

        const fetchBillboardsNames = async () => {
            try {
                setLoading(true);
                const response = await axios.get<BillboardName[]>(
                    `http://localhost:8080/api/v1/billboards/names?storeId=${storeId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                    });
                setBillboardsNames(response.data)
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate(`/dashboard/${storeId}/categories`);
                } else {
                    toast.error("Server error.");
                }
            } finally {
                setLoading(false);
            }
        };
        fetchBillboardsNames();
    }, [navigate, authState, categoryId, storeId]);

    if (!loading) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <CategoryForm
                        initialData={category}
                        billboards={billboardsNames}
                    />
                </div>
            </div>
        );
    }
}
export default CategoryPage;