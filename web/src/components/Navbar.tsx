import {useEffect, useState} from "react";
import {ChevronDownIcon, MountainIcon, ShoppingBag,} from "lucide-react"
import {DropdownMenu, DropdownMenuContent, DropdownMenuTrigger,} from "@/components/ui/dropdown-menu"
import {Link} from "react-router-dom";
import {NavbarElement} from "@/components/NavbarElement.tsx";
import {Category} from "@/types.ts";
import axios from "axios";
import {ShoppingCartButton} from "@/components/ShoppingCartButton.tsx";

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
        <header className="flex items-center h-16 px-4 md:px-6 border-b gap-x-6 justify-between">
            <div className="flex items-center gap-x-6">
                <Link to="#" className="flex items-center gap-2 text-lg font-semibold">
                    <MountainIcon className="w-6 h-6"/>
                    <span className="sr-only">Acme Inc</span>
                </Link>
                {(!loading && categories) && (
                    <nav className="flex gap-4 sm:gap-6 text-sm font-medium">
                        {categories?.map(category => {
                            if (category.children && category.children.length > 0) {
                                return (
                                    <DropdownMenu key={category.id}>
                                        <DropdownMenuTrigger
                                            className="flex items-center gap-1 hover:underline underline-offset-4">
                                            {category.name}
                                            <ChevronDownIcon className="w-4 h-4"/>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent align="start" className="w-[200px]">
                                            {category.children.map((category) => <NavbarElement key={category.id}
                                                                                                category={category}/>)}
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                );
                            } else {
                                return (
                                    <Link key={category.id} to="#" className="hover:underline underline-offset-4">
                                        {category.name}
                                    </Link>
                                );
                            }
                        })}
                    </nav>
                )}
            </div>
            <ShoppingCartButton className="flex items-center rounded-full bg-black px-4 py-2">
                <ShoppingBag size={20} color="white"/>
                <span className="ml-2 text-sm font-medium text-white">
                    0
                </span>
            </ShoppingCartButton>
        </header>
    );
}

export default Navbar;