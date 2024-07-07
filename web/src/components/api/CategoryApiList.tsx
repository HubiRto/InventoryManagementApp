import React from "react";
import {ApiAlert} from "@/components/api/ApiAlert.tsx";

interface ApiListProps {
    entityName: string;
    entityIdName: string;
}

export const CategoryApiList: React.FC<ApiListProps> = ({entityName, entityIdName}) => {
    const baseUrl = "http://localhost:8080/api/v1"

    return (
        <>
            <ApiAlert title="GET" variant="public" address={`${baseUrl}/${entityName}`} description="Retrive a list of all categories"/>
            <ApiAlert title="GET" variant="public" address={`${baseUrl}/${entityName}/{${entityIdName}}`} description="Retrive a scepific category by ID"/>
            <ApiAlert title="POST" variant="authenticated" address={`${baseUrl}/${entityName}`} description="Create a new category"/>
            <ApiAlert title="PATCH" variant="authenticated" address={`${baseUrl}/${entityName}`} description="Update a existing category by ID"/>
            <ApiAlert title="DELETE" variant="authenticated" address={`${baseUrl}/${entityName}/{${entityIdName}}`} description="Delete a category by ID"/>
        </>
    );
}