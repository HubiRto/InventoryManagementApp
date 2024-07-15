import React, {forwardRef} from "react";
import {cn} from "@/lib/utils.ts";

export interface ShoppingCartButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
}

export const ShoppingCartButton = forwardRef<HTMLButtonElement, ShoppingCartButtonProps>
(({className, children, disabled, type = "button", ...props}, ref) => {
    return (
        <button
            className={cn(
                `
                w-auto
                rounded-full
                bg-black
                border-transparent
                px-5
                py-3
                disabled:cursor-not-allowed
                disabled:opacity-50
                text-white
                font-semibold
                hover:opacity-75
                transition
                `,
                className
            )}
            ref={ref}
        >
            {children}
        </button>
    );
});