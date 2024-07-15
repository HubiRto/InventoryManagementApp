import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Category } from '@/types.ts';
import {ChevronDown, ChevronRight} from "lucide-react";

interface DropdownMenuProps {
    category: Category;
    level: number;
}

const DropdownMenu: React.FC<DropdownMenuProps> = ({ category, level }) => {
    const [isExpanded, setIsExpanded] = useState(false);
    const location = useLocation();
    const isActive = location.pathname === `/category/${category.id}`;
    const hasChildren = category.children && category.children.length > 0;

    const toggleExpand = () => setIsExpanded(!isExpanded);

    return (
        <div className={`ml-${level * 4}`}>
            <div className="flex items-center space-x-2">
                <Link
                    to={`/category/${category.id}`}
                    className={`text-sm font-medium transition-colors hover:text-black ${isActive ? 'text-black' : 'text-neutral-500'}`}
                >
                    {category.name}
                </Link>
                {hasChildren && (
                    <button onClick={toggleExpand} className="focus:outline-none">
                        {isExpanded ? <ChevronDown /> : <ChevronRight />}
                    </button>
                )}
            </div>
            {hasChildren && isExpanded && (
                <div className="ml-4">
                    {category.children?.map((child) => (
                        <DropdownMenu key={child.id} category={child} level={level + 1} />
                    ))}
                </div>
            )}
        </div>
    );
};

export default DropdownMenu;
