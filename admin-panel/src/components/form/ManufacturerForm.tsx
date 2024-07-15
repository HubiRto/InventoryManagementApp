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
import {useAuth} from "@/providers/AuthContext.tsx";
import {AlertModal} from "@/components/modal/AlertModal.tsx";
import {Manufacturer} from "@/models/Manufacturer.ts";
import axios from "axios";
import toast from "react-hot-toast";

interface ManufacturerFormProps {
    initialData: Manufacturer | null;
}

const formSchema = z.object({
    name: z.string().min(1),
    description: z.string().optional(),
    website: z.string().url().optional(),
});

type ManufacturerFormValues = z.infer<typeof formSchema>;

export const ManufacturerForm: React.FC<ManufacturerFormProps> = ({ initialData }) => {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { authState } = useAuth();
    const params = useParams();

    const title = initialData ? "Edit Manufacturer" : "Create Manufacturer";
    const description = initialData ? "Edit a manufacturer" : "Add a new manufacturer";
    const toastMessage = initialData ? "Manufacturer updated." : "Manufacturer created.";
    const action = initialData ? "Save changes" : "Create";

    const form = useForm<ManufacturerFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: initialData?.name || '',
            description: initialData?.description || '',
            website: initialData?.website || '',
        }
    });

    const handleSubmit = async (data: ManufacturerFormValues) => {
        setLoading(true);
        if (initialData === null) {
            try {
                await axios.post<Manufacturer>(`http://localhost:8080/api/v1/producents?storeId=${params.storeId}`, data, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
                navigate(`/dashboard/${params.storeId}/manufacturers`);
                toast.success(toastMessage);
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate('/');
                } else if (error.response && error.response.status === 409) {
                    if (error.response.data.error.includes('name')) {
                        form.setError('name', { message: 'Manufacturer with this name already exists' });
                    }
                } else {
                    toast.error("Failed to create manufacturer.");
                }
            } finally {
                setLoading(false);
            }
        } else {
            try {
                await axios.patch<Manufacturer>(`http://localhost:8080/api/v1/producents/${initialData.id}`, data, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
                navigate(`/dashboard/${params.storeId}/manufacturers`);
                toast.success(toastMessage);
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate(`/dashboard/${params.storeId}/manufacturers`);
                } else {
                    console.log(error);
                    toast.error("Failed to update manufacturer.");
                }
            } finally {
                setLoading(false);
            }
        }
    };

    const onDelete = async () => {
        try {
            setLoading(true);
            await axios.delete<string>(`http://localhost:8080/api/v1/producents/${params.manufacturerId}`, {
                headers: {
                    Authorization: `Bearer ${authState?.token}`,
                },
            });
            navigate(`/dashboard/${params.storeId}/manufacturers`);
            toast.success('Manufacturer deleted.');
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                navigate(`/dashboard/${params.storeId}/manufacturers`);
            } else {
                toast.error('Server error');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <AlertModal isOpen={openDeleteModal} onClose={() => setOpenDeleteModal(false)} onConfirm={() => onDelete()} loading={loading} />
            <div className="flex items-center justify-between">
                <Heading title={title} description={description} />
                {initialData && (
                    <Button disabled={loading} variant="destructive" size="icon" onClick={() => setOpenDeleteModal(true)}>
                        <Trash className="h-4 w-4" />
                    </Button>
                )}
            </div>
            <Separator />
            <Form {...form}>
                <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-8 w-full">
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="name"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Name</FormLabel>
                                    <FormControl>
                                        <Input disabled={loading} placeholder="Manufacturer name" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="description"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Description</FormLabel>
                                    <FormControl>
                                        <Input disabled={loading} placeholder="Manufacturer description" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="website"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Website</FormLabel>
                                    <FormControl>
                                        <Input disabled={loading} placeholder="Manufacturer website" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button disabled={loading} className="ml-auto" type="submit">
                        {action}
                    </Button>
                </form>
            </Form>
            <Separator />
        </>
    );
};
