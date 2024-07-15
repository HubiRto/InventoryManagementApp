import {Store} from "@/models/Store.ts";
import React, {useState} from "react";
import {Heading} from "@/components/Heading.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Trash} from "lucide-react";
import {Separator} from "@/components/ui/separator.tsx";
import * as z from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useNavigate, useParams} from "react-router-dom";
import toast from "react-hot-toast";
import axios from "axios";
import {useAuth} from "@/providers/AuthContext.tsx";
import {AlertModal} from "@/components/modal/AlertModal.tsx";
import {ApiAlert} from "@/components/api/ApiAlert.tsx";

interface SettingsFormProps {
    initialData: Store;
}

const formSchema = z.object({
    name: z.string().min(3),
});

type SettingsFormValues = z.infer<typeof formSchema>;

export const SettingsForm: React.FC<SettingsFormProps> = ({
                                                              initialData
                                                          }) => {
    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {storeId} = useParams();
    const {authState} = useAuth();

    const form = useForm<SettingsFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: initialData
    });

    const onSubmit = async (data: SettingsFormValues) => {
        try {
            setLoading(true);
            await axios.patch<Store>(
                `http://localhost:8080/api/v1/stores/${storeId}`,
                {
                    newStoreName: data.name
                },
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
            navigate(0);
            // window.location.reload();
            toast.success('Store updated.');
        } catch (error: any) {
            if (error.response && error.response.satus === 404) {
                navigate('/');
            } else if (error.response && error.response.status === 400) {
                form.setError("name", {message: "Name is required"});
            } else {
                toast.error('Server error');
            }
        } finally {
            setLoading(false);
        }
    };

    const onDelete = async () => {
        try {
            setLoading(true);
            await axios.delete<string>(
                `http://localhost:8080/api/v1/stores/${storeId}`,
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
            // navigate(0);
            navigate('/');
            // window.location.reload();
            toast.success('Store deleted.');
        } catch (error: any) {
            if (error.response && error.response.satus === 404) {
                navigate('/');
            } else {
                toast.error('Server error');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <AlertModal isOpen={open} onClose={() => setOpen(false)} onConfirm={() => onDelete()} loading={loading}/>
            <div className="flex items-center justify-between">
                <Heading title="Settings" description="Manage store preferences"/>
                <Button disabled={loading} variant="destructive" size="icon" onClick={() => setOpen(true)}>
                    <Trash className="h-4 w-4"/>
                </Button>
            </div>
            <Separator/>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 w-full">
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="name"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Name</FormLabel>
                                    <FormControl>
                                        <Input disabled={loading} placeholder="Store name" {...field}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button disabled={loading} className="ml-auto" type="submit">
                        Save changes
                    </Button>
                </form>
            </Form>
            <Separator/>
            <ApiAlert title="PATCH" address={`http://localhost:8080/api/v1/stores/${storeId}`} variant="public" description=""/>
        </>
    );
};