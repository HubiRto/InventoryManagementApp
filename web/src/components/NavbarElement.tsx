import {Category} from "@/types.ts";
import React from "react";
import {
    DropdownMenuItem,
    DropdownMenuPortal,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Link} from "react-router-dom";

interface NavbarElementProps {
    category: Category;
}

export const NavbarElement: React.FC<NavbarElementProps> = ({category}) => {
    if (category.children && category.children.length > 0) {
        return (
            <DropdownMenuSub key={category.id}>
                <DropdownMenuSubTrigger className="flex items-center gap-1">
                    {category.name}
                    {/*<ChevronDownIcon className="w-4 h-4 ml-2"/>*/}
                </DropdownMenuSubTrigger>
                <DropdownMenuPortal>
                    <DropdownMenuSubContent className="w-[200px]">
                        {category.children.map((child) => <NavbarElement category={child}/>)}
                    </DropdownMenuSubContent>
                </DropdownMenuPortal>
            </DropdownMenuSub>
        );
    }
    return (
        <DropdownMenuItem key={category.id}>
            <Link to="#" className="flex items-center gap-2">
                {category.name}
            </Link>
        </DropdownMenuItem>
    );
}