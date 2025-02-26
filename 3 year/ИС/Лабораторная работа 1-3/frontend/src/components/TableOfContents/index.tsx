interface TableOfContentsElement {
  name: string;
  page: React.RefObject<HTMLDivElement>
}

export function TableOfContents({ pages }: {pages: Array<TableOfContentsElement>}) {
  const scrollToPage = (pageRef: React.RefObject<HTMLDivElement>) => {
    pageRef.current?.scrollIntoView({ behavior: 'smooth' });
  };
  return <div className="snap-start flex flex-col justify-start items-start min-h-screen bg-white p-40">
    <h2 className="text-xl font-bold mb-4">Оглавление</h2>
    { pages.map(({ name, page }, index) => (
      <ul>
        <li className="mb-2">
          <button
            onClick={ () => scrollToPage(page) }
            className="text-blue-600 hover:underline"
          >
            {index + 1}) { name }{".".repeat((250 - name.length * 2))}{index+1}
          </button>
        </li>
      </ul>
    )) }
  </div>
}