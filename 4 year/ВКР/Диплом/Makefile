PUML_SRC := $(wildcard res/diagrams/*.puml)
MMD_SRC  := $(wildcard res/diagrams/*.mmd)
PUML_OUT := $(patsubst res/diagrams/%.puml,res/img/%.svg,$(PUML_SRC))
MMD_OUT  := $(patsubst res/diagrams/%.mmd,res/img/%.svg,$(MMD_SRC))
DIAGRAM_OUT := $(PUML_OUT) $(MMD_OUT)

.PHONY: all pdf diagrams watch clean help

all: diagrams pdf

pdf: main.pdf

main.pdf: main.typ prelude.typ $(DIAGRAM_OUT)
	typst compile main.typ

diagrams: $(DIAGRAM_OUT)

res/img/%.svg: res/diagrams/%.puml
	plantuml -tsvg -o '../img' $<

res/img/%.svg: res/diagrams/%.mmd
	mmdc -i $< -o $@ -t neutral -b transparent

watch:
	typst watch main.typ

clean:
	rm -f main.pdf $(DIAGRAM_OUT)

format:
	typstyle -i main.typ

help:
	@echo "Targets:"
	@echo "  make           — пересобрать диаграммы и PDF"
	@echo "  make pdf       — собрать только PDF из main.typ"
	@echo "  make diagrams  — пересобрать все PlantUML-диаграммы в SVG"
	@echo "  make watch     — typst watch с автопересборкой PDF"
	@echo "  make clean     — удалить PDF и сгенерированные SVG"
	@echo "  make format    - отформатировать документ"
