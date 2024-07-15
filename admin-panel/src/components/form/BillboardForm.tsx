import React, {useEffect, useState} from "react";
import {Heading} from "@/components/Heading.tsx";
import {Button} from "@/components/ui/button.tsx";
import {ImagePlus, Trash} from "lucide-react";
import {Separator} from "@/components/ui/separator.tsx";
import * as z from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {useAuth} from "@/providers/AuthContext.tsx";
import {AlertModal} from "@/components/modal/AlertModal.tsx";
import {Billboard} from "@/models/Billboard.ts";
import axios from "axios";
import toast from "react-hot-toast";
import {UploadImageModal} from "@/components/modal/UploadImageModal.tsx";

interface BillboardFormProps {
    initialData: Billboard | null;
}

const formSchema = z.object({
    label: z.string().min(1),
    imageFile: z.union([
        z.instanceof(File),
        z.string().url("Invalid image URL").optional()
    ]).refine(value => value !== undefined, "Image is required"),
});

type BillboardFormValues = z.infer<typeof formSchema>;

export const BillboardForm: React.FC<BillboardFormProps> = ({initialData}) => {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [openUploadModal, setOpenUploadModal] = useState(false);

    const [previewImage, setPreviewImage] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {authState} = useAuth();
    const params = useParams();


    const title = initialData ? "Edit billboard" : "Create billboard";
    const description = initialData ? "Edit a billboard" : "Add a new billboard";
    const toastMessage = initialData ? "Billboard updated." : "Billboard created.";
    const action = initialData ? "Save changes" : "Create";

    const form = useForm<BillboardFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            label: initialData?.label || '',
            imageFile: initialData ? initialData?.imageUrl : undefined,
        }
    });

    useEffect(() => {
        if (initialData?.imageUrl) {
            const fetchImage = async () => {
                try {
                    const response = await axios.get(initialData.imageUrl, {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                        },
                        responseType: 'blob',
                    });
                    const imageUrl = URL.createObjectURL(response.data);
                    setPreviewImage(imageUrl);
                } catch (error) {
                    console.error('Failed to fetch image', error);
                }
            };
            fetchImage();
        }
    }, [initialData]);

    const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewImage(reader.result as string);
            };
            reader.readAsDataURL(file);
            form.setValue('imageFile', file);
            form.clearErrors("imageFile");
            setOpenUploadModal(false);
        }
    };

    const handleRemoveImage = () => {
        setPreviewImage(null);
    };

    const handleSubmit = async (data: BillboardFormValues) => {
        if (!previewImage) {
            form.setError("imageFile", {message: "Image is required"});
            return;
        }

        setLoading(true);
        if (initialData === null) {
            try {
                const formData = new FormData();
                formData.append('storeId', params.storeId as string);
                formData.append('label', data.label);
                if (data.imageFile instanceof File) {
                    formData.append('image', data.imageFile);
                }

                await axios.post<Billboard>("http://localhost:8080/api/v1/billboards", formData, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                        'Content-Type': 'multipart/form-data',
                    },
                });
                navigate(`/dashboard/${params.storeId}/billboards`);
                toast.success(toastMessage);
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate('/');
                } else if (error.response && error.response.status === 409) {
                    if(error.response.data.error.includes('label')) {
                        form.setError('label', {message: 'Billboard with this label already exist'})
                    }
                } else {
                    toast.error("Failed to create billboard.");
                }
            } finally {
                setLoading(false);
            }
        } else {
            try {
                const formData = new FormData();
                formData.append('label', data.label);
                if (data.imageFile instanceof File) {
                    formData.append('image', data.imageFile);
                }

                await axios.patch<Billboard>(`http://localhost:8080/api/v1/billboards/${initialData.id}`, formData, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                        'Content-Type': 'multipart/form-data',
                    },
                });
                navigate(`/dashboard/${params.storeId}/billboards`);
                toast.success(toastMessage);
            } catch (error: any) {
                if (error.response && error.response.status === 404) {
                    navigate(`/dashboard/${params.storeId}/billboards`);
                } else {
                    console.log(error);
                    toast.error("Failed to update billboard.");
                }
            } finally {
                setLoading(false);
            }
        }
    };

    const onDelete = async () => {
        try {
            setLoading(true);
            await axios.delete<string>(
                `http://localhost:8080/api/v1/billboards/${params.billboardId}`,
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
            navigate(`/dashboard/${params.storeId}/billboards`);
            toast.success('Billboard deleted.');
        } catch (error: any) {
            if (error.response && error.response.satus === 404) {
                navigate(`/dashboard/${params.storeId}/billboards`);
            } else {
                toast.error('Server error');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <UploadImageModal isOpen={openUploadModal} onClose={() => setOpenUploadModal(false)}
                              onImageChange={handleImageChange}/>
            <AlertModal isOpen={openDeleteModal} onClose={() => setOpenDeleteModal(false)} onConfirm={() => onDelete()}
                        loading={loading}/>
            <div className="flex items-center justify-between">
                <Heading title={title} description={description}/>
                {initialData && (
                    <Button disabled={loading} variant="destructive" size="icon"
                            onClick={() => setOpenDeleteModal(true)}>
                        <Trash className="h-4 w-4"/>
                    </Button>
                )}
            </div>
            <Separator/>
            <Form {...form}>
                <form onSubmit={() => {
                }} className="space-y-8 w-full">
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="imageFile"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Background image</FormLabel>
                                    {previewImage && (
                                        <div
                                            className="relative w-[200px] h-[200px] rounded-md overflow-hidden" {...field}>
                                            <div className="z-10 absolute top-2 right-2">
                                                <Button type="button" onClick={() => handleRemoveImage()}
                                                        variant="destructive"
                                                        size="icon">
                                                    <Trash className="h-4 w-4"/>
                                                </Button>
                                            </div>
                                            <img
                                                className="object-cover w-full h-full"
                                                alt="Image"
                                                src={previewImage}
                                            />
                                        </div>
                                    )}
                                    <FormControl>
                                        <Button
                                            className="flex"
                                            type="button"
                                            variant="secondary"
                                            onClick={() => setOpenUploadModal(true)}
                                        >
                                            <ImagePlus className="h-4 w-4 mr-2"/>
                                            Upload an Image
                                        </Button>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="grid grid-cols-3 gap-8">
                        <FormField
                            control={form.control}
                            name="label"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Label</FormLabel>
                                    <FormControl>
                                        <Input disabled={loading} placeholder="Billboard label" {...field}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button disabled={loading} onClick={form.handleSubmit(handleSubmit)} className="ml-auto"
                            type="submit">
                        {action}
                    </Button>
                </form>
            </Form>
            <Separator/>
        </>
    );
};