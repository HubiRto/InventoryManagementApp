import {Heading} from "@/components/Heading.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Plus} from "lucide-react";
import {Separator} from "@/components/ui/separator.tsx";
import {useNavigate, useParams} from "react-router-dom";
import React from "react";
import {DataTable} from "@/components/DataTable.tsx";
import {CategoryColumn, columns} from "@/components/tableLayout/CategoryTableLayout.tsx";
import {CategoryApiList} from "@/components/api/CategoryApiList.tsx";

interface CategoryClientProps {
    data: CategoryColumn[];
}

export const CategoryClient: React.FC<CategoryClientProps> = ({data}) => {
    const navigate = useNavigate();
    const {storeId} = useParams();

    return (
        <>
            <div className="flex items-center justify-between">
                <Heading
                    title={`Categories (${data.length})`}
                    description="Mange categories for your store"
                />
                <Button onClick={() => navigate(`/dashboard/${storeId}/categories/new`)}>
                    <Plus className="mr-2 h-4 w-4"/>
                    Add new
                </Button>
            </div>
            <Separator/>
            <DataTable searchKey="name" columns={columns} data={data}/>
            <Heading title="API" description="API calls for Categories"/>
            <CategoryApiList entityName="categories" entityIdName="categoryId"/>
        </>
    );
}