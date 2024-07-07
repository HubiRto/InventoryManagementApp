import {useEffect, useState} from "react";
import axios from "axios";
import {Store} from "@/models/Store.ts";
import {useAuth} from "@/providers/AuthContext.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {Spinner} from "@/components/Spinner.tsx";

const DashboardPage = () => {
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

    if (loading) {
        return <Spinner/>
    }

    return (
        <div>
            {/*<div>{store?.id}</div>*/}
            {/*<div>{store?.name}</div>*/}
            {/*<div>{store?.userId}</div>*/}
            {/*<div>{store?.createdAt}</div>*/}
            {/*<div>{store?.updatedAt}</div>*/}
            <div className={`transition-opacity duration-1000`}>
                <h1>{store?.name}</h1>
                {/* Render other store-related content here */}
            </div>
        </div>
    );
}
export default DashboardPage;