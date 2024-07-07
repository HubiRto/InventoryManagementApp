import {Heading} from "@/components/Heading.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Plus} from "lucide-react";
import {Separator} from "@/components/ui/separator.tsx";
import {useNavigate, useParams} from "react-router-dom";
import React from "react";
import {BillboardColumn, columns} from "@/components/tableLayout/BillboardTableLayout.tsx";
import {DataTable} from "@/components/DataTable.tsx";
import {BillboardApiList} from "@/components/api/BillboardApiList.tsx";

interface BillboardClientProps {
    data: BillboardColumn[];
}

export const BillboardClient: React.FC<BillboardClientProps> = ({data}) => {
    const navigate = useNavigate();
    const {storeId} = useParams();

    return (
        <>
            <div className="flex items-center justify-between">
                <Heading
                    title={`Billboards (${data.length})`}
                    description="Mange billboards for your store"
                />
                <Button onClick={() => navigate(`/dashboard/${storeId}/billboards/new`)}>
                    <Plus className="mr-2 h-4 w-4"/>
                    Add new
                </Button>
            </div>
            <Separator/>
            <DataTable searchKey="label" columns={columns} data={data}/>
            <Heading title="API" description="API calls for Billboards"/>
            <BillboardApiList entityName="billboards" entityIdName="billboardId"/>
        </>
    );
}