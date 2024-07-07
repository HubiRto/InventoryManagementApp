import {BillboardClient} from "@/components/client/BillboardClient.tsx";
import {useEffect, useState} from "react";
import {Billboard} from "@/models/Billboard.ts";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {BillboardColumn} from "@/components/tableLayout/BillboardTableLayout.tsx";
import {format} from "date-fns";

const BillboardsPage = () => {
    const [billboards, setBillboards] = useState<Billboard[] | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {storeId} = useParams();
    const {authState} = useAuth();

    useEffect(() => {
        setLoading(true);
        const fetchBillboards = async () => {
            try {
                const response = await axios.get<Billboard[]>(
                    `http://localhost:8080/api/v1/billboards?storeId=${storeId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                    }
                );
                setBillboards(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchBillboards();
    }, [storeId, authState]);

    const formattedBillboardsToColumns = (billboards: Billboard[]): BillboardColumn[] => {
        return billboards.map((item) => ({
            id: item.id,
            label: item.label,
            createdAt: format(item.createdAt, "MMMM do, yyyy")
        }));
    }

    if(!loading && billboards) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <BillboardClient data={formattedBillboardsToColumns(billboards)}/>
                </div>
            </div>
        );
    }
}
export default BillboardsPage;