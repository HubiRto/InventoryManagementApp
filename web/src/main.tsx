import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import ErrorPage from "@/pages/ErrorPage.tsx";
import ProtectedRouteWrapper from "@/wrapper/ProtectedRouteWrapper.tsx";
import LoginPage from "@/pages/auth/LoginPage.tsx";
import RegisterPage from "@/pages/auth/RegisterPage.tsx";
import {AuthProvider} from "@/providers/AuthContext.tsx";
import GuestRouteWrapper from "@/wrapper/GuestRouteWrapper.tsx";
import {ToastProvider} from "@/providers/ToastProvider.tsx";
import HomePage from "@/pages/HomePage.tsx";
import React from "react";
import {ModalProvider} from "@/providers/ModalProvider.tsx";
import FindFirstStoreWrapper from "@/wrapper/FindFirstStoreWrapper.tsx";
import StoreWrapper from "@/wrapper/StoreWrapper.tsx";
import DashboardPage from "@/pages/DashboardPage.tsx";
import SettingsPage from "@/pages/SettingsPage.tsx";
import BillboardsPage from "@/pages/billboard/BillboardsPage.tsx";
import BillboardPage from "@/pages/billboard/BillboardPage.tsx";
import CategoriesPage from "@/pages/category/CategoriesPage.tsx";
import CategoryPage from "@/pages/category/CategoryPage.tsx";


const wrapWithGuestRoute = (element: React.ReactNode) => <GuestRouteWrapper>{element}</GuestRouteWrapper>;
const wrapWithProtectedRoute = (element: React.ReactNode) => <ProtectedRouteWrapper>{element}</ProtectedRouteWrapper>;
const wrapWithStoreModalRoute = (element: React.ReactNode) => (<><ModalProvider/>{element}</>);
//Check if user have any store and if it has then redirected to first store in database
const wrapWithCheckStoresRoute = (element: React.ReactNode) => <FindFirstStoreWrapper>{element}</FindFirstStoreWrapper>;
//Protects against switching to a store that does not exist or does not belong to the user
const wrapWithStorRoute = (element: React.ReactNode) => (<StoreWrapper>{element}</StoreWrapper>);

const composeWrappers = (wrappers: ((element: React.ReactNode) => React.ReactNode)[], element: React.ReactNode) =>
    wrappers.reduceRight((acc, wrapper) => wrapper(acc), element);


const routesConfig = [
    {
        path: "/",
        element: <HomePage/>,
        wrappers: [wrapWithProtectedRoute, wrapWithStoreModalRoute, wrapWithCheckStoresRoute],
        errorElement: <ErrorPage/>
    },
    {
        path: "/dashboard/:storeId",
        element: <DashboardPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {
        path: "/dashboard/:storeId/settings",
        element: <SettingsPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {
        path: "/dashboard/:storeId/billboards",
        element: <BillboardsPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {
        path: "/dashboard/:storeId/billboards/:billboardId",
        element: <BillboardPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {
        path: "/dashboard/:storeId/categories",
        element: <CategoriesPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {
        path: "/dashboard/:storeId/categories/:categoryId",
        element: <CategoryPage/>,
        wrappers: [
            wrapWithProtectedRoute,
            wrapWithStoreModalRoute,
            wrapWithStorRoute
        ]
    },
    {path: "/login", element: <LoginPage/>, wrappers: [wrapWithGuestRoute]},
    {path: "/register", element: <RegisterPage/>, wrappers: [wrapWithGuestRoute]},
];


const routes = routesConfig.map(({path, element, wrappers, errorElement}) => ({
    path,
    element: composeWrappers(wrappers, element),
    errorElement
}));

const router = createBrowserRouter(routes);

ReactDOM.createRoot(document.getElementById('root')!).render(
    // <React.StrictMode>
    <AuthProvider>
        <ToastProvider/>
        <RouterProvider router={router}/>
    </AuthProvider>
    // </React.StrictMode>,
)
