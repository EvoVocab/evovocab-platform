import type { BarDatum, BarItemProps } from '@nivo/bar';
import type { JSX } from 'react';

function SegmentBar({ bar }: BarItemProps<BarDatum>): JSX.Element {
  const raw = bar.data.data;
  const met = raw[`${bar.data.id}_met`] === 1;

  return (
    <rect
      className={`transition-colors duration-300 ${met ? String(raw.fillClass) : 'fill-muted'}`}
      height={bar.height}
      rx={2}
      width={bar.width}
      x={bar.x}
      y={bar.y}
    />
  );
}

export { SegmentBar };
