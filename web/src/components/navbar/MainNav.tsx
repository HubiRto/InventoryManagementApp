import React from "react";
import {cn} from "@/lib/utils.ts";
import {Link, useLocation, useParams} from "react-router-dom";

export function MainNav({
    className
}: React.HTMLAttributes<HTMLElement>) {
    const location = useLocation();
    const params = useParams();

    const routes = [
        {
            href: `/dashboard/${params.storeId}`,
            label: "Overview",
            active: location.pathname === `/dashboard/${params.storeId}`
        },
        {
            href: `/dashboard/${params.storeId}/billboards`,
            label: "Billboards",
            active: location.pathname === `/dashboard/${params.storeId}/billboards`
        },
        {
            href: `/dashboard/${params.storeId}/categories`,
            label: "Categories",
            active: location.pathname === `/dashboard/${params.storeId}/categories`
        },
        {
            href: `/dashboard/${params.storeId}/settings`,
            label: "Settings",
            active: location.pathname === `/dashboard/${params.storeId}/settings`
        }
    ];

    return (
      <nav className={cn("flex items-center space-x-4 lg:space-x-6", className)}>
          {routes.map((route) => (
              <Link
                  key={route.href}
                  to={route.href}
                  className={cn("text-sm font-medium transition-colors hover:text-primary",
                  route.active ? "text-black dark:text-white": "text-muted-foreground")}
              >
                  {route.label}
              </Link>
          ))}
      </nav>
    );
}