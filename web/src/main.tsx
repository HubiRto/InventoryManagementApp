import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import HomePage from "@/pages/HomePage.tsx";
import ErrorPage from "@/pages/ErrorPage.tsx";

const routesConfig = [
    {
        path: "/",
        element: <HomePage/>,
        wrappers: [],
        errorElement: <ErrorPage/>
    }
];

const composeWrappers = (wrappers: ((element: React.ReactNode) => React.ReactNode)[], element: React.ReactNode) =>
    wrappers.reduceRight((acc, wrapper) => wrapper(acc), element);

const routes = routesConfig.map(({path, element, wrappers, errorElement}) => ({
    path,
    element: composeWrappers(wrappers, element),
    errorElement
}));

const router = createBrowserRouter(routes);

ReactDOM.createRoot(document.getElementById('root')!).render(
    // <React.StrictMode>
        <RouterProvider router={router}/>
    // </React.StrictMode>,
)
