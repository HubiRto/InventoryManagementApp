import {cn} from "@/lib/utils.ts";

const DialogIcon = ({className, children, ...props}: {className?: string, children: React.ReactNode}) => {
    return (
        <div className={cn("p-3 border border-secondary rounded-lg w-fit shadow-sm mb-2", className)}
             {...props}
        >
            {children}
        </div>
    )
}
DialogIcon.displayName = "DialogIcon"