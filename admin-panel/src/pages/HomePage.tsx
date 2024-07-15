import {useStoreModal} from "@/hooks/UseStoreModal.tsx";
import {useEffect} from "react";

export default function HomePage() {
    const onOpen = useStoreModal((state) => state.onOpen);
    const isOpen = useStoreModal((state) => state.isOpen);

    useEffect(() => {
        if(!isOpen) onOpen();
    }, [isOpen, onOpen]);

    return (
        <div className="p-4">
            Home page
        </div>
    );
};