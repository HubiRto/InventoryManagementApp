import {ColumnDef} from "@tanstack/react-table";
import {Button} from "@/components/ui/button.tsx";
import {ArrowUpDown} from "lucide-react";
import {ProductCellAction} from "@/components/cellAction/ProductCellAction.tsx";

export type ProductColumn = {
    id: number;
    name: string;
    producent: string;
    availableInStock: boolean;
    netPrice: number;
    grossPrice: number;
}

export const columns: ColumnDef<ProductColumn>[] = [
    {
        accessorKey: "name",
        header: "Name",
    },
    {
        accessorKey: "producent",
        header: "Producent",
    },
    {
        accessorKey: "availableInStock",
        header: "Available",
    },
    {
        accessorKey: "netPrice",
        header: ({column}) => {
            return (
                <Button
                    variant="ghost"
                    onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                >
                    Netto Price
                    <ArrowUpDown className="ml-2 h-4 w-4"/>
                </Button>
            )
        },
        cell: ({row}) => (`${row.original.netPrice} zł`)
    },
    {
        accessorKey: "grossPrice",
        header: "Gross Price",
        cell: ({row}) => (`${row.original.grossPrice} zł`)
    },
    {
        id: "actions",
        cell: ({row}) => <ProductCellAction data={row.original}/>
    }
]