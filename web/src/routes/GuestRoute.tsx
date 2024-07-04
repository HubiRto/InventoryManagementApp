// src/routes/GuestRoute.tsx
import React from 'react';
import {useAuth} from "@/providers/AuthContext";
import {Navigate, useLocation} from "react-router-dom";
import {Spinner} from "@/components/Spinner.tsx";

const GuestRoute = ({children}: { children: React.ReactNode }) => {
    const location = useLocation();
    const {authState, isLoading} = useAuth();

    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <Spinner/>
            </div>
        );
    }

    if (authState?.token) {
        return <Navigate to="/" state={{ from: location }} />
    }

    return <>{children}</>
};

export default GuestRoute;