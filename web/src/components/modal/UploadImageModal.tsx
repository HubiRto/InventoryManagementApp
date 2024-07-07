import {Modal} from "@/components/modal/Modal.tsx";
import {Button} from "@/components/ui/button.tsx";
import React, {useEffect, useState} from "react";
import {UploadIcon} from "lucide-react";

interface AlertModalProps {
    isOpen: boolean;
    onClose: () => void;
    onImageChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

export const UploadImageModal: React.FC<AlertModalProps> = ({isOpen, onClose, onImageChange}) => {
    const [isMounted, setIsMounted] = useState(false);

    useEffect(() => {
        setIsMounted(true);
    }, []);

    if (!isMounted) {
        return null;
    }

    return (
        <Modal
            title="Upload Image"
            description="Drag and drop your image or click to select a file from your computer."
            isOpen={isOpen}
            onClose={onClose}
        >
            <div className="grid gap-4 py-4">
                <div
                    className="group relative flex h-48 cursor-pointer items-center justify-center rounded-lg border-2 border-dashed border-gray-300 bg-gray-50 transition-colors hover:border-primary hover:bg-gray-100 dark:border-gray-700 dark:bg-gray-900 dark:hover:border-primary">
                    <div className="z-10 text-center">
                        <UploadIcon className="mx-auto h-8 w-8 text-gray-400 group-hover:text-primary"/>
                        <p className="mt-2 text-sm text-gray-500 dark:text-gray-400">
                            Drag and drop your image here or click to select a file
                        </p>
                        <Button variant="outline" size="sm" className="mt-4 w-full">
                            Select File
                        </Button>
                    </div>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={onImageChange}
                        className="absolute inset-0 z-20 h-full w-full cursor-pointer opacity-0"
                    />
                </div>
            </div>
        </Modal>
    );
};