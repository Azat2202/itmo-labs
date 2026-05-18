#!/usr/bin/env python3
"""
Convert <foreignObject> labels emitted by mermaid-cli (v11) into native
SVG <text> elements so Typst can render diagram labels.

Mermaid v11 ignores `flowchart.htmlLabels: false` for node labels and always
wraps them in <foreignObject><div><span><p>...</p></span></div></foreignObject>.
Typst does not render foreignObject content (see typst/typst#1421), which
leaves the diagram looking like empty grey rectangles.

This script rewrites every <foreignObject> in place into a centred SVG <text>
element with the same width/height as the foreignObject, joining nested
<p>/<br> blocks with line breaks.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path
from xml.sax.saxutils import escape


FOREIGN_OBJECT_RE = re.compile(
    r'<foreignObject\b([^>]*)>(.*?)</foreignObject>',
    re.DOTALL,
)
ATTR_RE = re.compile(r'(\w+)\s*=\s*"([^"]*)"')
TAG_RE = re.compile(r'<[^>]+>')
WHITESPACE_RE = re.compile(r'\s+')


def extract_lines(inner_html: str) -> list[str]:
    parts = re.split(r'<\s*br\s*/?\s*>|</p>\s*<p[^>]*>', inner_html, flags=re.IGNORECASE)
    lines = []
    for part in parts:
        text = TAG_RE.sub('', part)
        text = WHITESPACE_RE.sub(' ', text).strip()
        if text:
            lines.append(text)
    return lines


def replace_foreign_object(match: re.Match) -> str:
    raw_attrs = match.group(1)
    inner = match.group(2)

    attrs = dict(ATTR_RE.findall(raw_attrs))
    try:
        width = float(attrs.get('width', '0'))
        height = float(attrs.get('height', '0'))
    except ValueError:
        width = height = 0.0

    lines = extract_lines(inner)
    if not lines:
        return ''

    cx = width / 2
    cy = height / 2
    line_height = 1.2
    total = (len(lines) - 1) * line_height
    first_dy = -total / 2

    tspans = []
    for i, line in enumerate(lines):
        dy = f'{first_dy:.2f}em' if i == 0 else f'{line_height:.2f}em'
        tspans.append(f'<tspan x="{cx:.3f}" dy="{dy}">{escape(line)}</tspan>')

    return (
        f'<text x="{cx:.3f}" y="{cy:.3f}" text-anchor="middle" '
        f'dominant-baseline="central">{"".join(tspans)}</text>'
    )


def fix_svg(path: Path) -> bool:
    src = path.read_text(encoding='utf-8')
    fixed = FOREIGN_OBJECT_RE.sub(replace_foreign_object, src)
    if fixed == src:
        return False
    path.write_text(fixed, encoding='utf-8')
    return True


def main(argv: list[str]) -> int:
    if len(argv) < 2:
        print(f'usage: {argv[0]} <svg> [<svg> ...]', file=sys.stderr)
        return 2
    for arg in argv[1:]:
        path = Path(arg)
        changed = fix_svg(path)
        marker = 'rewrote' if changed else 'unchanged'
        print(f'{marker}: {path}')
    return 0


if __name__ == '__main__':
    sys.exit(main(sys.argv))
