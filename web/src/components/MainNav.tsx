import React from "react";
import {Category} from "@/types.ts";
import DropdownMenu from "@/components/DropdownMenu.tsx";

interface MainNavProps {
    data: Category[];
}

const MainNav: React.FC<MainNavProps> = ({data}) => {
    return (
        <nav className="mx-6 flex space-x-4">
            {data.map((category) => (
                <DropdownMenu key={category.id} category={category} level={0} />
            ))}
        </nav>
    );
}

export default MainNav;