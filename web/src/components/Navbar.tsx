import {useEffect, useState} from "react";
import {ChevronDownIcon, MountainIcon,} from "lucide-react"
import {DropdownMenu, DropdownMenuContent, DropdownMenuTrigger,} from "@/components/ui/dropdown-menu"
import {Link} from "react-router-dom";
import {NavbarElement} from "@/components/NavbarElement.tsx";
import {Category} from "@/types.ts";
import axios from "axios";

const Navbar = () => {
    const [categories, setCategories] = useState<Category[] | undefined>(undefined);
    const [loading, setLoading] = useState<boolean>();

    useEffect(() => {
        setLoading(true);
        const fetchCategories = async () => {
            try {
                const response = await axios.get<Category[]>(
                    `http://localhost:8080/api/v1/categories?storeId=1`);
                setCategories(response.data);
            } catch (error: any) {
                console.log(error);
            } finally {
                setLoading(false);
            }
        }

        fetchCategories();
    }, []);

    return (
        <header className="flex items-center h-16 px-4 md:px-6 border-b gap-x-6">
            <Link to="#" className="flex items-center gap-2 text-lg font-semibold">
                <MountainIcon className="w-6 h-6"/>
                <span className="sr-only">Acme Inc</span>
            </Link>
            {(!loading && categories) && (
                <nav className="flex gap-4 sm:gap-6 text-sm font-medium">
                    {categories?.map(category => {
                        if (category.children && category.children.length > 0) {
                            return (
                                <DropdownMenu>
                                    <DropdownMenuTrigger
                                        className="flex items-center gap-1 hover:underline underline-offset-4">
                                        {category.name}
                                        <ChevronDownIcon className="w-4 h-4"/>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="start" className="w-[200px]">
                                        {category.children.map((category) => <NavbarElement category={category}/>)}
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            );
                        } else {
                            return (
                                <Link to="#" className="hover:underline underline-offset-4">
                                    {category.name}
                                </Link>
                            );
                        }
                    })}
                </nav>
            )}
        </header>
    );
}

export default Navbar;