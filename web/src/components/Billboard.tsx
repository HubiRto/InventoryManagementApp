import {Billboard as BillboardType} from "@/types.ts";
import React from "react";

interface BillboardProps {
    data: BillboardType;
}

export const Billboard: React.FC<BillboardProps> = ({data}) => {
    return (
      <div className="p-4 sm:p-6 lg:p-8 rounded-xl overflow-hidden">
        <div
            className="rounded-xl relative aspect-square md:aspect-[2.4/1] overflow-hidden bg-cover"
            style={{backgroundImage: `url(${data?.imageUrl}`}}
        >
            <div className="h-full w-full flex flex-col justify-center items-center text-center gap-y-8">
                <div
                    className="text-white font-bold text-3xl sm:text-5xl lg:text-6xl sm:max-w-xl max-w-xs"
                    style={{textShadow: "2px 2px black"}}
                >
                    <span className="drop-shadow-2xl">{data.label}</span>
                </div>
            </div>
        </div>
      </div>
    );
};
