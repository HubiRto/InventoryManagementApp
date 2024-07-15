import React from "react";

const formatter = new Intl.NumberFormat("pl-PL", {
    style: 'currency',
    currency: 'PLN'
})

interface CurrencyProps {
    value?: string | number;
}

export const Currency: React.FC<CurrencyProps> = ({value}) => {
    return (
        <div className="font-semibold">
            {formatter.format(Number(value))}
        </div>
    );
};