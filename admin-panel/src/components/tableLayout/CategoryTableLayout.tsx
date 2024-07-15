import {ColumnDef} from "@tanstack/react-table";
import {CategoryCellAction} from "@/components/cellAction/CategoryCellAction.tsx";
import {Button} from "@/components/ui/button.tsx";
import {ArrowUpDown} from "lucide-react";

export type CategoryColumn = {
    id: number;
    name: string;
    billboardLabel: string;
    createdAt: string;
}

export const columns: ColumnDef<CategoryColumn>[] = [
    {
        accessorKey: "name",
        header: "Name",
    },
    {
        accessorKey: "billboard",
        header: "Billboard",
        cell: ({row}) => row.original.billboardLabel
    },
    {
        accessorKey: "createdAt",
        header: ({ column }) => {
            return (
                <Button
                    variant="ghost"
                    onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                >
                    Created At
                    <ArrowUpDown className="ml-2 h-4 w-4" />
                </Button>
            )
        },
    },
    {
        id: "actions",
        cell: ({row}) => <CategoryCellAction data={row.original}/>
    }
]