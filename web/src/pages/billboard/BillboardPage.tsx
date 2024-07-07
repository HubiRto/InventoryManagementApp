import {BillboardForm} from "@/components/form/BillboardForm.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Billboard} from "@/models/Billboard.ts";
import {useAuth} from "@/providers/AuthContext.tsx";
import toast from "react-hot-toast";

const BillboardPage = () => {
    const {storeId, billboardId} = useParams();
    const {authState} = useAuth();
    const navigate = useNavigate();
    const [billboard, setBillboard] = useState<Billboard | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if(billboardId !== "new") {
            const fetchBillboard = async () => {
                try {
                    setLoading(true);
                    const response = await axios.get<Billboard>(
                        `http://localhost:8080/api/v1/billboards/${billboardId}`,
                        {
                            headers: {
                                Authorization: `Bearer ${authState?.token}`,
                            },
                        });
                    setBillboard(response.data)
                } catch (error: any) {
                    if (error.response && error.response.status === 404) {
                        navigate(`/dashboard/${storeId}/billboards`);
                    } else {
                        toast.error("Server error.");
                    }
                } finally {
                    setLoading(false);
                }
            };
            fetchBillboard();
        }
    }, [navigate, authState, billboardId, storeId]);

    if (!loading) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <BillboardForm initialData={billboard}/>
                </div>
            </div>
        );
    }
}
export default BillboardPage;