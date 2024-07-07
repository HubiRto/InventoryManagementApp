import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {format} from "date-fns";
import {Category} from "@/models/Category.ts";
import {CategoryColumn} from "@/components/tableLayout/CategoryTableLayout.tsx";
import {CategoryClient} from "@/components/client/CategoryClient.tsx";

const CategoriesPage = () => {
    const [categories, setCategories] = useState<Category[] | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {storeId} = useParams();
    const {authState} = useAuth();

    useEffect(() => {
        setLoading(true);
        const fetchCategories = async () => {
            try {
                const response = await axios.get<Category[]>(
                    `http://localhost:8080/api/v1/categories?storeId=${storeId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                    }
                );
                setCategories(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchCategories();
    }, [storeId, authState]);

    const formattedCategoriesToColumns = (categoriess: Category[]): CategoryColumn[] => {
        return categoriess.map((item: Category) => ({
            id: item.id,
            name: item.name,
            billboardLabel: item.billboardLabel !== null ? item.billboardLabel! : 'Empty',
            createdAt: format(item.createdAt, "MMMM do, yyyy")
        }));
    }

    if (!loading && categories) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <CategoryClient data={formattedCategoriesToColumns(categories)}/>
                </div>
            </div>
        );
    }
}
export default CategoriesPage;