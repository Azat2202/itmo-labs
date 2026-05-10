/*
  Title page layout.
  - No pagebreak after title page, caller should do it. 
  - No desabling page numbering, caller should do it.
*/
#let title_page(
  title: [],
  author: []
) = [
  #align(center)[
    #v(10cm)
    #text(24pt, weight: "bold")[#title]
    #v(1cm)
    #author
  ]
]