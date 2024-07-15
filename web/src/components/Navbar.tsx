import React, {useState} from "react";
import {Category} from "@/types.ts";
import {
    ChevronDownIcon,
    Cloud, CodeIcon,
    CreditCard,
    Github,
    Keyboard,
    LifeBuoy,
    LogOut,
    Mail,
    MessageSquare, MountainIcon,
    Plus,
    PlusCircle,
    Settings, SmartphoneIcon, TypeIcon,
    User,
    UserPlus,
    Users,
} from "lucide-react"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuPortal,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger, DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem,
    NavigationMenuList,
    NavigationMenuTrigger
} from "@/components/ui/navigation-menu.tsx";
import {Link} from "react-router-dom";


const categories: Category[] = [
    {
        id: 1,
        name: "Zaprawy",
        createdAt: "12.03.2004",
        updatedAt: "12.03.2004",
        billboardId: 1,
        billboardLabel: "abc",
        parentId: null,
        children: [
            {
                id: 2,
                name: "Zaprawy tynkarskie",
                createdAt: "12.03.2004",
                updatedAt: "12.03.2004",
                billboardId: 1,
                billboardLabel: "abc",
                parentId: 1,
                children: [
                    {
                        id: 3,
                        name: "Zaprawy tynkarsko-murarskie",
                        createdAt: "12.03.2004",
                        updatedAt: "12.03.2004",
                        billboardId: 1,
                        billboardLabel: "abc",
                        parentId: 2,
                        children: []
                    }
                ]
            },
            {
                id: 4,
                name: "Zaprawy murarskie",
                createdAt: "12.03.2004",
                updatedAt: "12.03.2004",
                billboardId: 1,
                billboardLabel: "abc",
                parentId: 1,
                children: []
            }
        ]
    },
    {
        id: 5,
        name: "Zaprawy w",
        createdAt: "12.03.2004",
        updatedAt: "12.03.2004",
        billboardId: 1,
        billboardLabel: "abc",
        parentId: null,
        children: [
            {
                id: 5,
                name: "Zaprawy tynkarskie",
                createdAt: "12.03.2004",
                updatedAt: "12.03.2004",
                billboardId: 1,
                billboardLabel: "abc",
                parentId: 1,
                children: [
                    {
                        id: 7,
                        name: "Zaprawy tynkarsko-murarskie",
                        createdAt: "12.03.2004",
                        updatedAt: "12.03.2004",
                        billboardId: 1,
                        billboardLabel: "abc",
                        parentId: 2,
                        children: []
                    }
                ]
            },
            {
                id: 8,
                name: "Zaprawy murarskie",
                createdAt: "12.03.2004",
                updatedAt: "12.03.2004",
                billboardId: 1,
                billboardLabel: "abc",
                parentId: 1,
                children: []
            }
        ]
    },
];

const Navbar = () => {
    const [loading, setLoading] = useState<boolean>();

    // if (!loading && categories) {
    //     return (
    //         <div className="border-b">
    //             <Container>
    //                 <div className="relative px-4 sm:px-6 lg:px-8 flex h-16 items-center">
    //                     <Link to="/" className="ml-4 flex lg:ml-0 gap-x-2">
    //                         <p className="font-bold text-lg">STORE</p>
    //                     </Link>
    //                     <MainNav data={categories} />
    //                 </div>
    //             </Container>
    //         </div>
    //     );
    // }

    return (
        <header className="flex items-center h-16 px-4 md:px-6 border-b gap-x-6">
            <Link to="#" className="flex items-center gap-2 text-lg font-semibold">
                <MountainIcon className="w-6 h-6"/>
                <span className="sr-only">Acme Inc</span>
            </Link>
            <nav className="flex gap-4 sm:gap-6 text-sm font-medium">
                <Link to="#" className="hover:underline underline-offset-4">
                    Home
                </Link>
                <Link to="#" className="hover:underline underline-offset-4">
                    About
                </Link>
                <DropdownMenu>
                    <DropdownMenuTrigger className="flex items-center gap-1 hover:underline underline-offset-4">
                        Services
                        <ChevronDownIcon className="w-4 h-4"/>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="start" className="w-[200px]">
                        <DropdownMenuItem>
                            <Link to="#" className="flex items-center gap-2">
                                <CodeIcon className="w-4 h-4"/>
                                Web Development
                            </Link>
                        </DropdownMenuItem>
                        <DropdownMenuItem>
                            <Link to="#" className="flex items-center gap-2">
                                <SmartphoneIcon className="w-4 h-4"/>
                                Mobile Development
                            </Link>
                        </DropdownMenuItem>
                        <DropdownMenuSub>
                            <DropdownMenuSubTrigger>
                                <UserPlus className="mr-2 h-4 w-4" />
                                <span>Invite users</span>
                            </DropdownMenuSubTrigger>
                            <DropdownMenuPortal>
                                <DropdownMenuSubContent>
                                    <DropdownMenuItem>
                                        <Mail className="mr-2 h-4 w-4" />
                                        <span>Email</span>
                                    </DropdownMenuItem>
                                    <DropdownMenuSub>
                                        <DropdownMenuSubTrigger>
                                            <UserPlus className="mr-2 h-4 w-4" />
                                            <span>Invite users</span>
                                        </DropdownMenuSubTrigger>
                                        <DropdownMenuPortal>
                                            <DropdownMenuSubContent>
                                                <DropdownMenuItem>
                                                    <Mail className="mr-2 h-4 w-4" />
                                                    <span>Email</span>
                                                </DropdownMenuItem>
                                                <DropdownMenuItem>
                                                    <MessageSquare className="mr-2 h-4 w-4" />
                                                    <span>Message</span>
                                                </DropdownMenuItem>
                                                <DropdownMenuSeparator />
                                                <DropdownMenuItem>
                                                    <PlusCircle className="mr-2 h-4 w-4" />
                                                    <span>More...</span>
                                                </DropdownMenuItem>
                                            </DropdownMenuSubContent>
                                        </DropdownMenuPortal>
                                    </DropdownMenuSub>
                                    <DropdownMenuSeparator />
                                    <DropdownMenuItem>
                                        <PlusCircle className="mr-2 h-4 w-4" />
                                        <span>More...</span>
                                    </DropdownMenuItem>
                                </DropdownMenuSubContent>
                            </DropdownMenuPortal>
                        </DropdownMenuSub>
                        <DropdownMenuItem>
                            <Link to="#" className="flex items-center gap-2">
                                <TypeIcon className="w-4 h-4"/>
                                UI/UX Design
                            </Link>
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
                <Link to="#" className="hover:underline underline-offset-4">
                    Contact
                </Link>
            </nav>
        </header>
    );
}

export default Navbar;