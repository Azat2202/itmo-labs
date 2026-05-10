DIAGRAM_SRC := $(wildcard res/diagrams/*.puml)
DIAGRAM_OUT := $(patsubst res/diagrams/%.puml,res/img/%.svg,$(DIAGRAM_SRC))

.PHONY: all pdf diagrams watch clean help

all: diagrams pdf

pdf: main.pdf

main.pdf: main.typ prelude.typ $(DIAGRAM_OUT)
	typst compile main.typ

diagrams: $(DIAGRAM_OUT)

res/img/%.svg: res/diagrams/%.puml
	plantuml -tsvg -o '../img' $<

watch:
	typst watch main.typ

clean:
	rm -f main.pdf $(DIAGRAM_OUT)

help:
	@echo "Targets:"
	@echo "  make           — пересобрать диаграммы и PDF"
	@echo "  make pdf       — собрать только PDF из main.typ"
	@echo "  make diagrams  — пересобрать все PlantUML-диаграммы в SVG"
	@echo "  make watch     — typst watch с автопересборкой PDF"
	@echo "  make clean     — удалить PDF и сгенерированные SVG"
