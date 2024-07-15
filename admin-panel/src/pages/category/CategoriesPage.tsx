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
                    `http://localhost:8080/api/v1/categories?storeId=${storeId}`);
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

    const formattedCategoriesToColumns = (categories: Category[]): CategoryColumn[] => {
        const processCategory = (category: Category, parentName: string): CategoryColumn[] => {
            const fullName = parentName ? `${parentName} / ${category.name}` : category.name;
            const column: CategoryColumn = {
                id: category.id,
                name: fullName,
                billboardLabel: category.billboardLabel !== null ? category.billboardLabel! : 'Empty',
                createdAt: format(category.createdAt, "MMMM do, yyyy")
            };
            let childrenColumns: CategoryColumn[] = [];
            if (category.children) {
                childrenColumns = category.children.flatMap(child => processCategory(child, fullName));
            }
            return [column, ...childrenColumns];
        };

        return categories.flatMap(category => processCategory(category, ""));
    };

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