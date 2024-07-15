import React from 'react';
import { Category } from '@/types.ts';

interface CategorySelectProps {
    category: Category;
    level: number;
}

const CategorySelect: React.FC<CategorySelectProps> = ({ category, level }) => {
    const hasChildren = category.children && category.children.length > 0;

    return (
        <div className={`ml-${level * 4}`}>
            <label className="block text-sm font-medium text-gray-700">{category.name}</label>
            <select
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
            >
                <option value={category.id}>{category.name}</option>
                {hasChildren && category.children!.map((child) => (
                    <option key={child.id} value={child.id}>{child.name}</option>
                ))}
            </select>
            {hasChildren && category.children!.map((child) => (
                <CategorySelect key={child.id} category={child} level={level + 1} />
            ))}
        </div>
    );
};

export default CategorySelect;
