import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {format} from "date-fns";
import {Manufacturer} from "@/models/Manufacturer.ts";
import {ManufacturerColumn} from "@/components/tableLayout/ManufacturerTableLayout.tsx";
import {ManufacturerClient} from "@/components/client/ManufacturerClient.tsx";

const ManufacturersPage = () => {
    const [manufacturers, setManufacturers] = useState<Manufacturer[] | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {storeId} = useParams();
    const {authState} = useAuth();

    useEffect(() => {
        setLoading(true);
        const fetchBillboards = async () => {
            try {
                const response = await axios.get<Manufacturer[]>(
                    `http://localhost:8080/api/v1/producents?storeId=${storeId}`);
                setManufacturers(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchBillboards();
    }, [storeId, authState]);

    const formattedBillboardsToColumns = (manufacturers: Manufacturer[]): ManufacturerColumn[] => {
        return manufacturers.map((item) => ({
            id: item.id,
            name: item.name,
            website: item.website,
            createdAt: format(item.createdAt, "MMMM do, yyyy")
        }));
    }

    if (!loading && manufacturers) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <ManufacturerClient data={formattedBillboardsToColumns(manufacturers)}/>
                </div>
            </div>
        );
    }
}
export default ManufacturersPage;