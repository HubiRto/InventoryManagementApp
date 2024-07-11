// src/wrapper/FindFirstStoreWrapper.tsx

import React, {useEffect} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {Store} from "@/models/Store.ts";
import Navbar from "@/components/navbar/Navbar.tsx";

interface StoreProps {
    children: React.ReactNode;
}

const StoreWrapper: React.FC<StoreProps> = ({children}) => {
    const navigate = useNavigate();
    const {authState} = useAuth();
    const params = useParams();

    useEffect(() => {
        const checkStore = async () => {
            try {
                await axios.get<Store>(`http://localhost:8080/api/v1/stores/${params.storeId}`);
            } catch (error: any) {
                //If store does not exist then navigate to dashboard
                navigate('/');
            }
        };

        checkStore();
    }, [navigate, authState]);

    if (!params.storeId) {
        navigate('/');
    }

    return (
        <>
            <Navbar/>
            {children}
        </>
    );
};

export default StoreWrapper;