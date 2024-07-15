import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {useAuth} from "@/providers/AuthContext.tsx";
import {MainNav} from "@/components/navbar/MainNav.tsx";
import {useEffect, useState} from "react";
import {Store} from "@/models/Store.ts";
import axios from "axios";
import toast from "react-hot-toast";
import StoreSwitcher from "@/components/navbar/StoreSwtcher.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Menu} from "lucide-react";
import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet.tsx";

const Navbar = () => {
    const { onLogout, user } = useAuth();
    const [stores, setStores] = useState<Store[]>();

    useEffect(() => {
        const checkStores = async () => {
            try {
                const response = await axios.get<Store[]>(
                    `http://localhost:8080/api/v1/stores?userId=${user?.id}`
                );
                setStores(response.data);
            } catch (error: any) {
                toast.error('Loading error');
            }
        };

        checkStores();
    }, [user]);

    return (
        <div className="border-b">
            <div className="flex h-16 items-center px-4">
                <div className="flex-1 flex items-center gap-6 md:gap-5 lg:gap-6">
                    <StoreSwitcher items={stores!} />
                    <div className="hidden md:flex">
                        <MainNav className="mx-6" />
                    </div>
                </div>
                <div className="ml-auto flex items-center space-x-4">
                    <Avatar onClick={onLogout}>
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <Sheet>
                        <SheetTrigger asChild>
                            <Button
                                variant="outline"
                                size="icon"
                                className="shrink-0 md:hidden"
                            >
                                <Menu className="h-5 w-5" />
                                <span className="sr-only">Toggle navigation menu</span>
                            </Button>
                        </SheetTrigger>
                        <SheetContent className="w-full h-full p-4 flex flex-col" side="left">
                            <MainNav orientation="vertical" className="mt-4 font-medium text-lg gap-6" />
                        </SheetContent>
                    </Sheet>
                </div>
            </div>
        </div>
    );
}

export default Navbar;
