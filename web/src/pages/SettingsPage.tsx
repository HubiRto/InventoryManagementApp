import {useEffect, useState} from "react";
import axios from "axios";
import {Store} from "@/models/Store.ts";
import {useAuth} from "@/providers/AuthContext.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {SettingsForm} from "@/components/form/SettingsForm.tsx";

const SettingsPage = () => {
    const [store, setStore] = useState<Store | undefined>(undefined);
    const [loading, setLoading] = useState(false);
    const {authState} = useAuth();
    const navigate = useNavigate();
    const { storeId } = useParams();

    useEffect(() => {
        setLoading(true);
        const checkStore = async () => {
            try {
                setLoading(true);
                const response = await axios.get<Store>(
                    `http://localhost:8080/api/v1/stores/${storeId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                    });
                setStore(response.data)
            } catch (error: any) {
                navigate('/')
            } finally {
                setLoading(false);
            }
        };

        checkStore();
    }, [navigate, authState, storeId]);

    if(!loading) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <SettingsForm initialData={store!}/>
                </div>
            </div>
        );
    }
}
export default SettingsPage;