import {ReactNode} from "react";

function BoxDiv({children}: {children: ReactNode}){
    return (
        <div className="border-2 shadow-xl hover:shadow-2xl border-gray-700 rounded-3xl p-3 m-3 h-max">
            {children}
        </div>
    )
}

export default BoxDiv;
