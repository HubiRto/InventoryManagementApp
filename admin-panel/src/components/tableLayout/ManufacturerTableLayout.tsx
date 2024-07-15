import {ColumnDef} from "@tanstack/react-table";
import {Button} from "@/components/ui/button.tsx";
import {ArrowUpDown} from "lucide-react";
import {ManufacturerCellAction} from "@/components/cellAction/ManufacturerCellAction.tsx";
import {Link} from "react-router-dom";

export type ManufacturerColumn = {
    id: number;
    name: string;
    website: string;
    createdAt: string;
}

export const columns: ColumnDef<ManufacturerColumn>[] = [
    {
        accessorKey: "name",
        header: "Name",
    },
    {
        accessorKey: "website",
        header: "Website",
        cell: ({row}) => <Link className="text-blue-400 font-bold" to={row.original.website}>{row.original.website}</Link>
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
        cell: ({row}) => <ManufacturerCellAction data={row.original}/>
    }
]