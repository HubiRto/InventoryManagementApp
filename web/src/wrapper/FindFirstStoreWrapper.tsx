// src/wrapper/FindFirstStoreWrapper.tsx

import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import axios from "axios";
import {Store} from "@/models/Store.ts";

interface CheckStoresProps {
    children: React.ReactNode;
}

const FindFirstStoreWrapper: React.FC<CheckStoresProps> = ({children}) => {
    const navigate = useNavigate();
    const {authState, user} = useAuth();

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

                if (response.data.length > 0) {
                    navigate(`/dashboard/${response.data[0].id}`);
                }
            } catch (error) {
                navigate('/login');
            }
        };

        checkStores();
    }, [navigate, authState]);

    return <>{children}</>;
};

export default FindFirstStoreWrapper;