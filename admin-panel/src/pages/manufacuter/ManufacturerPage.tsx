import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Manufacturer} from "@/models/Manufacturer.ts";
import {useAuth} from "@/providers/AuthContext.tsx";
import toast from "react-hot-toast";
import {ManufacturerForm} from "@/components/form/ManufacturerForm.tsx";

const ManufacturerPage = () => {
    const {storeId, manufacturerId} = useParams();
    const {authState} = useAuth();
    const navigate = useNavigate();
    const [manufacturer, setManufacturer] = useState<Manufacturer | null>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (manufacturerId !== "new") {
            const fetchManufacturer = async () => {
                try {
                    setLoading(true);
                    const response = await axios.get<Manufacturer>(
                        `http://localhost:8080/api/v1/producents/${manufacturerId}`
                    );
                    setManufacturer(response.data);
                } catch (error: any) {
                    if (error.response && error.response.status === 404) {
                        navigate(`/dashboard/${storeId}/manufacturers`);
                    } else {
                        toast.error("Server error.");
                    }
                } finally {
                    setLoading(false);
                }
            };
            fetchManufacturer();
        }
    }, [navigate, authState, manufacturerId, storeId]);

    if (!loading) {
        return (
            <div className="flex-col">
                <div className="flex-1 space-y-4 p-8 pt-6">
                    <ManufacturerForm initialData={manufacturer}/>
                </div>
            </div>
        );
    }
}

export default ManufacturerPage;
