import {useStoreModal} from "@/hooks/UseStoreModal.tsx";
import {Modal} from "@/components/modal/Modal.tsx";
import * as z from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useState} from "react";
import axios from "axios";
import {useAuth} from "@/providers/AuthContext.tsx";
import {Store} from "@/models/Store.ts";
import toast from "react-hot-toast";

const formSchema = z.object({
    name: z.string().min(3),
});

export const StoreModal = () => {
    const storeModal = useStoreModal();
    const { authState} = useAuth();
    const [loading, setLoading] = useState(false);
    // const navigate = useNavigate();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: ""
        },
    });

    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        try {
            setLoading(true);
            const response = await axios.post<Store>(
                `http://localhost:8080/api/v1/stores`,
                { name: values.name },
                {
                    headers: {
                        Authorization: `Bearer ${authState?.token}`,
                    },
                }
            );
            window.location.assign(`/dashboard/${response.data.id}`);
            toast.success('Successfully create store');
            // navigate(`/dashboard/${response.data.id}`)
        } catch (error: any) {
            if (error.response && error.response.status === 409) {
                toast.error("Create store failed");
                form.setError("name", {message: "Store with this name already exist"});
            } else {
                toast.error("Create store failed. Server error");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal
            title="Create store"
            description="Add a new store to mange products and categories"
            isOpen={storeModal.isOpen}
            onClose={storeModal.onClose}
        >
            <div>
                <div className="space-y-4 py-2 pb-4">
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)}>
                            <FormField
                                control={form.control}
                                name="name"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Name</FormLabel>
                                        <FormControl>
                                            <Input disabled={loading} placeholder="E-Commerce" {...field} />
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                            <div className="pt-6 space-x-2 flex items-center justify-end w-full">
                                <Button disabled={loading} variant="outline"
                                        onClick={storeModal.onClose}>Cancel</Button>
                                <Button disabled={loading} type="submit">Continue</Button>
                            </div>
                        </form>
                    </Form>
                </div>
            </div>
        </Modal>
    );
};