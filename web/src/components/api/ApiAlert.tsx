import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert.tsx";
import {Copy, Server} from "lucide-react";
import React from "react";
import {Badge, BadgeProps} from "@/components/ui/badge.tsx";
import {Button} from "@/components/ui/button.tsx";
import toast from "react-hot-toast";

interface ApiAlertProps {
    title: string;
    address: string;
    description: string;
    variant: "public" | "authenticated";
}

const textMap: Record<ApiAlertProps["variant"], string> = {
    public: "Public",
    authenticated: "Authenticated"
};

const variantMap: Record<ApiAlertProps["variant"], BadgeProps["variant"]> = {
    public: "secondary",
    authenticated: "destructive"
};

export const ApiAlert: React.FC<ApiAlertProps> = ({
                                                      title, description, address, variant = "public"
                                                  }) => {
    const onCopy = () => {
        navigator.clipboard.writeText(description);
        toast.success("API Route copied to the clipboard")
    }

    return (
        <Alert>
            <Server className="h-4 w-4 my-2"/>
            <AlertTitle className="flex items-center gap-x-2">
                <span
                    className={`px-3 py-1 rounded-full text-sm font-medium ${
                        title === "GET"
                            ? "bg-green-100 text-green-800 dark:bg-green-800 dark:text-green-100"
                            : title === "PATCH"
                                ? "bg-blue-100 text-blue-800 dark:bg-blue-800 dark:text-blue-100"
                                : title === "POST"
                                    ? "bg-yellow-100 text-yellow-800 dark:bg-yellow-800 dark:text-yellow-100"
                                    : "bg-red-100 text-red-800 dark:bg-red-800 dark:text-red-100"
                    }`}
                >
                {title}
              </span>
                <Badge className="px-3 py-1 font-medium text-sm" variant={variantMap[variant]}>
                    {textMap[variant]}
                </Badge>
            </AlertTitle>
            <AlertDescription className="mt-4 flex items-center justify-between">
                <div className="flex-col items-center">
                    <code className="relative rounded bg-muted px-[0.3rem] py-[0.2rem] font-mono text-sm font-semibold">
                        {address}
                    </code>
                    <p className="text-gray-600 dark:text-gray-400 mt-3 ml-1">{description}</p>
                </div>
                <Button variant="outline" size="icon" onClick={() => onCopy()}>
                    <Copy className="h-4 w-4"/>
                </Button>
            </AlertDescription>
        </Alert>
    );
}