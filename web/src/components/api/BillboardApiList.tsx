import React from "react";
import {ApiAlert} from "@/components/api/ApiAlert.tsx";

interface ApiListProps {
    entityName: string;
    entityIdName: string;
}

export const BillboardApiList: React.FC<ApiListProps> = ({entityName, entityIdName}) => {
    const baseUrl = "http://localhost:8080/api/v1"

    return (
        <>
            <ApiAlert title="GET" variant="public" address={`${baseUrl}/${entityName}`} description="Retrive a list of all billboards"/>
            <ApiAlert title="GET" variant="public" address={`${baseUrl}/${entityName}/{${entityIdName}}`} description="Retrive a scepific billboard by ID"/>
            <ApiAlert title="POST" variant="authenticated" address={`${baseUrl}/${entityName}?storeId={storeId}&label={label}&image{image}`} description="Create a new billboard"/>
            <ApiAlert title="PATCH" variant="authenticated" address={`${baseUrl}/${entityName}/{${entityIdName}}?label={label}&image{image}`} description="Update a existing billboard by ID"/>
            <ApiAlert title="DELETE" variant="authenticated" address={`${baseUrl}/${entityName}/{${entityIdName}}`} description="Delete a billboard by ID"/>
            <ApiAlert title="GET" variant="authenticated" address={`${baseUrl}/${entityName}/{${entityIdName}}/image`} description="Retrive a billboard image"/>
        </>
    );
}