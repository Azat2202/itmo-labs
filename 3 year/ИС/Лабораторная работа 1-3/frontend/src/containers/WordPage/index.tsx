import { ReactNode } from "react";

export function WordPage({ children, refProp } : {children: ReactNode, refProp: React.RefObject<HTMLDivElement>}){
  return <div ref={refProp} className="flex justify-start items-start flex-col min-h-screen bg-white">
    <div className="prose">{ children }</div>
  </div>
}