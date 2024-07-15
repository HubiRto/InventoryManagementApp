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
import {ProductDetailsDTO} from "@/models/ProductDetailsDTO.ts";
import axios from "axios";
import toast from "react-hot-toast";
import {UploadImageModal} from "@/components/modal/UploadImageModal.tsx";
import {ProductSummaryDTO} from "@/models/ProductSummaryDTO.ts";
import {Category} from "@/models/Category.ts";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {CategoryName} from "@/models/CategoryName.ts";
import {ManufacturerName} from "@/models/ManufacturerName.ts";
import {Card, CardContent} from "@/components/ui/card.tsx";

interface ProductFormProps {
    initialData: ProductDetailsDTO | null;
}

const formSchema = z.object({
    name: z.string().min(1),
    description: z.string().min(1),
    quantity: z.number().min(0),
    netPrice: z.number().min(0),
    grossPrice: z.number().min(0),
    categoryId: z.number().min(1),
    producentId: z.number().min(1),
    vat: z.number().min(0),
    images: z.array(z.string().url("Invalid image URL").optional())
});

type ProductFormValues = z.infer<typeof formSchema>;

export const ProductForm: React.FC<ProductFormProps> = ({initialData}) => {
    const [grossPrice, setGrossPrice] = useState<number>(initialData?.grossPrice || 0);
    const [manufacturers, setManufacturers] = useState<ManufacturerName[] | null>(null);
    const [categories, setCategories] = useState<CategoryName[] | null>(null);

    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [openUploadModal, setOpenUploadModal] = useState(false);

    const [previewImages, setPreviewImages] = useState<string[]>([]);
    const [imageFiles, setImageFiles] = useState<File[]>([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {authState} = useAuth();
    const params = useParams();

    const title = initialData ? "Edit product" : "Create product";
    const description = initialData ? "Edit a product" : "Add a new product";
    const toastMessage = initialData ? "Product updated." : "Product created.";
    const action = initialData ? "Save changes" : "Create";

    const form = useForm<ProductFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: initialData?.name || '',
            description: initialData?.description || '',
            quantity: initialData?.quantity || 0,
            netPrice: initialData?.netPrice || 0,
            grossPrice: initialData?.grossPrice || 0,
            categoryId: initialData?.categoryId || undefined,
            producentId: initialData?.producentId || undefined,
            vat: initialData?.vat || 0,
            images: initialData?.images || []
        }
    });

    const calculateGrossPrice = (netPrice: number, vat: number) => {
        return netPrice + (netPrice * vat / 100);
    };

    useEffect(() => {
        const initialNetPrice = form.getValues('netPrice');
        const initialVat = form.getValues('vat');

        if (!isNaN(initialNetPrice) && !isNaN(initialVat)) {
            setGrossPrice(calculateGrossPrice(initialNetPrice, initialVat));
        }
    }, []);

    useEffect(() => {
        const subscription = form.watch(({ netPrice, vat }) => {
            const netPriceNumber = netPrice!;
            const vatNumber = vat!;
            if (!isNaN(netPriceNumber) && !isNaN(vatNumber)) {
                setGrossPrice(calculateGrossPrice(netPriceNumber, vatNumber));
            }
        });

        return () => subscription.unsubscribe();
    }, [form]);

    useEffect(() => {
        if (initialData?.images) {
            setPreviewImages(initialData.images);
        }
    }, [initialData]);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get<Category[]>(
                    `http://localhost:8080/api/v1/categories/names?storeId=${params.storeId}`);
                setCategories(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchCategories();

        const fetchManufacturers = async () => {
            try {
                const response = await axios.get<ManufacturerName[]>(
                    `http://localhost:8080/api/v1/producents/names?storeId=${params.storeId}`);
                setManufacturers(response.data);
            } catch (error: any) {
                console.log(error);
                navigate("/");
            } finally {
                setLoading(false);
            }
        }

        fetchManufacturers();
    }, [params.storeId]);

    const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = event.target.files;
        if (files) {
            const newPreviewImages = Array.from(files).map(file => URL.createObjectURL(file));
            setPreviewImages(prevImages => [...prevImages, ...newPreviewImages]);
            setImageFiles(prevFiles => [...prevFiles, ...Array.from(files)]);
            form.clearErrors("images");
            setOpenUploadModal(false);
        }
    };

    const handleRemoveImage = (index: number) => {
        setPreviewImages(prevImages => prevImages.filter((_, i) => i !== index));
        setImageFiles(prevFiles => prevFiles.filter((_, i) => i !== index));
        const updatedImages = form.getValues('images').filter((_, i) => i !== index);
        form.setValue('images', updatedImages);
    };

    const handleSubmit = async (data: ProductFormValues) => {
        setLoading(true);
        try {
            // Create or update the product
            let productId: number | null;
            if (initialData === null) {
                const response = await axios.post<ProductSummaryDTO>(
                    `http://localhost:8080/api/v1/products?storeId=${params.storeId}`,
                    {
                        name: data.name,
                        description: data.description,
                        categoryId: data.categoryId,
                        producentId: data.producentId,
                        quantity: data.quantity,
                        netPrice: data.netPrice,
                        vat: data.vat,
                        attributes: {}
                    }, {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`
                        }
                    });
                productId = response.data.id;

                if (productId && imageFiles.length > 0) {
                    const formData = new FormData();
                    imageFiles.forEach((file) => {
                        formData.append(`images`, file);
                    });

                    await axios.post<ProductDetailsDTO>(`http://localhost:8080/api/v1/products/${productId}/images`, formData, {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                }
            } else {
                await axios.patch<ProductSummaryDTO>(`http://localhost:8080/api/v1/products/${initialData.id}`, data, {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`
                    }
                });
                productId = initialData.id;

                if (productId && imageFiles.length > 0) {
                    const formData = new FormData();
                    imageFiles.forEach((file) => {
                        formData.append(`images`, file);
                    });

                    await axios.patch<ProductDetailsDTO>(`http://localhost:8080/api/v1/products/${productId}/images`, formData, {
                        headers: {
                            Authorization: `Bearer ${authState?.token}`,
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                }
            }

            navigate(`/dashboard/${params.storeId}/products`);
            toast.success(toastMessage);
        } catch (error: any) {
            console.log(error);
            toast.error(initialData ? "Failed to update product." : "Failed to create product.");
        } finally {
            setLoading(false);
        }
    };

    const onDelete = async () => {
        try {
            setLoading(true);
            await axios.delete<string>(
                `http://localhost:8080/api/v1/products/${params.productId}`,
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                });
            navigate(`/dashboard/${params.storeId}/products`);
            toast.success('Product deleted.');
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                navigate(`/dashboard/${params.storeId}/products`);
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
                <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-8 w-full">
                    <div className="flex flex-row gap-4">
                        {previewImages.map((image, index) => (
                            <div key={index} className="relative w-[200px] h-[200px] rounded-md overflow-hidden">
                                <div className="z-10 absolute top-2 right-2">
                                    <Button type="button" onClick={() => handleRemoveImage(index)} variant="destructive"
                                            size="icon">
                                        <Trash className="h-4 w-4"/>
                                    </Button>
                                </div>
                                <img className="object-cover w-full h-full" alt="Product Image" src={image}/>
                            </div>
                        ))}
                    </div>
                    <div className="flex flex-row gap-4">
                        <FormField
                            control={form.control}
                            name="images"
                            render={({field}) => (
                                <FormItem>
                                    {/*<FormLabel>Images</FormLabel>*/}
                                    <FormControl>
                                        <Button type="button" variant="secondary"
                                                onClick={() => setOpenUploadModal(true)}>
                                            <ImagePlus className="h-4 w-4 mr-2"/>
                                            Upload Images
                                        </Button>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <div className="grid grid-cols-2 gap-8">
                        <Card>
                            <CardContent>
                                <div className="space-y-8 mt-5">
                                    <FormField
                                        control={form.control}
                                        name="name"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Product Name</FormLabel>
                                                <FormControl>
                                                    <Input disabled={loading} placeholder="Product name" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <FormField
                                        control={form.control}
                                        name="description"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Description</FormLabel>
                                                <FormControl>
                                                    <Input disabled={loading}
                                                           placeholder="Product description" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <FormField
                                        control={form.control}
                                        name="quantity"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Quantity</FormLabel>
                                                <FormControl>
                                                    <Input
                                                        type="number"
                                                        disabled={loading}
                                                        placeholder="Quantity"
                                                        onChange={(e) => field.onChange(Number(e.target.value))}
                                                        defaultValue={field.value}
                                                    />
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <FormField
                                        control={form.control}
                                        name="categoryId"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Category</FormLabel>
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
                                                    <SelectContent className="overflow-auto">
                                                        {categories?.map((category: CategoryName) => (
                                                            <SelectItem
                                                                key={category.id}
                                                                value={category.id.toString()}
                                                            >
                                                                {category.name}
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
                                        name="producentId"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Manufacturers</FormLabel>
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
                                                                aria-placeholder="Select a manufacturer"
                                                            />
                                                        </SelectTrigger>
                                                    </FormControl>
                                                    <SelectContent className="overflow-auto">
                                                        {manufacturers?.map((manufacturer: ManufacturerName) => (
                                                            <SelectItem
                                                                key={manufacturer.id}
                                                                value={manufacturer.id.toString()}
                                                            >
                                                                {manufacturer.name}
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
                                        name="netPrice"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Net Price (zł)</FormLabel>
                                                <FormControl>
                                                    <Input
                                                        type="number"
                                                        disabled={loading}
                                                        placeholder="Net price"
                                                        onChange={(e) => field.onChange(Number(e.target.value))}
                                                        defaultValue={field.value}
                                                    />
                                                </FormControl>
                                                <div className="text-sm text-gray-500">
                                                    Gross Price: {grossPrice.toFixed(2)}
                                                </div>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                    <FormField
                                        control={form.control}
                                        name="vat"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>VAT (%)</FormLabel>
                                                <FormControl>
                                                    <Input
                                                        type="number"
                                                        disabled={loading}
                                                        placeholder="VAT"
                                                        onChange={(e) => field.onChange(Number(e.target.value))}
                                                        defaultValue={field.value}
                                                    />
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </CardContent>
                        </Card>
                        <Card>
                            <CardContent>
                                <div className="mt-5">
                                    <h2 className="text-lg font-semibold mb-4">Attributes</h2>
                                    <div className="border rounded-md p-4">
                                        {/* Placeholder for attributes table */}
                                        <p>Attributes table coming soon...</p>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                    <Button disabled={loading} className="ml-auto" type="submit">
                        {action}
                    </Button>
                </form>
            </Form>
            <Separator/>
        </>
    );
};
