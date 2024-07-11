import {ColumnDef} from "@tanstack/react-table";
import {CategoryCellAction} from "@/components/cellAction/CategoryCellAction.tsx";

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
        header: "Date",
    },
    {
        id: "actions",
        cell: ({row}) => <CategoryCellAction data={row.original}/>
    }
]