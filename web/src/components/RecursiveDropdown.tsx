import {Category} from "@/types.ts";
import React, {useState} from "react";
import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem, NavigationMenuLink, NavigationMenuList,
    NavigationMenuTrigger, navigationMenuTriggerStyle
} from "@/components/ui/navigation-menu.tsx";
import {Link} from "react-router-dom";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {ChevronDown, ChevronRight} from "lucide-react";

interface RecursiveDropdownProps {
    category: Category;
    level: number;
}

const RecursiveDropdown: React.FC<RecursiveDropdownProps> = ({ category, level }) => {
    const hasChildren = category.children && category.children.length > 0;
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <div className={`ml-${level * 4} flex items-center space-x-2 cursor-pointer`}>
                    <span>{category.name}</span>
                    {hasChildren && (
                        <button onClick={() => setIsExpanded(!isExpanded)} className="focus:outline-none">
                            {isExpanded ? <ChevronDown /> : <ChevronRight />}
                        </button>
                    )}
                </div>
            </DropdownMenuTrigger>
            {hasChildren && isExpanded && (
                <DropdownMenuContent className="ml-4">
                    {category.children?.map((child) => (
                        <DropdownMenuItem key={child.id} asChild>
                            <RecursiveDropdown category={child} level={level + 1} />
                        </DropdownMenuItem>
                    ))}
                </DropdownMenuContent>
            )}
        </DropdownMenu>
    );
};

export default RecursiveDropdown;