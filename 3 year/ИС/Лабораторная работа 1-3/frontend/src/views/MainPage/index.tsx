import { WordPage } from "../../containers/WordPage";
import { useRef } from "react";
import { MainTable } from "../../components/MainTable";
import { TableOfContents } from "../../components/TableOfContents";
import {CommandsPage} from "../../components/Commands";
import {AdminPanel} from "../../components/AdminPanel";
import {UploadFile} from "../../components/UploadFile";

export function MainPage() {
  const mainTable = useRef(null);
  const page2Ref = useRef(null);
  const page3Ref = useRef(null);
  const page4Ref = useRef(null);


  return <>
    <div className="h-screen snap-y snap-mandatory overflow-y-scroll">
      <TableOfContents pages={[
        {name: "Таблица", page: mainTable},
        {name: "Основные команды", page: page2Ref},
        {name: "Загрузка фидов", page: page3Ref},
        {name: "Админ панель", page: page4Ref},
      ]} />
      <div className="snap-start">
        <WordPage refProp={ mainTable }><MainTable/></WordPage>
      </div>
      <div className="snap-start">
        <WordPage refProp={ page2Ref }><CommandsPage/></WordPage>
      </div>
      <div className="snap-start">
        <WordPage refProp={ page3Ref }><UploadFile/></WordPage>
      </div>

      <div className="snap-start">
        <WordPage refProp={ page4Ref }><AdminPanel/></WordPage>
      </div>

    </div>
  </>
}