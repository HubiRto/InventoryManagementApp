// components/ProtectedRoute.tsx

import React from 'react';
import {useAuth} from "@/providers/AuthContext";
import {Navigate, useLocation} from "react-router-dom";
import {Spinner} from "@/components/Spinner.tsx";

const ProtectedRoute = ({children}: { children: React.ReactNode }) => {
    const location = useLocation();
    const {authState, isLoading} = useAuth();



    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <Spinner/>
            </div>
        );
    }



    if (!authState?.token) {
        return <Navigate to="/login" state={{from: location}}/>
    }

    return <>{children}</>
};

export default ProtectedRoute;
