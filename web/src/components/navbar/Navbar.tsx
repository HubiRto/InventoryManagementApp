import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {useAuth} from "@/providers/AuthContext.tsx";
import {MainNav} from "@/components/navbar/MainNav.tsx";
import {useEffect, useState} from "react";
import {Store} from "@/models/Store.ts";
import axios from "axios";
import toast from "react-hot-toast";
import {Spinner} from "@/components/Spinner.tsx";
import StoreSwitcher from "@/components/navbar/StoreSwtcher.tsx";

const Navbar = () => {
    const {onLogout, user, authState} = useAuth();
    const [stores, setStores] = useState<Store[]>();

    useEffect(() => {
        const checkStores = async () => {
            try {
                const response = await axios.get<Store[]>(
                    `http://localhost:8080/api/v1/stores?userId=${user?.id}`,
                    {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                    });
                setStores(response.data)
            } catch (error: any) {
                toast.error('Loading error')
            }
        };

        checkStores();
    }, [user]);

    return (
        <div className="border-b">
            <div className="flex h-16 items-center px-4">
                <StoreSwitcher items={stores!}/>
                <MainNav className="mx-6"/>
                <div className="ml-auto flex items-center space-x-4">
                    <Avatar onClick={onLogout}>
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn"/>
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                </div>
            </div>
        </div>
    );
}
export default Navbar;