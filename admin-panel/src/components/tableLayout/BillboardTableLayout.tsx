import {ColumnDef} from "@tanstack/react-table";
import {BillboardCellAction} from "@/components/cellAction/BillboardCellAction.tsx";

export type BillboardColumn = {
    id: number;
    label: string;
    createdAt: string;
}

export const columns: ColumnDef<BillboardColumn>[] = [
    {
        accessorKey: "label",
        header: "Label",
    },
    {
        accessorKey: "createdAt",
        header: "Date",
    },
    {
        id: "actions",
        cell: ({row}) => <BillboardCellAction data={row.original}/>
    }
]