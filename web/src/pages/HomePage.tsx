import Navbar from "@/components/Navbar.tsx";
import Footer from "@/components/Footer.tsx";
import {Container} from "@/components/Container.tsx";
import {Billboard} from "@/components/Billboard.tsx";
import {Billboard as BillboardType, ProductSummary} from "@/types.ts"
import {useEffect, useState} from "react";
import axios from "axios";
import {ProductList} from "@/components/ProductList.tsx";

const HomePage = () => {
    const [billboard, setBillboard] = useState<BillboardType | undefined>(undefined);
    const [loadingBillboards, setLoadingBillboards] = useState<boolean>();

    const [products, setProducts] = useState<ProductSummary[] | undefined>(undefined);
    const [loadingProducts, setLoadingProducts] = useState<boolean>();

    useEffect(() => {
        const fetchBillboard = async () => {
            setLoadingBillboards(true);
            try {
                const response = await axios.get<BillboardType>(
                    `http://localhost:8080/api/v1/billboards/5`);
                setBillboard(response.data);
            } catch (error: any) {
                console.log(error);
            } finally {
                setLoadingBillboards(false);
            }
        }

        fetchBillboard();

        const fetchProducts = async () => {
            setLoadingProducts(true);
            try {
                const response = await axios.get<ProductSummary[]>(
                    `http://localhost:8080/api/v1/products?storeId=1`);
                setProducts(response.data);
            } catch (error: any) {
                console.log(error);
            } finally {
                setLoadingProducts(false);
            }
        }

        fetchProducts();
    }, []);

    return (
        <>
            <Navbar/>
            <Container>
                <div className="space-y-10 pb-10">
                    {(!loadingBillboards && billboard) && (
                        <Billboard data={billboard}/>
                    )}
                </div>
                <div className="flex flex-col gap-y-8 px-4 sm:px-6 lg:px-8">
                    {(!loadingProducts && products) && (
                        <ProductList title="Featured products" items={products}/>
                    )}
                </div>
            </Container>
            <Footer/>
        </>
    );
};

export default HomePage;