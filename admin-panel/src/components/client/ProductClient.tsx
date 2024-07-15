import {Heading} from "@/components/Heading.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Plus} from "lucide-react";
import {Separator} from "@/components/ui/separator.tsx";
import {useNavigate, useParams} from "react-router-dom";
import React from "react";
import {DataTable} from "@/components/DataTable.tsx";
import {BillboardApiList} from "@/components/api/BillboardApiList.tsx";
import {columns, ProductColumn} from "@/components/tableLayout/ProductTableLayout.tsx";

interface ProductClientProps {
    data: ProductColumn[];
}

export const ProductClient: React.FC<ProductClientProps> = ({data}) => {
    const navigate = useNavigate();
    const {storeId} = useParams();

    return (
        <>
            <div className="flex items-center justify-between">
                <Heading
                    title={`Products (${data.length})`}
                    description="Mange products for your store"
                />
                <Button onClick={() => navigate(`/dashboard/${storeId}/products/new`)}>
                    <Plus className="mr-2 h-4 w-4"/>
                    Add new
                </Button>
            </div>
            <Separator/>
            <DataTable searchKey="name" columns={columns} data={data}/>
            <Heading title="API" description="API calls for Products"/>
            <BillboardApiList entityName="products" entityIdName="productId"/>
        </>
    );
}