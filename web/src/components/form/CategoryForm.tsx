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
import axios from "axios";
import toast from "react-hot-toast";
import {Category} from "@/models/Category.ts";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {BillboardName} from "@/models/BillboardName.ts";
import {CategoryName} from "@/models/CategoryName.ts";

interface CategoryFormProps {
    initialData: Category | null;
    billboards: BillboardName[];
    categories: CategoryName[];
}

const formSchema = z.object({
    name: z.string().min(1),
    billboardId: z.number().min(1),
    categoryParentId: z.union([z.number().nullable(), z.string().transform(val => val === 'None' ? null : parseInt(val))])
});

type CategoryFormValues = z.infer<typeof formSchema>;

export const CategoryForm: React.FC<CategoryFormProps> = ({initialData, billboards, categories}) => {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const {authState} = useAuth();
    const params = useParams();


    const title = initialData ? "Edit category" : "Create category";
    const description = initialData ? "Edit a category" : "Add a new category";
    const toastMessage = initialData ? "Category updated." : "Category created.";
    const action = initialData ? "Save changes" : "Create";

    const form = useForm<CategoryFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: initialData?.name || '',
            billboardId: initialData?.billboardId || undefined,
            categoryParentId: initialData?.parentId || null
        }
    });

    const onSubmit = async (data: CategoryFormValues) => {
        setLoading(true);
        try {
            if (!initialData) {
                console.log(data);
                await axios.post<Category>(`http://localhost:8080/api/v1/categories?storeId=${params.storeId}`, data, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`
                    },
                });
            } else {
                await axios.patch<Category>(`http://localhost:8080/api/v1/categories/${params.categoryId}`, data, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`
                    },
                });
            }

            navigate(`/dashboard/${params.storeId}/categories`);
            toast.success(toastMessage);
        } catch (error: any) {
            if (!error.response) navigate("/");

            if (error.response.status === 409) {
                let errorMes = "Cannot set parent. This creates a cyclic dependency.";
                if (error.response.data.error && error.response.data.error === errorMes) {
                    form.setError("categoryParentId", {message: errorMes});
                } else {
                    form.setError("name", {message: "Category with this name already exist"})
                }
            }
        } finally {
            setLoading(false);
        }
    };

    const onDelete = async () => {
        try {
            setLoading(true);
            await axios.delete<string>(
                `http://localhost:8080/api/v1/categories/${params.billboardId}`,
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
            navigate(0);
            navigate(`/dashboard/${params.storeId}/categories`);
            toast.success('Billboard deleted.');
        } catch (error: any) {
            if (error.response && error.response.satus === 404) {
                navigate(`/dashboard/${params.storeId}/categories`);
            } else {
                toast.error('Server error');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <AlertModal
                isOpen={openDeleteModal}
                onClose={() => setOpenDeleteModal(false)}
                onConfirm={() => onDelete()}
                loading={loading}
            />
            <div className="flex items-center justify-between">
                <Heading
                    title={title}
                    description={description}
                />
                {initialData && (
                    <Button
                        disabled={loading}
                        variant="destructive"
                        size="icon"
                        onClick={() => setOpenDeleteModal(true)}
                    >
                        <Trash className="h-4 w-4"/>
                    </Button>
                )}
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
                                        <Input disabled={loading} placeholder="Category name" {...field}/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="billboardId"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Billboard</FormLabel>
                                    <Select
                                        disabled={loading}
                                        onValueChange={(value) => field.onChange(value === 'None' ? null : parseInt(value))}
                                        value={field.value ? field.value.toString() : 'None'}
                                        defaultValue={field.value ? field.value.toString() : 'None'}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue
                                                    defaultValue={field.value}
                                                    aria-placeholder="Select a billboard"
                                                />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {billboards.map((billboard: BillboardName) => (
                                                <SelectItem
                                                    key={billboard.id}
                                                    value={billboard.id.toString()}
                                                >
                                                    {billboard.label}
                                                </SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="categoryParentId"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Parent Category</FormLabel>
                                    <Select
                                        disabled={loading}
                                        onValueChange={(value) => field.onChange(value === 'None' ? null : parseInt(value))}
                                        value={field.value ? field.value.toString() : 'None'}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue
                                                    defaultValue={field.value ? field.value.toString() : 'None'}
                                                    aria-placeholder="Select a parent category"
                                                />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            <SelectItem value="None">None</SelectItem>
                                            {initialData ? (
                                                categories
                                                    .filter(category => parseInt(params.categoryId!) !== category.id)
                                                    .map((category: CategoryName) => (
                                                        <SelectItem
                                                            key={category.id}
                                                            value={category.id.toString()}
                                                        >
                                                            {category.name}
                                                        </SelectItem>
                                                    ))
                                            ) : (
                                                categories.map((category: CategoryName) => (
                                                    <SelectItem
                                                        key={category.id}
                                                        value={category.id.toString()}
                                                    >
                                                        {category.name}
                                                    </SelectItem>
                                                ))
                                            )}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button
                        disabled={loading}
                        className="ml-auto"
                        type="submit"
                    >
                        {action}
                    </Button>
                </form>
            </Form>
            <Separator/>
        </>
    );
};