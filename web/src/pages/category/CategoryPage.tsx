import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {useAuth} from "@/providers/AuthContext.tsx";
import toast from "react-hot-toast";
import {CategoryForm} from "@/components/form/CategoryForm.tsx";
import {BillboardName} from "@/models/BillboardName.ts";
import {Category} from "@/models/Category.ts";
import {CategoryName} from "@/models/CategoryName.ts";

const CategoryPage = () => {
    const {storeId, categoryId} = useParams();
    const {authState} = useAuth();
    const navigate = useNavigate();
    const [billboardsNames, setBillboardsNames] = useState<BillboardName[]>([]);
    const [categoryNames, setCategoryNames] = useState<CategoryName[]>([]);
    const [category, setCategory] = useState<Category | null>(null);

    const [loadingCategory, setLoadingCategory] = useState(false);
    const [loadingBillboards, setLoadingBillboards] = useState(false);
    const [loadingCategoriesNames, setLoadingCategoriesNames] = useState(false);

    useEffect(() => {
        if(categoryId !== "new") {
            const fetchCategory = async () => {
                try {
                    setLoadingCategory(true);
                    const response = await axios.get<Category>(
                        `http://localhost:8080/api/v1/categories/${categoryId}`);
                    setCategory(response.data);
                } catch (error: any) {
                    navigate(`/dashboard/${storeId}/categories`);
                    toast.error("Server error.");
                } finally {
                    setLoadingCategory(false);
                }
            };

            fetchCategory();
        }

        const fetchBillboardsNames = async () => {
            try {
                setLoadingBillboards(true);
                const response = await axios.get<BillboardName[]>(
                    `http://localhost:8080/api/v1/billboards/names?storeId=${storeId}`);
                setBillboardsNames(response.data)
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate(`/dashboard/${storeId}/categories`);
                } else {
                    toast.error("Server error.");
                }
            } finally {
                setLoadingBillboards(false);
            }
        };
        fetchBillboardsNames();

        const fetchCategoryNames = async () => {
            try {
                setLoadingCategoriesNames(true);
                const response = await axios.get<CategoryName[]>(
                    `http://localhost:8080/api/v1/categories/names?storeId=${storeId}`);
                setCategoryNames(response.data)
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate(`/dashboard/${storeId}/categories`);
                } else {
                    toast.error("Server error.");
                }
            } finally {
                setLoadingCategoriesNames(false);
            }
        };
        fetchCategoryNames();
    }, [navigate, authState, categoryId, storeId]);

    if (!loadingCategory && !loadingBillboards && !loadingCategoriesNames) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <CategoryForm
                        initialData={category}
                        billboards={billboardsNames}
                        categories={categoryNames}
                    />
                </div>
            </div>
        );
    }
}
export default CategoryPage;